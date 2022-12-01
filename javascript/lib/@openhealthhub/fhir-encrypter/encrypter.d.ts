import { Bundle, Practitioner, QuestionnaireResponse } from 'fhir/r4';
export declare function encryptQuestionnaireResponse(response: Readonly<QuestionnaireResponse>, practitioners?: Practitioner[]): Promise<Bundle>;
