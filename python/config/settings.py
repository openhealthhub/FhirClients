from fhirpy import AsyncFHIRClient

client = AsyncFHIRClient(
    'https://api-sandbox-staging.openhealthhub.com/OpenHealthhub/fhir/4',
    authorization=None,
    extra_headers={'x-api-key': 'ad880601-b7e6-4d86-901d-b6fca96fc725'}
)
