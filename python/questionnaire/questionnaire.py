import fhirclient.models.questionnaire as q

from config.settings import server

questionnaire = q.Questionnaire.read('1', server)

print(questionnaire.description)
