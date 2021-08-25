#!/bin/bash

source ../config.sh
source ../auth/auth.sh

printf "Getting vital signs\n"
curl "$baseUrl/Observation/1" -H "$apiKeyHeader" -H "$authHeader"

printf "\n\n"

printf "Searching vital signs\n"
curl "$baseUrl/Observation?identifier=identifier&device-name=devicename" -H "$apiKeyHeader" -H "$authHeader"
