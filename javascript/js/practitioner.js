import Client from "./client";

class PractitionerClient {
  async get() {
    const client = new Client();
    return client.request('Practitioner/1');
  }

  async searchByCareTeam() {
    const client = new Client();
    return client.request('Practitioner/?_has:CareTeam:_id=1');
  }
}

export default PractitionerClient;
