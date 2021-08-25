import Client from "./client";

class VitalSignsClient {
   async get() {
    const client = new Client();
    return client.request("Observation/1");
  }

  async search() {
    const client = new Client();
    return client.request("Observation?identifier=1234&device-name=testDevice");
  }
}

export default VitalSignsClient;
