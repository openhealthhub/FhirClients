import Client from "./client";

class SubscriptionClient {
  async create() {
    const client = new Client();
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
