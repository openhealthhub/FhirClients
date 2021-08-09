from fhirclient import client
import fhirclient.models.questionnaire as q

settings = {
    'app_id': 'ohh-fhir',
    'api_base': 'https://api-sandbox-staging.openhealthhub.com/fhir'
}

cli = client.FHIRClient(settings=settings)

questionnaire = q.Questionnaire.read('1', cli.server)

questionnaire.description
