import { PractitionerKey } from '../models/practitioner-key';
export declare class EncryptService {
    private key?;
    private practitionerKey?;
    loadKey(key: PractitionerKey): Promise<void>;
    encrypt(value: string, key: PractitionerKey): Promise<string>;
}
