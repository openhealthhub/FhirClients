require 'fhir_models'
require 'fhir_client'

class FhirClient
  def initialize
    client = FHIR::Client.new('https://api-sandbox-staging.openhealthhub.com/fhir')
    FHIR::Model.client = client
  end
end
