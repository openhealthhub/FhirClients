from fhirpy import AsyncFHIRClient

client = AsyncFHIRClient(
    'https://api-sandbox-staging.openhealthhub.com/OpenHealthhub/fhir/4',
    authorization='None', #leaving this defaulted at None breaks, setting it to an irrelevant string stops the client from breaking
    extra_headers={'x-api-key': '90da723b-70f7-400b-8ddb-8574c45bff13'}
)
