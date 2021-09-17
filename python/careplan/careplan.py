import asyncio
import json

from config.settings import client


async def get_careplan():
    care_plan = await client.resource('CarePlan').execute('1', 'GET')

    print(care_plan.instantiatesCanonical)

    with open('careplan.json', 'r') as file:
        appointment_json = json.load(file)

    create_response = await client.execute('CarePlan', method='post', data=appointment_json)

    print(create_response)


asyncio.run(get_careplan())


