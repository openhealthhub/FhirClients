import asyncio
import json
import sys

sys.path.append('../')
from config.settings import client


async def get_careplan():
    care_plan = await client.resource('CarePlan').execute('1', 'GET')

    print(care_plan.instantiatesCanonical)

    search_response = await client.resources('CarePlan').search(
        **{'instantiates-canonical': 'PlanDefinition/97f680b9-e397-4298-8c53-de62a284c806'}).fetch()

    print(search_response)

    get_with_practitioners = await client.resources('CarePlan').search(
       _id=1).include('CarePlan', 'care-team').include('CareTeam', 'participant').fetch_raw()

    print(get_with_practitioners)

    with open('careplan.json', 'r') as file:
        careplan_json = json.load(file)

    create_response = await client.execute('CarePlan', method='post', data=careplan_json)

    print(create_response)

    update_response = await client.execute('CarePlan', method='put', data=careplan_json)

    print(update_response)

    delete_response = await client.resource('CarePlan').execute('1', 'DELETE')

    print(delete_response)


asyncio.run(get_careplan())
