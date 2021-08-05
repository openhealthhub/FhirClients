import FHIR from "fhirclient";
import {FHIR_ENDPOINT} from "./constants";
import * as openpgp from 'openpgp/dist/compat/openpgp';
import privateKey from '../../sandbox.key';

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
    const client = FHIR.client(FHIR_ENDPOINT);
    const resp = await client.request("QuestionnaireResponse/1");
    const decryptedValues = await this.decrypt(resp);

    this.addToResponse(resp, decryptedValues);

    return resp;
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
    const messageObj = await openpgp.message.readArmored(response.extension[0].valueString);

    const result = await openpgp.decrypt({message: messageObj, privateKeys: [this.pgpPrivKeyDecrypted]});
    return JSON.parse(result.data);
  }
}

export default QuestionnaireResponseClient;
