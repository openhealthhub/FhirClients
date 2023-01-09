import asyncio
import sys

from fhirpy.lib import AsyncFHIRResource

sys.path.append("../")
from config.settings import client


async def get_practitioner():
    practitioner: AsyncFHIRResource = (
        await client.resources("Practitioner").search(_id=1).get()
    )

    # or

    practitioner: AsyncFHIRResource = await client.reference(
        "Practitioner", 1
    ).to_resource()

    print(practitioner)

    search_by_careteam = (
        await client.resources("Practitioner").has("CareTeam", _id=1).fetch()
    )

    print(search_by_careteam)


asyncio.run(get_practitioner())
