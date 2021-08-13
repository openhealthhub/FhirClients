#!/bin/bash

source ../config.sh

printf "Getting QuestionnaireResponse\n\n"

curl "$baseUrl/QuestionnaireResponse/1" -H "$apiKeyHeader"

printf "\n\nSearching QuestionnaireResponse\n\n"

curl "$baseUrl/QuestionnaireResponse?identifier=patientnumber&part-of=programuuid" -H "$apiKeyHeader"
