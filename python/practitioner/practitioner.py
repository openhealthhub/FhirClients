import asyncio
import sys

sys.path.append('../')
from config.settings import client


async def get_practitioner():
    practitioner = await client.resource('Practitioner').execute('1', 'GET')

    print(practitioner)

    search_by_careteam = await client.resources('Practitioner').search(**{'_has:CareTeam:_id':1}).fetch()

    print(search_by_careteam)


asyncio.run(get_practitioner())
