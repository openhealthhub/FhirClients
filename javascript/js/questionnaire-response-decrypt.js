import privateKey from '../../sandbox.key';
import {decrypt, decryptKey, readMessage, readPrivateKey} from "openpgp";

export class QuestionnaireResponseDecryptClient {
  constructor() {
    readPrivateKey({armoredKey: privateKey}).then(async (keys) => this.pgpPrivKeyDecrypted = await this.decryptPrivateKey(keys));
  }

  async decryptPrivateKey(keys) {
    return decryptKey({privateKey: keys, passphrase: 'api-sandbox'});
  }

  async handleResponse(resp) {
    if (this.isEncryptedResponse(resp)) {
      const decryptedValues = await this.decrypt(resp);

      this.addToResponse(resp, decryptedValues);
    }

    return resp;
  }

  isEncryptedResponse(resp) {
    return resp.meta.profile.some(profile => profile === 'https://api.openhealthhub.com/OpenHealthhub/fhir/4/StructureDefinition/EncryptedQuestionnaireResponse');
  }

  addToResponse(resp, decryptedValues) {
    const keys = Object.keys(decryptedValues);
    resp.item.forEach(item => {
      this.addAnswersToItem(keys, item, decryptedValues);
    })
  }

  addAnswersToItem(keys, item, decryptedValues) {
    if (keys.includes(item.linkId)) {
      this.setDecryptedAnswer(item, decryptedValues[item.linkId]);
    }

    if (item.item) {
      item.item.forEach(nestedItem => this.addAnswersToItem(keys, nestedItem, decryptedValues));
    }
  }

  setDecryptedAnswer(item, decryptedValue) {
    item.answer.forEach((answer, i) => {
      const decryptedObj = decryptedValue[i];
      if (answer._valueString) {
        answer.valueString = decryptedObj.value;
      }

      if (answer.valueCoding) {
        answer.valueCoding.code = decryptedObj.value;
      }

      if (answer._valueDecimal) {
        answer.valueDecimal = decryptedObj.value;
      }

      if (answer.valueAttachment) {
        answer.valueAttachment.data = decryptedObj.value;
      }

      if (answer._valueDate) {
        answer.valueDate = decryptedObj.value;
      }

      decryptedObj.codes.forEach(code => {
        item.answer.push({
          valueCoding: {
            code: code.code,
            system: code.system,
            version: code.version,
            display: code.display
          }
        })
      })
    })
  }

  async decrypt(response) {
    const stringValue = response.extension[0].valueString;
    return await this.decryptValue(stringValue);
  }

  async decryptValue(value) {
    const messageObj = await readMessage({armoredMessage: value});

    const result = await decrypt({message: messageObj, decryptionKeys: [this.pgpPrivKeyDecrypted]});
    return JSON.parse(result.data);
  }
}
