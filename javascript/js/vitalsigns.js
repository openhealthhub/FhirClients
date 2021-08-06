import FHIR from "fhirclient";
import {FHIR_ENDPOINT} from "./constants";

class VitalSignsClient {
   async get() {
    const client = FHIR.client(FHIR_ENDPOINT);
    return client.request("Observation/1");
  }

  async search() {
    const client = FHIR.client(FHIR_ENDPOINT);
    return client.request("Observation?identifier=1234&device-name=testDevice");
  }
}

export default VitalSignsClient;
