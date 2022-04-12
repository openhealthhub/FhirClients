import FHIR from "fhirclient";
import {API_KEY, CLIENT_ID, CLIENT_SECRET, FHIR_ENDPOINT, KEYCLOAK_TOKEN_URI} from "./constants";

class Client {

  constructor() {
    this.client = FHIR.client(FHIR_ENDPOINT);
    this.tokenPromise = this.getToken().then(resp => this.token = resp);
  }

  // WARNING: Using client credentials from a browser is not safe!
  async getToken() {
    const headers = new Headers();
    headers.append('Content-Type', 'application/x-www-form-urlencoded');
    headers.append('Accept', 'application/json');

    const urlencoded = new URLSearchParams();
    urlencoded.append('client_id', CLIENT_ID);
    urlencoded.append('client_secret', CLIENT_SECRET);
    urlencoded.append('grant_type', 'client_credentials');

    const requestOptions = {
      method: 'POST',
      headers: headers,
      body: urlencoded,
      redirect: 'follow'
    };

    return fetch(KEYCLOAK_TOKEN_URI, requestOptions)
      .then(response => response.json())
      .then(result => result.access_token)
      .catch(error => console.log('error', error));
  }

  async request(request) {
    await this.tokenPromise;
    const requestObj = typeof request === 'string' ? {url: request} : request;
    requestObj.headers = {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${this.token}`,
      'x-api-key': API_KEY
    };
    return this.client.request(requestObj);
  }

  create(resource) {
    return this.request({
      url: resource.resourceType,
      method: 'POST',
      body: JSON.stringify(resource),
    });
  }

  update(resource) {
    return this.request({
      url: `${resource.resourceType}/${resource.id}`,
      method: 'PUT',
      body: JSON.stringify(resource),
    });
  }

}

export default Client;
