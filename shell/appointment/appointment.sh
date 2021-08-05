#!/bin/bash

baseUrl="https://api-sandbox-staging.openhealthhub.com/fhir"

printf "Getting Appointment\n"
curl "$baseUrl/Appointment/3"

printf "\n\n"

printf 'Creating Appointment\n'
curl -X POST "$baseUrl/Appointment" -H "Content-Type: application/json" --data-binary "@appointment.json"
