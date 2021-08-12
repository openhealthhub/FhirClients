from fhirclient import client

cli = client.FHIRClient(settings={
    'app_id': 'ohh-fhir',
    'api_base': 'https://api-sandbox-staging.openhealthhub.com/OpenHealthhub/fhir/4'
})
server = cli.server
