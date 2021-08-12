import FHIR from "fhirclient";
import {FHIR_ENDPOINT} from "./constants";

class Client {

  constructor() {
    this.client = FHIR.client(FHIR_ENDPOINT);
  }

  request(request) {
    const requestObj = typeof request === 'string' ? {url: request} : request;
    const parameterSeparator = requestObj.url.indexOf('?') === -1 ? '?' : '&';
    requestObj.url = `${requestObj.url}${parameterSeparator}apikey=ad880601-b7e6-4d86-901d-b6fca96fc725`;
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
