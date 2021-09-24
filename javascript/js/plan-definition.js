import Client from "./client";

class PlanDefinitionClient {

  async get() {
    const client = new Client();
    return client.request("PlanDefinition/4944e73f-e447-49ba-a64c-a246b9ef4bdd");
  }

  async search() {
    const client = new Client();
    return client.request("PlanDefinition?definition=Questionnaire/866683f3-c41b-47c0-b42f-86f9ff978d1d&publisher=Program Creator");
  }
}

export default PlanDefinitionClient;
