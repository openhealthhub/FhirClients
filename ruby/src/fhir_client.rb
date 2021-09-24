require 'fhir_models'
require 'fhir_client'

class FhirClient
  KEYCLOAK_OIDC_BASE_URL = 'https://auth-staging.openhealthhub.com/auth/realms/OpenHealthHub/protocol/openid-connect'.freeze
  CLIENT_SECRET = '915e87d4-16ee-4ca5-b701-b38b6afce8ff'.freeze
  CLIENT_ID = 'api-sandbox'.freeze

  API_KEY = 'ad880601-b7e6-4d86-901d-b6fca96fc725'.freeze

  FHIR_ENDPOINT = 'https://api-sandbox-staging.openhealthhub.com/OpenHealthhub/fhir-sandbox/4'.freeze

  def initialize
    client = FHIR::Client.new(FHIR_ENDPOINT)
    client.additional_headers = { 'x-api-key': API_KEY }
    client.set_oauth2_auth(CLIENT_ID, CLIENT_SECRET, KEYCLOAK_OIDC_BASE_URL + '/auth', KEYCLOAK_OIDC_BASE_URL + '/token')
    FHIR::Model.client = client
    # Workaround for https://github.com/fhir-crucible/fhir_models/issues/93
    client.default_xml
  end
end
