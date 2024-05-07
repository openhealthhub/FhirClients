import asyncio
import json
import sys

sys.path.append("../")

from config.settings import client
from aiohttp import ClientSession


async def deliver_transaction(transaction_bundle):
    async with ClientSession() as session:
        async with session.post(client.server_url, data=json.dumps(transaction_bundle)) as resp:
            return await resp.text()

async def execute_batch():
    with open("batch.json", "r") as file:
        batch_json = json.load(file)

    response = await deliver_transaction(batch_json)

    print(response)


asyncio.run(get_careplan())
