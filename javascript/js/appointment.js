import FHIR from 'fhirclient';
import {FHIR_ENDPOINT} from './constants';

class AppointmentClient {
  async get() {
    const client = FHIR.client(FHIR_ENDPOINT);
    return client.request('Appointment/1');
  }

  async create() {
    const client = FHIR.client(FHIR_ENDPOINT);
    return client.create({
      resourceType: 'Appointment',
      status: 'booked',
      priority: 5,
      description: 'Discussion on the results of your recent MRI',
      start: '2021-08-05T15:20:35.348+02:00',
      end: '2021-08-05T15:20:35.348+02:00',
      created: '1970-01-01T01:00:00+01:00',
      comment: 'Further expand on the results of the MRI and determine the next actions that may be appropriate.',
      participant: [
        {
          actor: {
            reference: 'Patient/example'
          },
          required: 'required',
          status: 'accepted'
        },
        {
          actor: {
            reference: 'Practitioner/example',
            display: 'Dr Adam Careful'
          }
        }
      ]
    });
  }
}

export default AppointmentClient;
