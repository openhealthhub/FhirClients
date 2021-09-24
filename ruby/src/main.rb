require '../src/appointment'
require '../src/careplan'
require '../src/questionnaire'
require '../src/questionnaire-response'
require '../src/vitalsigns'
require '../src/subscription'
require '../src/binary'

response = client.search_questionnaire_response
puts response.to_json

client = BinaryClient.new
response = client.upload_key
puts response.code

