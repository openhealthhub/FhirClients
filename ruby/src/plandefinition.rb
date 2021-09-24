require './fhir_client'

class PlanDefinitionClient
  def get_plandefinition(id)
    FhirClient.new

    FHIR::PlanDefinition.read(id)
  end

  def search_plandefinition
    FhirClient.new

    params = {
      'definition': 'Questionnaire/866683f3-c41b-47c0-b42f-86f9ff978d1d',
      'publisher': 'Program Creator'
    }
    FHIR::PlanDefinition.search(params)
  end
end
