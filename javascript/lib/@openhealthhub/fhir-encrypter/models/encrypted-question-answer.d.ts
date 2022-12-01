import { Coding } from 'fhir/r4';
export interface EncryptedQuestionAnswer {
    value?: string | number;
    codes: Coding[];
}
