import AppointmentClient from "./appointment";
import QuestionnaireClient from "./questionnaire";

export const CLIENT_MAP = {
  Appointment: new AppointmentClient(),
  Questionnaire: new QuestionnaireClient()
}
