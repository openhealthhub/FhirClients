require '../src/careplan'
require '../src/careteam'
require '../src/practitioner'
require '../src/questionnaire'
require '../src/questionnaire-response'
require '../src/subscription'
require '../src/binary'

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

client = SubscriptionClient.new
response = client.create_subscription
puts response.to_json

client = CarePlanClient.new
response = client.create_careplan
puts response.to_json

client = CarePlanClient.new
response = client.get_careplan_with_practitioners(4)
puts response.to_json

client = CareTeamClient.new
response = client.get_careteam_with_practitioners(4)
puts response.to_json

client = PractitionerClient.new
response = client.search_practitioners_by_careteam_id(4)
puts response.to_json

client = BinaryClient.new
response = client.upload_key
puts response.code
