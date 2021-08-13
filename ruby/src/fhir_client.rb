require 'fhir_models'
require 'fhir_client'

class FhirClient
  def initialize
    client = FHIR::Client.new('https://api-sandbox-staging.openhealthhub.com/OpenHealthhub/fhir/4')
    client.additional_headers = {'x-api-key': 'ad880601-b7e6-4d86-901d-b6fca96fc725'}
    FHIR::Model.client = client
    # Workaround for https://github.com/fhir-crucible/fhir_models/issues/93
    client.default_xml
  end
end
