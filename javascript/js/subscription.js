import FHIR from "fhirclient";
import {FHIR_ENDPOINT} from "./constants";

class SubscriptionClient {
  async create() {
    const client = FHIR.client(FHIR_ENDPOINT);
    return client.create({
        resourceType: 'Subscription',
        criteria: 'Appointment?name=test',
        channel: {
          type: 'rest-hook',
          endpoint: 'https://your-webhook/endpoint'
        }
    });
  }
}

export default SubscriptionClient;
