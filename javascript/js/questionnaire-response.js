import * as openpgp from 'openpgp/dist/compat/openpgp';
import privateKey from '../../sandbox.key';
import Client from "./client";

class QuestionnaireResponseClient {
  constructor() {
    openpgp.key.readArmored(privateKey).then(async (keys) => this.pgpPrivKeyDecrypted = await this.decryptPrivateKey(keys));
  }

  async decryptPrivateKey(keys) {
    let key = keys.keys[0];

    await key.decrypt('api-sandbox');

    return key;
  }

  async get() {
    const client = new Client();
    const resp = await client.request("QuestionnaireResponse/57a1f708-d9cf-4d8c-9f25-b5a450e7f0ca");
    return this.handleResponse(resp);
  }

  async search() {
    const client = new Client();
    const resp = await client.request("QuestionnaireResponse?based-on.instantiates-canonical=PlanDefinition/97f680b9-e397-4298-8c53-de62a284c806");
    return Promise.all(resp.entry.map(entry => this.handleResponse(entry.resource)));
  }

  async handleResponse(resp) {
    if (this.isEncryptedResponse(resp)) {
      const decryptedValues = await this.decrypt(resp);

      this.addToResponse(resp, decryptedValues);
    }

    return resp;
  }

  isEncryptedResponse(resp) {
    return resp.meta.profile.some(profile => profile === 'http://openhealthhub.com/fhir/StructureDefinition/EncryptedQuestionnaireResponse');
  }

  addToResponse(resp, decryptedValues) {
    const keys = Object.keys(decryptedValues);
    resp.item.forEach(item => {
      this.addAnswersToItem(keys, item, decryptedValues);
    })
  }

  addAnswersToItem(keys, item, decryptedValues) {
    if (keys.includes(item.linkId)) {
      this.setDecryptedAnswer(item.answer, decryptedValues[item.linkId]);
    }

    if (item.item) {
      item.item.forEach(nestedItem => this.addAnswersToItem(keys, nestedItem, decryptedValues));
    }
  }

  setDecryptedAnswer(answers, decryptedValue) {
    answers.forEach((answer, i) => {
      if (answer._valueString) {
        answer.valueString = decryptedValue[i];
      }

      if (answer.valueCoding) {
        answer.valueCoding.code = decryptedValue[i];
      }

      if (answer._valueDecimal) {
        answer.valueDecimal = decryptedValue[i];
      }

      if (answer.valueAttachment) {
        answer.valueAttachment.data = decryptedValue[i];
      }

      if (answer._valueDate) {
        answer.valueDate = decryptedValue[i];
      }
    })
  }

  async decrypt(response) {
    const stringValue = response.extension[0].valueString;
    return await this.decryptValue(stringValue);
  }

  async decryptValue(value) {
    const messageObj = await openpgp.message.readArmored(value);

    const result = await openpgp.decrypt({message: messageObj, privateKeys: [this.pgpPrivKeyDecrypted]});
    return JSON.parse(result.data);
  }
}

export default QuestionnaireResponseClient;
