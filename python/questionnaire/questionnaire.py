import asyncio

from config.settings import client
from fhirpy.lib import AsyncFHIRResource


async def get_questionnaire():
    questionnaire: AsyncFHIRResource = (
        await client.resources("Questionnaire").search(_id=1).first()
    )

    # or

    questionnaire: AsyncFHIRResource = await client.reference(
        "Questionnaire", 1
    ).to_resource()

    print(questionnaire.description)


asyncio.run(get_questionnaire())
