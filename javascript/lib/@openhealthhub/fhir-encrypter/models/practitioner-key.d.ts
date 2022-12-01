import { Practitioner } from 'fhir/r4';
export interface PractitionerKey {
    userUuid: string;
    keyUuid: string;
    publicKey: string;
    practitioner: Practitioner;
}
