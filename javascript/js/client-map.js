import AppointmentClient from "./appointment";
import QuestionnaireClient from "./questionnaire";
import SubscriptionClient from "./subscription";
import QuestionnaireResponseClient from './questionnaire-response';

export const CLIENT_MAP = {
  Appointment: new AppointmentClient(),
  Questionnaire: new QuestionnaireClient(),
  Subscription: new SubscriptionClient(),
  QuestionnaireResponse: new QuestionnaireResponseClient()
}
