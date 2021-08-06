import FHIR from "fhirclient";
import {FHIR_ENDPOINT} from "./constants";

class QuestionnaireClient {
   async get() {
    const client = FHIR.client(FHIR_ENDPOINT);
    return client.request("Questionnaire/1");
  }
}

export default QuestionnaireClient;
