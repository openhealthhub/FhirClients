require './appointment'
require './questionnaire'
require './questionnaire-response'
require './vitalsigns'
require './subscription'

client = AppointmentClient.new
response = client.get_appointment(4)
puts response.to_json

response = client.create_appointment
puts response.to_json

client = QuestionnaireClient.new
response = client.get_questionnaire(4)
puts response.to_json

client = QuestionnaireResponseClient.new
response = client.get_questionnaire_response(4)
# TODO OP-651 to_json not emitting all encrypted value see if this is just a to_json issue or an actual implementation issue
puts response.to_json

response = client.search_questionnaire_response
puts response.to_json

client = VitalSignsClient.new
response = client.get_vitalsigns(4)
puts response.to_json
response = client.search_vitalsigns
puts response.to_json

client = SubscriptionClient.new
response = client.create_subscription
puts response.to_json
