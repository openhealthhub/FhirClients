require 'fhir_models'
require 'fhir_client'

class FhirClient
  def initialize
    client = FHIR::Client.new('https://api-sandbox-staging.openhealthhub.com/fhir')
    FHIR::Model.client = client
    # Workaround for https://github.com/fhir-crucible/fhir_models/issues/93
    client.default_xml
  end
end
