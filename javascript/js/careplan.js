import Client from "./client";

const CARE_PLAN_RESOURCE = {
  resourceType: "CarePlan",
  contained: [
    {
      resourceType: "Patient",
      id: "patient",
      identifier: [
        {
          system: "urn:oid:2.16.840.1.113883.2.4.99",
          value: "1234"
        },
        {
          system: "urn:ietf:rfc:3986",
          value: "urn:uuid:57698ca0-7c63-4367-90c6-2007dcd5761a"
        }
      ],
      name: [
        {
          text: "Test Patient"
        }
      ],
      telecom: [
        {
          system: "email",
          value: "test@patient.ohh"
        }
      ]
    }
  ],
  extension: [
    {
      url: "http://openhealthhub.com/fhir/StructureDefinition/plan-pin",
      valueString: "59gladtc"
    }
  ],
  instantiatesCanonical: [
    "https://api.openhealthhub.com/OpenHealthhub/fhir/4/PlanDefinition/cca2eaf3-03a9-46c0-88c6-e0287917cea6"
  ],
  status: "completed",
  intent: "plan",
  subject: {
    reference: "#patient"
  },
  period: {
    start: "2021-03-16T13:32:37.430+01:00",
    end: "2021-03-16T13:32:37.430+01:00"
  }
};

class CarePlanClient {
  async get() {
    const client = new Client();
    return client.request('CarePlan/1');
  }

  async search() {
    const client = new Client();
    return client.request('CarePlan/?instantiates-canonical=PlanDefinition/97f680b9-e397-4298-8c53-de62a284c806&patient.identifier=1234');
  }

  async getWithPractitioners() {
    const client = new Client();
    return client.request('CarePlan/?_id=1&_include=CarePlan:care-team&_include=CareTeam:participant');
  }

  async create() {
    const client = new Client();
    return client.create(CARE_PLAN_RESOURCE);
  }

  async update() {
    const client = new Client();
    return client.update({...CARE_PLAN_RESOURCE, id: 1});
  }

  async delete() {
    const client = new Client();
    client.delete('CarePlan', 1);
  }
}

export default CarePlanClient;
