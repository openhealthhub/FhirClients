import asyncio
import sys

sys.path.append("../")
from config.settings import client

from fhirpy.lib import AsyncFHIRResource


async def get_careteam():
    care_team: AsyncFHIRResource = (
        await client.resources("CareTeam").search(_id=1).get()
    )

    # or

    care_team: AsyncFHIRResource = await client.reference("CareTeam", 1).to_resource()
    print(care_team)

    get_with_practitioners = (
        await client.resources("CareTeam")
        .search(_id=1)
        .include("CareTeam", "participant")
        .fetch_raw()
    )

    print(get_with_practitioners)


asyncio.run(get_careteam())
