import { Coding, QuestionnaireResponseItemAnswer } from 'fhir/r4';
export declare enum SupportedAnswerType {
    STRING = "STRING",
    DECIMAL = "DECIMAL",
    DATE = "DATE",
    ATTACHMENT = "ATTACHMENT",
    CODING = "CODING"
}
declare type SupportedAnswerValue = string | number | Coding | undefined;
export declare const getValueForType: (answer: QuestionnaireResponseItemAnswer, typeString: keyof typeof SupportedAnswerType) => SupportedAnswerValue;
export declare const getAnswerType: (answer: QuestionnaireResponseItemAnswer) => keyof typeof SupportedAnswerType;
export {};
