import Client from "./client";
import {API_KEY, FHIR_ENDPOINT} from "./constants";
import publicKey from '../../sandbox.pub';

class BinaryClient {
  async create() {
    const client = new Client();
    const token = await client.getToken();
    const request = {
      method: 'POST',
      headers: {
        'Content-Type': 'text/plain',
        'Authorization': `Bearer ${token}`,
        'X-API-KEY': API_KEY
      },
      body: btoa(publicKey)
    };
    const promise = fetch(FHIR_ENDPOINT + '/Binary', request);
    return promise.then(resp => {
      return {responseStatus: resp.status, request};
    });
  }
}

export default BinaryClient;
