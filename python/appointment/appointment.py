import json

from config.settings import client

appointment = await client.resources('Appointment').search(_id='1')

print(appointment.description)

with open('appointment.json', 'r') as file:
    appointmentJson = json.load(file)

createResponse = client.execute('Appointment', method='post', data=appointmentJson)

print(createResponse)
