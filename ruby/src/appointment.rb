require './fhir_client'

class AppointmentClient
  def get_appointment(id)
    FhirClient.new

    FHIR::Appointment.read(id)
  end

  def create_appointment
    FhirClient.new

    appointment = FHIR::Appointment.new
    appointment.status = 'booked'
    appointment.priority = 5
    appointment.start = Date.new(2021, 7, 9)
    appointment.end = Date.new(2021, 7, 12)
    appointment.comment = 'Further expand on the results of the MRI and determine the next actions that may be appropriate.'
    appointment.description = 'Discussion on the results of your recent MRI'

    appointment_type = FHIR::CodeableConcept.new
    appointment_type_coding = FHIR::Coding.new
    appointment_type_coding.system = 'http://terminology.hl7.org/CodeSystem/v2-0276'
    appointment_type_coding.code = 'FOLLOWUP'
    appointment_type_coding.id = 'A follow up visit from a previous appointment'
    appointment_type.coding = appointment_type_coding
    appointment.appointmentType = appointment_type

    FHIR::Appointment.create(appointment)
  end
end
