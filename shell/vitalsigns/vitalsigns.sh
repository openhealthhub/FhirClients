#!/bin/bash

source ../config.sh

printf "Getting vital signs\n"
curl "$baseUrl/Observation/1" -H "$apiKeyHeader"

printf "\n\n"

printf "Searching vital signs\n"
curl "$baseUrl/Observation?identifier=identifier&device-name=devicename" -H "$apiKeyHeader"
