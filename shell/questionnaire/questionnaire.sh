#!/bin/bash

baseUrl="https://api-sandbox-staging.openhealthhub.com/fhir"

printf "Getting Questionnaire\n\n"

curl "$baseUrl/Questionnaire/1"

