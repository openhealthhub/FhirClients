#!/bin/bash

source ../config.sh
source ../auth/auth.sh

printf "Getting Appointment\n"
curl "$baseUrl/Appointment/3" -H "$apiKeyHeader" -H "$authHeader"

printf "\n\n"

printf 'Creating Appointment\n'
curl -X POST "$baseUrl/Appointment" -H "Content-Type: application/json" -H "$apiKeyHeader" -H "$authHeader" --data-binary "@appointment.json"
