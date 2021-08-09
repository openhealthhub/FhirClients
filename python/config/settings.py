from fhirclient import client

settings = {
    'app_id': 'ohh-fhir',
    'api_base': 'https://api-sandbox-staging.openhealthhub.com/fhir'
}

cli = client.FHIRClient(settings=settings)
server = cli.server
