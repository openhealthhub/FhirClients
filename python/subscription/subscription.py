import json

from config.settings import server
import fhirclient.models.subscription as s
with open('subscription.json', 'r') as file:
    subJson  = json.load(file)

createResponse = s.Subscription(subJson).create(server=server)

print(createResponse)
