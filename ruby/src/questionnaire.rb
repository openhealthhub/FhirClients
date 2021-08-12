require './fhir_client'

class QuestionnaireClient
  def get_questionnaire(id)
    FhirClient.new

    FHIR::Questionnaire.read(id)
  end
end
