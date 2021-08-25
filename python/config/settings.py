import requests
from fhirpy import AsyncFHIRClient


def get_token():
    client_id = 'api-sandbox'
    client_secret = '915e87d4-16ee-4ca5-b701-b38b6afce8ff'
    url = 'https://auth-staging.openhealthhub.com/auth/realms/OpenHealthHub/protocol/openid-connect/token'
    response = requests.post(url,
                  f'client_id={client_id}&client_secret={client_secret}&grant_type=client_credentials',
                  headers={'Content-Type': 'application/x-www-form-urlencoded'}).json()
    return response['access_token']


client = AsyncFHIRClient(
    'https://api-sandbox-staging.openhealthhub.com/OpenHealthhub/fhir-sandbox/4',
    authorization=f'Bearer {get_token()}',
    extra_headers={'x-api-key': 'ad880601-b7e6-4d86-901d-b6fca96fc725'}
)
