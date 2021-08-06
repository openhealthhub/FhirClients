#!/bin/bash

baseUrl="https://api-sandbox-staging.openhealthhub.com/fhir"

printf "Getting QuestionnaireResponse\n\n"

curl "$baseUrl/QuestionnaireResponse/1"

printf "\n\nSearching QuestionnaireResponse\n\n"

curl "$baseUrl/QuestionnaireResponse?identifier=patientnumber&part-of=programuuid"
