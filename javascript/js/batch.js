import Client from "./client";

const BATCH_BUNDLE = {
  resourceType: "Bundle",
  entry:
    [{
      request: {
        method: 'GET',
        url: 'CarePlan/123'
      }
    }, {
      request: {
        method: 'POST',
        url: 'CarePlan'
      },
      resource: {
        type: 'CarePlan',
        ...content
      }
    }]
};

class CarePlanClient {
  async create() {
    const client = new Client();
    return client.create(BATCH_BUNDLE);
  }
}

export default CarePlanClient;
