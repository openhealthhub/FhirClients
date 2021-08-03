import FHIR from "fhirclient";
import {FHIR_ENDPOINT} from "./constants";

class AppointmentClient {
   async get() {
    const client = FHIR.client(FHIR_ENDPOINT);
    return await client.request("Appointment/1");
  }
}

export default AppointmentClient;
