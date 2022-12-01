import { readKey, createMessage, encrypt } from 'openpgp';
import _ from 'lodash';

class EncryptService {
    async loadKey(key) {
        this.practitionerKey = key;
        this.key = await readKey({ armoredKey: key.publicKey });
    }
    async encrypt(value, key) {
        if (!this.key) {
            throw new Error('no key loaded, did you call loadKey?');
        }
        if (this.practitionerKey !== key) {
            throw new Error('trying to encrypt with incorrect key. Did you forget to call loadKey with the new key?');
        }
        const message = await createMessage({ text: value });
        return encrypt({ message, config: {}, encryptionKeys: this.key });
    }
}

const STRUCTURE_DEFINITION_URL = 'https://api.openhealthhub.com/OpenHealthhub/fhir/4/StructureDefinition/';
const CODE_SYSTEM_URL = 'https://api.openhealthhub.com/OpenHealthhub/fhir/4/CodeSystem/';

const PARTICIPANT_PUBLIC_KEY_UUID_SYSTEM = 'urn:ietf:rfc:3986';
const PARTICIPANT_PUBLIC_KEY_URL = `${STRUCTURE_DEFINITION_URL}participant-program-public-key`;
class ParticipantKeyService {
    toParticipantKey(practitioner) {
        if (!practitioner.id) {
            throw new Error('public key resource author should have an id');
        }
        const keyUuid = practitioner.identifier?.find(i => i.system === PARTICIPANT_PUBLIC_KEY_UUID_SYSTEM)?.value;
        if (!keyUuid) {
            throw new Error('public key resource author should contain a key uuid as identifier');
        }
        const publicKey = practitioner.extension?.find(ext => ext.url === PARTICIPANT_PUBLIC_KEY_URL)?.valueString;
        if (!publicKey) {
            throw new Error('public key resource should have an extension containing the actual public key value');
        }
        return {
            keyUuid,
            practitioner,
            publicKey,
            userUuid: practitioner.id
        };
    }
}

var SupportedAnswerType;
(function (SupportedAnswerType) {
    SupportedAnswerType["STRING"] = "STRING";
    SupportedAnswerType["DECIMAL"] = "DECIMAL";
    SupportedAnswerType["DATE"] = "DATE";
    SupportedAnswerType["ATTACHMENT"] = "ATTACHMENT";
    SupportedAnswerType["CODING"] = "CODING";
})(SupportedAnswerType || (SupportedAnswerType = {}));
const getValueForType = (answer, typeString) => {
    const type = SupportedAnswerType[typeString];
    switch (type) {
        case SupportedAnswerType.ATTACHMENT:
            return answer.valueAttachment?.data;
        case SupportedAnswerType.DECIMAL:
            return answer.valueDecimal;
        case SupportedAnswerType.DATE:
            return answer.valueDate;
        case SupportedAnswerType.STRING:
            return answer.valueString;
        case SupportedAnswerType.CODING:
            return answer.valueCoding;
        default:
            throw new Error('programming error, forgot to add new SupportedAnswerType');
    }
};
const getFieldNameForType = (type, answer) => {
    const key = `value${type.charAt(0).toUpperCase() + type.slice(1).toLowerCase()}`;
    return key;
};
const getAnswerType = (answer) => {
    for (const type in SupportedAnswerType) {
        const fieldName = getFieldNameForType(type, answer);
        if (fieldName && answer[fieldName]) {
            return type;
        }
    }
    throw new Error(`answer set, but using unsupported value, supported values are: [${Object.values(SupportedAnswerType).join(', ')}]`);
};

class QuestionnaireResponseService {
    constructor(encryptService, key) {
        this.encryptService = encryptService;
        this.key = key;
    }
    async createEncryptedResponse(originalResponse) {
        const response = _.cloneDeep(originalResponse);
        response.meta = response.meta ?? {};
        response.meta.profile = [`${STRUCTURE_DEFINITION_URL}EncryptedQuestionnaireResponse`];
        this.setPractitioner(response, this.key.practitioner);
        await this.encryptAnswers(response);
        return response;
    }
    async encryptAnswers(response) {
        const itemMap = {};
        await this.encryptItem(itemMap, response.item);
        await this.encryptAllAnswers(itemMap, response);
    }
    async encryptItem(itemMap, items = []) {
        await Promise.all(items.map(async (item) => {
            const answers = item.answer;
            if (answers && answers.length > 0) {
                await Promise.all(answers.map(async (answer) => {
                    await this.encryptAnswer(answer, item, itemMap);
                }));
            }
            await this.encryptItem(itemMap, item.item);
        }));
    }
    async encryptAnswer(answer, item, itemMap) {
        const answerType = getAnswerType(answer);
        const value = getValueForType(answer, answerType);
        const extensions = answer.extension ?? [];
        const codes = extensions.filter(ext => ext.url === `${STRUCTURE_DEFINITION_URL}question-answer-option-coding`)
            .filter(ext => !!ext.valueCoding)
            .map(ext => ext.valueCoding) ?? [];
        if (value) {
            const answerArray = item.linkId in itemMap ? itemMap[item.linkId] : [];
            const valueIsCode = typeof value !== 'string' && typeof value !== 'number';
            const actValue = valueIsCode ? undefined : value;
            const actualCodes = (valueIsCode ? [...codes, value] : codes);
            const encryptedObj = { value: actValue, codes: actualCodes };
            answerArray.push(encryptedObj);
            itemMap[item.linkId] = answerArray;
            await this.setEncryptedValue(answer, encryptedObj, answerType);
        }
    }
    async setEncryptedValue(answer, value, answerType) {
        const valueString = await this.encryptValue(value);
        switch (answerType) {
            case 'ATTACHMENT':
                answer.valueAttachment = this.createValueWithEncryptExtension(valueString, 'encrypted-attachment');
                return;
            case 'DATE':
                // eslint-disable-next-line no-underscore-dangle
                answer._valueDate = this.createValueWithEncryptExtension(valueString, 'encrypted-dateType');
                delete answer.valueDate;
                return;
            case 'DECIMAL':
                // @ts-expect-error: _valueDecimal should be present in FHIR types
                // eslint-disable-next-line no-underscore-dangle
                answer._valueDecimal = this.createValueWithEncryptExtension(valueString, 'encrypted-decimalType');
                delete answer.valueDecimal;
                return;
            case 'STRING':
                // eslint-disable-next-line no-underscore-dangle
                answer._valueString = this.createValueWithEncryptExtension(valueString, 'encrypted-stringType');
                delete answer.valueString;
                return;
            case 'CODING':
                answer.valueCoding = this.createValueWithEncryptExtension(valueString, 'encrypted-coding');
                return;
            default:
                throw new Error('unknown answerType');
        }
    }
    createValueWithEncryptExtension(valueString, extensionType) {
        return { extension: [{ url: STRUCTURE_DEFINITION_URL + extensionType, valueString }] };
    }
    async encryptAllAnswers(itemMap, response) {
        const allEncrypted = await this.encryptValue(itemMap);
        response.extension = [{ url: `${STRUCTURE_DEFINITION_URL}encryptedAnswers`, valueString: allEncrypted }];
    }
    async encryptValue(value) {
        return this.encryptService.encrypt(JSON.stringify(value), this.key);
    }
    setPractitioner(response, practitioner) {
        const contained = response.contained ?? [];
        contained.push(practitioner);
        response.contained = contained;
        response.author = { reference: `#${practitioner.id}` };
    }
}

async function encryptQuestionnaireResponse(response, practitioners = []) {
    const bundle = { resourceType: 'Bundle', type: 'transaction' };
    bundle.entry = await Promise.all(practitioners.map(async (practitioner) => {
        const entry = {};
        entry.resource = await encryptForKey(response, practitioner);
        return entry;
    }));
    return bundle;
}
const encryptForKey = async (response, practitioner) => {
    const key = new ParticipantKeyService().toParticipantKey(practitioner);
    const encryptService = new EncryptService();
    await encryptService.loadKey(key);
    const qrService = new QuestionnaireResponseService(encryptService, key);
    return qrService.createEncryptedResponse(response);
};

/**
 * Generated bundle index. Do not edit.
 */

export { encryptQuestionnaireResponse };
//# sourceMappingURL=openhealthhub-fhir-encrypter.mjs.map
