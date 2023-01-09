import asyncio

from config.settings import client
from fhirpy.lib import AsyncFHIRResource
from fhirpy.base.utils import get_by_path


async def get_observation():
    observation: AsyncFHIRResource = (
        await client.resources("Observation").search(_id="1").get()
    )

    print(observation.resource_type, observation.id)

    observations = (
        await client.resources("Observation")
        .search(identifier="identifier", device_name="devicename")
        .fetch()
    )

    for o in observations:
        print(o.get_by_path(["valueQuantity", "value"]))


asyncio.run(get_observation())
