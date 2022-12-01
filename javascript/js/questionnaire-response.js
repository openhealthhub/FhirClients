import Client from "./client";
import {QuestionnaireResponseDecryptClient} from "./questionnaire-response-decrypt";
import {QuestionnaireResponseEncryptClient} from "./questionnaire-response-encrypt";

class QuestionnaireResponseClient {

  constructor() {
    this.decryptClient = new QuestionnaireResponseDecryptClient();
    this.encryptClient = new QuestionnaireResponseEncryptClient();
  }

  async get() {
    const client = new Client();
    const resp = await client.request("QuestionnaireResponse/57a1f708-d9cf-4d8c-9f25-b5a450e7f0ca");
    return this.decryptClient.handleResponse(resp);
  }

  async search() {
    const client = new Client();
    const resp = await client.request("QuestionnaireResponse?based-on.instantiates-canonical=PlanDefinition/97f680b9-e397-4298-8c53-de62a284c806");
    return Promise.all(resp.entry.map(entry => this.decryptClient.handleResponse(entry.resource)));
  }

  async create() {
    const client = new Client();
    const bundle = await this.encryptClient.encrypt();
    return client.create(bundle);
  }
}

export default QuestionnaireResponseClient;
