from config.settings import client

questionnaire = await client.resources('Questionnaire').search(_id='1')

print(questionnaire.description)
