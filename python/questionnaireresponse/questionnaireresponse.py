from config.settings import client

response = client.resources('QuestionnaireResponse').search(_id='1')

print(response.text)

qrs = client.resources('QuestionnaireResponse').search(part_of='programUUID', identifier='patientnumber')

for q in qrs:
    print(q.as_json())
