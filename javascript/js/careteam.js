import Client from "./client";

class CareTeamClient {
  async get() {
    const client = new Client();
    return client.request('CareTeam/1');
  }

  async getWithPractitioners() {
    const client = new Client();
    return client.request('CareTeam/?_id=1&_include=CareTeam:participant');
  }
}

export default CareTeamClient;
