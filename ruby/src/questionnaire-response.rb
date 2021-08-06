require './fhir_client'

class QuestionnaireResponseClient
  def get_questionnaire_response(id)
    FhirClient.new

    FHIR::QuestionnaireResponse.read(id)
  end
end
