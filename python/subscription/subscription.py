import asyncio
import json

from config.settings import client


async def create_subscription():
    with open('subscription.json', 'r') as file:
        subJson  = json.load(file)

    createResponse = await client.execute('Subscription', method='post', data=subJson)

    print(createResponse)

asyncio.run(create_subscription())
