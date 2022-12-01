#!/bin/bash

source ../config.sh
source ../auth/auth.sh

printf "Getting CarePlan\n"
curl "$baseUrl/CarePlan/3" -H "$apiKeyHeader" -H "$authHeader"

printf "\n\n"

printf "Searching CarePlan\n"
curl "$baseUrl/CarePlan/?instantiates-canonical=PlanDefinition/97f680b9-e397-4298-8c53-de62a284c806&patient.identifier=1234" -H "$apiKeyHeader" -H "$authHeader"

printf "\n\n"

printf "Getting CarePlan with Practitioners\n"
curl "$baseUrl/CarePlan/?_id=3&_include=CarePlan:care-team&_include=CareTeam:participant" -H "$apiKeyHeader" -H "$authHeader"

printf "\n\n"

printf 'Creating CarePlan\n'
curl -X POST "$baseUrl/CarePlan" -H "Content-Type: application/json" -H "$apiKeyHeader" -H "$authHeader" --data-binary "@careplan.json"

printf "\n\n"

printf 'Updating CarePlan\n'
curl -X PUT "$baseUrl/CarePlan/1" -H "Content-Type: application/json" -H "$apiKeyHeader" -H "$authHeader" --data-binary "@careplan.json"

printf "\n\n"

printf 'Deleting CarePlan\n'
curl -X DELETE "$baseUrl/CarePlan/1" -H "$apiKeyHeader" -H "$authHeader"
