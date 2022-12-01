import { Coding, QuestionnaireResponseItemAnswer } from 'fhir/r4';
export declare enum SupportedAnswerType {
    STRING = "STRING",
    DECIMAL = "DECIMAL",
    DATE = "DATE",
    ATTACHMENT = "ATTACHMENT",
    CODING = "CODING"
}
export declare const getValueForType: (answer: QuestionnaireResponseItemAnswer, typeString: keyof typeof SupportedAnswerType) => string | number | Coding | undefined;
export declare const getAnswerType: (answer: QuestionnaireResponseItemAnswer) => keyof typeof SupportedAnswerType;
