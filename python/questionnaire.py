import requests

response = requests.get('https://api-sandbox-staging.openhealthhub.com/fhir/Questionnaire/1')

print(response.json())
