#!/bin/bash

source ../config.sh
source ../auth/auth.sh

printf "Getting QuestionnaireResponse\n\n"

curl "$baseUrl/QuestionnaireResponse/1" -H "$apiKeyHeader" -H "$authHeader"

printf "\n\nSearching QuestionnaireResponse\n\n"

curl "$baseUrl/QuestionnaireResponse?identifier=patientnumber&part-of=programuuid" -H "$apiKeyHeader" -H "$authHeader"
