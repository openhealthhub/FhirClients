require 'fhir_models'
require 'fhir_client'

class FhirClient
  KEYCLOAK_OIDC_BASE_URL = 'https://auth-sandbox-staging.openhealthhub.com/auth/realms/OpenHealthHub/protocol/openid-connect'
  CLIENT_SECRET = '915e87d4-16ee-4ca5-b701-b38b6afce8ff'
  CLIENT_ID = 'api-sandbox'

  API_KEY = '90da723b-70f7-400b-8ddb-8574c45bff13'

  FHIR_ENDPOINT = 'https://api-sandbox-staging.openhealthhub.com/OpenHealthhub/fhir/4'

  def initialize
    client = FHIR::Client.new(FHIR_ENDPOINT)
    client.additional_headers = { 'x-api-key': API_KEY }
    client.set_oauth2_auth(CLIENT_ID, CLIENT_SECRET, KEYCLOAK_OIDC_BASE_URL + '/auth', KEYCLOAK_OIDC_BASE_URL + '/token')
    FHIR::Model.client = client
    # Workaround for https://github.com/fhir-crucible/fhir_models/issues/93
    client.default_xml
  end
end
