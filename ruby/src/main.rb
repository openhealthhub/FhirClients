require '../src/appointment'
require '../src/questionnaire'
require '../src/questionnaire-response'
require '../src/vitalsigns'
require '../src/subscription'

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
puts response.to_json

response = client.search_questionnaire_response
puts response.to_json

client = PlanDefinitionClient.new
response = client.get_plandefinition('4944e73f-e447-49ba-a64c-a246b9ef4bdd')
puts response.to_json
response = client.search_plandefinition
puts response.to_json

client = VitalSignsClient.new
response = client.get_vitalsigns(4)
puts response.to_json
response = client.search_vitalsigns
puts response.to_json

client = SubscriptionClient.new
response = client.create_subscription
puts response.to_json
