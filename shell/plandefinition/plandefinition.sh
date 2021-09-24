#!/bin/bash

source ../config.sh
source ../auth/auth.sh

printf "Getting PlanDefinition\n"
curl "$baseUrl/PlanDefinition/4944e73f-e447-49ba-a64c-a246b9ef4bdd" -H "$apiKeyHeader" -H "$authHeader"

printf "\n\n"

printf "Searching PlanDefinition\n"
curl "$baseUrl/PlanDefinition?definition=Questionnaire/866683f3-c41b-47c0-b42f-86f9ff978d1d&publisher=Program Creator" -H "$apiKeyHeader" -H "$authHeader"
