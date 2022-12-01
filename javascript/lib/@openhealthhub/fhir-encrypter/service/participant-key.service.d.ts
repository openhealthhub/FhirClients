import { Practitioner } from 'fhir/r4';
import { PractitionerKey } from '../models/practitioner-key';
export declare const PARTICIPANT_PUBLIC_KEY_UUID_SYSTEM = "urn:ietf:rfc:3986";
export declare const PARTICIPANT_PUBLIC_KEY_URL: string;
export declare class ParticipantKeyService {
    toParticipantKey(practitioner: Practitioner): PractitionerKey;
}
