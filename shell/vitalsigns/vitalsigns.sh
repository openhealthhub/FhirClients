#!/bin/bash

baseUrl="https://api-sandbox-staging.openhealthhub.com/fhir"

printf "Getting vital signs\n"
curl "$baseUrl/Observation/1"

printf "\n\n"

printf "Searching vital signs\n"
curl "$baseUrl/Observation?identifier=identifier&device-name=devicename"
