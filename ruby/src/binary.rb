class BinaryClient

  def upload_key
    token = get_token
    encoded_key = get_base64_encoded_key
    uri = URI(FhirClient::FHIR_ENDPOINT + '/Binary')
    Net::HTTP.post(uri, encoded_key, { 'Content-Type' => 'text/plain',
                                       'X-API-KEY' => FhirClient::API_KEY,
                                       'Authorization' => 'Bearer ' + token })
  end

  def get_base64_encoded_key
    file = open('../../sandbox.pub')
    key = file.read
    Base64.strict_encode64(key)
  end

  def get_token
    uri = URI(FhirClient::KEYCLOAK_OIDC_BASE_URL + '/token')
    params = { 'client_id' => FhirClient::CLIENT_ID,
               'client_secret' => FhirClient::CLIENT_SECRET,
               'grant_type' => 'client_credentials' }
    res = Net::HTTP.post_form(uri, params)
    JSON.parse(res.body)['access_token']
  end
end
