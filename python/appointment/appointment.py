import json

from config.settings import client

appointment = client.resources('Appointment').search(_id='1')

print(appointment.description)

with open('appointment.json', 'r') as file:
    appointmentJson = json.load(file)

createResponse = a.Appointment(appointmentJson).create(server=server)

print(createResponse)
