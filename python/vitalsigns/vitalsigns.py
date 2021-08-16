import asyncio

from config.settings import client


async def get_observation():
    obs = await client.resource('Observation').execute('1', 'GET')

    print(obs.resourceType, obs.id)

    observations = await client.resources('Observation').search(identifier='identifier', device_name='devicename').fetch()

    for o in observations:
        print(o.valueQuantity.value)

asyncio.run(get_observation())
