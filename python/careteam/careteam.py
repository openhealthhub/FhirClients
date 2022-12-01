import asyncio
import sys

sys.path.append('../')
from config.settings import client


async def get_careteam():
    care_team = await client.resource('CareTeam').execute('1', 'GET')

    print(care_team)

    get_with_practitioners = await client.resources('CareTeam').search(
       _id=1).include('CareTeam', 'participant').fetch_raw()

    print(get_with_practitioners)


asyncio.run(get_careteam())
