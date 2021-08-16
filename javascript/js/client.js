import FHIR from "fhirclient";
import {FHIR_ENDPOINT} from "./constants";

class Client {

  constructor() {
    this.client = FHIR.client(FHIR_ENDPOINT);
  }

  request(request) {
    const requestObj = typeof request === 'string' ? {url: request} : request;
    const parameterSeparator = requestObj.url.indexOf('?') === -1 ? '?' : '&';
    requestObj.url = `${requestObj.url}${parameterSeparator}apikey=${API_KEY}`;
    return this.client.request(requestObj);
  }

  create(resource) {
    return this.request({
      url: resource.resourceType,
      method: 'POST',
      body: JSON.stringify(resource),
      headers: {
        'Content-Type': 'application/json'
      }
    });
  }

}

export default Client;
