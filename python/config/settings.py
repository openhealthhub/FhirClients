import requests
from fhirpy import AsyncFHIRClient

FHIR_URL = 'https://api.openhealthhub.com/OpenHealthhub/fhir-sandbox/4'
API_KEY = 'ad880601-b7e6-4d86-901d-b6fca96fc725'


def get_token():
    client_id = 'api-sandbox'
    client_secret = '95810e52-4307-41f5-99a4-d873ab63b536'
    url = 'https://auth.openhealthhub.com/auth/realms/OpenHealthHub/protocol/openid-connect/token'
    response = requests.post(url,
                             f'client_id={client_id}&client_secret={client_secret}&grant_type=client_credentials',
                             headers={'Content-Type': 'application/x-www-form-urlencoded'}).json()
    return response['access_token']


client = AsyncFHIRClient(
    FHIR_URL,
    authorization=f'Bearer {get_token()}',
    extra_headers={'x-api-key': API_KEY}
)
