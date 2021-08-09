import json

from config.settings import server
import fhirclient.models.subscription as s
with open('subscription.json', 'r') as file:
    appointmentJson = json.load(file)

createResponse = s.Subscription(appointmentJson).create(server=server)

print(createResponse)
