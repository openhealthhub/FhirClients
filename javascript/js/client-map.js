import QuestionnaireClient from "./questionnaire";
import SubscriptionClient from "./subscription";
import QuestionnaireResponseClient from './questionnaire-response';
import VitalSignsClient from "./vitalsigns";
import CarePlanClient from "./careplan";
import PlanDefinitionClient from "./plan-definition";
import BinaryClient from "./binary";

export const CLIENT_MAP = {
  Binary: new BinaryClient(),
  PlanDefinition: new PlanDefinitionClient(),
  Questionnaire: new QuestionnaireClient(),
  QuestionnaireResponse: new QuestionnaireResponseClient(),
  VitalSigns: new VitalSignsClient(),
  CarePlan: new CarePlanClient(),
  Subscription: new SubscriptionClient()
}
