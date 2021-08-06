require './appointment'
require './questionnaire'
require './questionnaire-response'
require './vitalsigns'

client = AppointmentClient.new
response = client.get_appointment(4)
puts response.to_json

client = QuestionnaireClient.new
response = client.get_questionnaire(4)
puts response.to_json

client = QuestionnaireResponseClient.new
response = client.get_questionnaire_response(4)
# TODO OP-651 to_json not emitting all encrypted value see if this is just a to_json issue or an actual implementation issue
puts response.to_json

client = VitalSignsClient.new
response = client.get_vitalsigns(4)
puts response.to_json
