#!/bin/bash

source ../config.sh
source ../auth/auth.sh

printf "Getting CarePlan\n"
curl "$baseUrl/CarePlan/3" -H "$apiKeyHeader" -H "$authHeader"

printf "\n\n"

printf 'Creating CarePlan\n'
curl -X POST "$baseUrl/CarePlan" -H "Content-Type: application/json" -H "$apiKeyHeader" -H "$authHeader" --data-binary "@careplan.json"


printf 'Updating CarePlan\n'
curl -X PUT "$baseUrl/CarePlan/1" -H "Content-Type: application/json" -H "$apiKeyHeader" -H "$authHeader" --data-binary "@careplan.json"

printf 'Deleting CarePlan\n'
curl -X DELETE "$baseUrl/CarePlan/1" -H "$apiKeyHeader" -H "$authHeader"
