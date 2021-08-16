import asyncio

from config.settings import client


async def questionnaire_response():
    response = await client.resource('QuestionnaireResponse').execute('1', 'GET')

    print(response.resourceType, response.id)

    qrs = await client.resources('QuestionnaireResponse').search(part_of='programUUID', identifier='patientnumber').fetch()

    for q in qrs:
        print(q.reference)

asyncio.run(questionnaire_response())
