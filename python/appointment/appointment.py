import json

import fhirclient.models.appointment

from config.settings import server
import fhirclient.models.appointment as a

appointment = a.Appointment.read('1', server)

print(appointment.description)

with open('appointment.json', 'r') as file:
    appointmentJson = json.load(file)

createResponse = a.Appointment(appointmentJson).create(server=server)

print(createResponse)
