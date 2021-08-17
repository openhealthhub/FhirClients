from fhirpy import AsyncFHIRClient

client = AsyncFHIRClient(
    'https://api-sandbox-staging.openhealthhub.com/OpenHealthhub/fhir-sandbox/4',
    authorization='None', #leaving this defaulted at None breaks, setting it to an irrelevant string stops the client from breaking
    extra_headers={'x-api-key': 'ad880601-b7e6-4d86-901d-b6fca96fc725'}
)
