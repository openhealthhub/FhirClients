from config.settings import server
import fhirclient.models.questionnaireresponse as qr

response = qr.QuestionnaireResponse.read('1', server)

print(response.text)

qrs = qr.QuestionnaireResponse.where({'part-of': 'programUUID', 'identifier': 'patientnumber'}).perform_resources(server)

for q in qrs:
    print(q.as_json())
