require './fhir_client'

class CarePlanClient
  def get_careplan(id)
    FhirClient.new

    FHIR::CarePlan.read(id)
  end

  def search_careplan
    FhirClient.new

    params = {
      'instantiates-canonical': 'PlanDefinition/97f680b9-e397-4298-8c53-de62a284c806',
      'patient.identifier': '1234'
    }
    FHIR::CarePlan.search(params)
  end

  def get_careplan_with_practitioners(id)
    FhirClient.new

    params = {
      '_id': id,
      '_include': %w[CareTeam:participant CarePlan:care-team]
    }
    FHIR::CarePlan.search(params)
  end

  def create_careplan
    FhirClient.new

    careplan = create_careplan_resource

    FHIR::CarePlan.create(careplan)
  end

  def update_careplan
    FhirClient.new

    careplan = create_careplan_resource
    careplan.id = 1

    FHIR::CarePlan.partial_update(careplan.id, careplan)
  end

  def delete_careplan(id)
    FhirClient.new

    FHIR::CarePlan.destroy(id)
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
