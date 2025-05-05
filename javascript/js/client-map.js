import QuestionnaireClient from "./questionnaire";
import SubscriptionClient from "./subscription";
import QuestionnaireResponseClient from './questionnaire-response';
import CarePlanClient from "./careplan";
import CareTeamClient from "./careteam";
import PlanDefinitionClient from "./plan-definition";
import BinaryClient from "./binary";
import PractitionerClient from "./practitioner";

export const CLIENT_MAP = {
  Binary: new BinaryClient(),
  PlanDefinition: new PlanDefinitionClient(),
  Questionnaire: new QuestionnaireClient(),
  QuestionnaireResponse: new QuestionnaireResponseClient(),
  CarePlan: new CarePlanClient(),
  CareTeam: new CareTeamClient(),
  Practitioner: new PractitionerClient(),
  Subscription: new SubscriptionClient()
}
