import asyncio

from config.settings import client


async def get_questionnaire():
    questionnaire = await client.resource('Questionnaire').execute('1', 'GET')

    print(questionnaire.description)


asyncio.run(get_questionnaire())
