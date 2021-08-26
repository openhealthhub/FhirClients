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
    appointment.start = Date.new(2021, 7, 9)

    patient = FHIR::Patient.new
    patient.id = 'patient'
    patient_identifier = FHIR::Identifier.new
    patient_identifier.system = 'urn:oid:2.16.840.1.113883.2.4.99'
    patient_identifier.value = '1234'
    patient.identifier = [patient_identifier]
    patient.name = 'Test Patient'
    patient_email = FHIR::ContactPoint.new
    patient_email.system = 'email'
    patient_email.value = 'test@patient.ohh'
    patient.telecom = [patient_email]
    appointment.contained = [patient]

    pin_extension = FHIR::Extension.new
    pin_extension.url = 'http://openhealthhub.com/fhir/StructureDefinition/appointment-pin'
    pin_extension.valueString = '59gladtc'
    appointment.extension = [pin_extension]

    plan_definition_reference = FHIR::Reference.new
    plan_definition_reference.reference = 'PlanDefinition/cca2eaf3-03a9-46c0-88c6-e0287917cea6'
    appointment.supportingInformation = [plan_definition_reference]

    patient_participant = FHIR::Appointment::Participant.new
    patient_participant.status = 'needs-action'
    patient_reference = FHIR::Reference.new
    patient_reference.reference = '#patient'
    patient_participant.actor = patient_reference
    appointment.participant = [patient_reference]

    FHIR::Appointment.create(appointment)
  end
end
