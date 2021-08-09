require './fhir_client'

class QuestionnaireResponseClient
  def get_questionnaire_response(id)
    FhirClient.new

    FHIR::QuestionnaireResponse.read(id)
  end

  def search_questionnaire_response
    FhirClient.new

    params = {
      'part-of': '97f680b9-e397-4298-8c53-de62a284c806',
      'identifier': '6226217e-7ae9-4fa2-8fbe-e83a8f8540f9'
    }
    FHIR::QuestionnaireResponse.search(params)
  end
end
