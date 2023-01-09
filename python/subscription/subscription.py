import asyncio
import json

from config.settings import client
from fhirpy.lib import AsyncFHIRResource


async def create_subscription():
    with open("subscription.json", "r") as file:
        subJson = json.load(file)

    subscription: AsyncFHIRResource = await client.resource("Subscription", **subJson)
    await subscription.save()

    print(subscription)


asyncio.run(create_subscription())
