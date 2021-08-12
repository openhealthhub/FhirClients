from fhirclient import client
from fhirclient.server import FHIRServer

cli = client.FHIRClient(settings={
    'app_id': 'ohh-fhir',
    'api_base': 'http://localhost:8080/fhir' #'https://api-sandbox-staging.openhealthhub.com/OpenHealthhub/fhir/4'
})
server: FHIRServer = cli.server
