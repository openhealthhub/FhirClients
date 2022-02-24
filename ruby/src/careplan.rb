require './fhir_client'

class CarePlanClient
  def get_careplan(id)
    FhirClient.new

    FHIR::CarePlan.read(id)
  end

  def create_careplan
    FhirClient.new

    careplan = FHIR::CarePlan.new
    careplan.period = FHIR::Period.new
    careplan.period.start = Date.new(2021, 7, 9)

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
    careplan.contained = [patient]

    careplan.instantiatesCanonical = 'PlanDefinition/cca2eaf3-03a9-46c0-88c6-e0287917cea6'

    patient_reference = FHIR::Reference.new
    patient_reference.reference = '#patient'
    careplan.subject = patient_reference

    FHIR::CarePlan.create(careplan)
  end
end
