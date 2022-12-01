import { QuestionnaireResponse } from 'fhir/r4';
import { PractitionerKey } from '../models/practitioner-key';
import { EncryptService } from './encrypt.service';
export declare class QuestionnaireResponseService {
    private readonly encryptService;
    private readonly key;
    constructor(encryptService: EncryptService, key: PractitionerKey);
    createEncryptedResponse(originalResponse: QuestionnaireResponse): Promise<QuestionnaireResponse>;
    private encryptAnswers;
    private encryptItem;
    private encryptAnswer;
    private setEncryptedValue;
    private createValueWithEncryptExtension;
    private encryptAllAnswers;
    private encryptValue;
    private setPractitioner;
}
