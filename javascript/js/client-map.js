import AppointmentClient from "./appointment";
import QuestionnaireClient from "./questionnaire";
import SubscriptionClient from "./subscription";

export const CLIENT_MAP = {
  Appointment: new AppointmentClient(),
  Questionnaire: new QuestionnaireClient(),
  Subscription: new SubscriptionClient()
}
