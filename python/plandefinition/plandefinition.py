import asyncio

from config.settings import client


async def get_plandefinition():
    plandefinition = await client.resource('PlanDefinition').execute('1', 'GET')

    print(plandefinition.resourceType, plandefinition.id)

    plandefinitions = await client.resources('PlanDefinition').search(definition='Questionnaire/866683f3-c41b-47c0-b42f-86f9ff978d1d',
                                                                      publisher='Program Creator').fetch()

    for p in plandefinitions:
        print(p.description)


asyncio.run(get_plandefinition())
