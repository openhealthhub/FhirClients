import asyncio
import base64

import requests

from config.settings import FHIR_URL, get_token, API_KEY


async def create_key():
    encoded_key = await get_base64_encoded_key()
    token = get_token()
    resp = requests.post(url=FHIR_URL + '/Binary',
                         data=encoded_key,
                         headers={'Content-Type': 'text/plain', 'Authorization': f'Bearer {token}', 'X-API-KEY': API_KEY})
    return resp


async def get_base64_encoded_key():
    with open('../../sandbox.pub') as file:
        key = file.read()
    key_bytes = key.encode('ascii')
    base64_bytes = base64.b64encode(key_bytes)
    return base64_bytes.decode('ascii')


asyncio.run(get_observation())
