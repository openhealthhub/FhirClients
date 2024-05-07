require './fhir_client'

class BatchClient
  def batch_request
    FhirClient.new

    careplan = create_careplan_resource
    careplan.id = 1

    batch_request = FHIR::Transaction.new

    create_request = FHIR::Bundle::Entry::Request.new
    create_request.url = 'CarePlan'
    create_request.request_method = 'POST'
    create_request.entry = careplan

    get_request = FHIR::Bundle::Entry::Request.new
    get_request.url = 'CarePlan/123'
    get_request.request_method = 'GET'

    batch_request.entry = [create_request, get_request]

    batch_response = FHIR::Client.post(batch_request)
    batch_response
  end

  private

  def create_careplan_resource
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
    careplan
  end
end
