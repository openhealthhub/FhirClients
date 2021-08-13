import json

from config.settings import client

with open('subscription.json', 'r') as file:
    subJson  = json.load(file)

createResponse = client.execute('Subscription', method='post', data=subJson)

print(createResponse)
