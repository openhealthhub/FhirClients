import Client from "./client";

class SubscriptionClient {
  async create() {
    const client = new Client();
    return client.create({
      resourceType: 'Subscription',
      criteria: 'Appointment?name=test',
      status: 'requested',
      channel: {
        type: 'rest-hook',
        endpoint: 'https://your-webhook/endpoint',
        header: ['Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8']
      }
    });
  }
}

export default SubscriptionClient;
