import asyncio
import json

from config.settings import client


async def get_appointment():
    appointment = await client.resource('Appointment').execute('1', 'GET')

    print(appointment.description)

    with open('appointment.json', 'r') as file:
        appointment_json = json.load(file)

    create_response = await client.execute('Appointment', method='post', data=appointment_json)

    print(create_response)


asyncio.run(get_appointment())


