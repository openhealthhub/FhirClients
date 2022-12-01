import { __awaiter } from 'tslib';
import { readKey, createMessage, encrypt } from 'openpgp';
import _ from 'lodash';

class EncryptService {
    loadKey(key) {
        return __awaiter(this, void 0, void 0, function* () {
            this.practitionerKey = key;
            this.key = yield readKey({ armoredKey: key.publicKey });
        });
    }
    encrypt(value, key) {
        return __awaiter(this, void 0, void 0, function* () {
            if (!this.key) {
                throw new Error('no key loaded, did you call loadKey?');
            }
            if (this.practitionerKey !== key) {
                throw new Error('trying to encrypt with incorrect key. Did you forget to call loadKey with the new key?');
            }
            const message = yield createMessage({ text: value });
            return encrypt({ message, config: {}, encryptionKeys: this.key });
        });
    }
}

const STRUCTURE_DEFINITION_URL = 'https://api.openhealthhub.com/OpenHealthhub/fhir/4/StructureDefinition/';
const CODE_SYSTEM_URL = 'https://api.openhealthhub.com/OpenHealthhub/fhir/4/CodeSystem/';

const PARTICIPANT_PUBLIC_KEY_UUID_SYSTEM = 'urn:ietf:rfc:3986';
const PARTICIPANT_PUBLIC_KEY_URL = `${STRUCTURE_DEFINITION_URL}participant-program-public-key`;
class ParticipantKeyService {
    toParticipantKey(practitioner) {
        var _a, _b, _c, _d;
        if (!practitioner.id) {
            throw new Error('public key resource author should have an id');
        }
        const keyUuid = (_b = (_a = practitioner.identifier) === null || _a === void 0 ? void 0 : _a.find(i => i.system === PARTICIPANT_PUBLIC_KEY_UUID_SYSTEM)) === null || _b === void 0 ? void 0 : _b.value;
        if (!keyUuid) {
            throw new Error('public key resource author should contain a key uuid as identifier');
        }
        const publicKey = (_d = (_c = practitioner.extension) === null || _c === void 0 ? void 0 : _c.find(ext => ext.url === PARTICIPANT_PUBLIC_KEY_URL)) === null || _d === void 0 ? void 0 : _d.valueString;
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
    var _a;
    const type = SupportedAnswerType[typeString];
    switch (type) {
        case SupportedAnswerType.ATTACHMENT:
            return (_a = answer.valueAttachment) === null || _a === void 0 ? void 0 : _a.data;
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
    createEncryptedResponse(originalResponse) {
        var _a;
        return __awaiter(this, void 0, void 0, function* () {
            const response = _.cloneDeep(originalResponse);
            response.meta = (_a = response.meta) !== null && _a !== void 0 ? _a : {};
            response.meta.profile = [`${STRUCTURE_DEFINITION_URL}EncryptedQuestionnaireResponse`];
            this.setPractitioner(response, this.key.practitioner);
            yield this.encryptAnswers(response);
            return response;
        });
    }
    encryptAnswers(response) {
        return __awaiter(this, void 0, void 0, function* () {
            const itemMap = {};
            yield this.encryptItem(itemMap, response.item);
            yield this.encryptAllAnswers(itemMap, response);
        });
    }
    encryptItem(itemMap, items = []) {
        return __awaiter(this, void 0, void 0, function* () {
            yield Promise.all(items.map((item) => __awaiter(this, void 0, void 0, function* () {
                const answers = item.answer;
                if (answers && answers.length > 0) {
                    yield Promise.all(answers.map((answer) => __awaiter(this, void 0, void 0, function* () {
                        yield this.encryptAnswer(answer, item, itemMap);
                    })));
                }
                yield this.encryptItem(itemMap, item.item);
            })));
        });
    }
    encryptAnswer(answer, item, itemMap) {
        var _a, _b;
        return __awaiter(this, void 0, void 0, function* () {
            const answerType = getAnswerType(answer);
            const value = getValueForType(answer, answerType);
            const extensions = (_a = answer.extension) !== null && _a !== void 0 ? _a : [];
            const codes = (_b = extensions.filter(ext => ext.url === `${STRUCTURE_DEFINITION_URL}question-answer-option-coding`)
                .filter(ext => !!ext.valueCoding)
                .map(ext => ext.valueCoding)) !== null && _b !== void 0 ? _b : [];
            if (value) {
                const answerArray = item.linkId in itemMap ? itemMap[item.linkId] : [];
                const valueIsCode = typeof value !== 'string' && typeof value !== 'number';
                const actValue = valueIsCode ? undefined : value;
                const actualCodes = (valueIsCode ? [...codes, value] : codes);
                const encryptedObj = { value: actValue, codes: actualCodes };
                answerArray.push(encryptedObj);
                itemMap[item.linkId] = answerArray;
                yield this.setEncryptedValue(answer, encryptedObj, answerType);
            }
        });
    }
    setEncryptedValue(answer, value, answerType) {
        return __awaiter(this, void 0, void 0, function* () {
            const valueString = yield this.encryptValue(value);
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
        });
    }
    createValueWithEncryptExtension(valueString, extensionType) {
        return { extension: [{ url: STRUCTURE_DEFINITION_URL + extensionType, valueString }] };
    }
    encryptAllAnswers(itemMap, response) {
        return __awaiter(this, void 0, void 0, function* () {
            const allEncrypted = yield this.encryptValue(itemMap);
            response.extension = [{ url: `${STRUCTURE_DEFINITION_URL}encryptedAnswers`, valueString: allEncrypted }];
        });
    }
    encryptValue(value) {
        return __awaiter(this, void 0, void 0, function* () {
            return this.encryptService.encrypt(JSON.stringify(value), this.key);
        });
    }
    setPractitioner(response, practitioner) {
        var _a;
        const contained = (_a = response.contained) !== null && _a !== void 0 ? _a : [];
        contained.push(practitioner);
        response.contained = contained;
        response.author = { reference: `#${practitioner.id}` };
    }
}

function encryptQuestionnaireResponse(response, practitioners = []) {
    return __awaiter(this, void 0, void 0, function* () {
        const bundle = { resourceType: 'Bundle', type: 'transaction' };
        bundle.entry = yield Promise.all(practitioners.map((practitioner) => __awaiter(this, void 0, void 0, function* () {
            const entry = {};
            entry.resource = yield encryptForKey(response, practitioner);
            return entry;
        })));
        return bundle;
    });
}
const encryptForKey = (response, practitioner) => __awaiter(void 0, void 0, void 0, function* () {
    const key = new ParticipantKeyService().toParticipantKey(practitioner);
    const encryptService = new EncryptService();
    yield encryptService.loadKey(key);
    const qrService = new QuestionnaireResponseService(encryptService, key);
    return qrService.createEncryptedResponse(response);
});

/**
 * Generated bundle index. Do not edit.
 */

export { encryptQuestionnaireResponse };
//# sourceMappingURL=openhealthhub-fhir-encrypter.mjs.map
