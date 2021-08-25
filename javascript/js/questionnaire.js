import Client from "./client";

class QuestionnaireClient {
   async get() {
    const client = new Client();
    return client.request("Questionnaire/1");
  }
}

export default QuestionnaireClient;
