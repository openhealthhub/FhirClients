import asyncio

from config.settings import client


async def questionnaire_response():
    response = await client.resource('QuestionnaireResponse').execute('57a1f708-d9cf-4d8c-9f25-b5a450e7f0ca', 'GET')

    print(response.resourceType, response.id)

    qrs = await client.resources('QuestionnaireResponse').search(based_on='PlanDefinition/97f680b9-e397-4298-8c53-de62a284c806',
                                                                 patient__identifier='6226217e').fetch()

    for q in qrs:
        print(q.reference)


asyncio.run(questionnaire_response())
