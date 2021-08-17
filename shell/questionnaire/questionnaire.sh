#!/bin/bash

source ../config.sh
source ../auth/auth.sh

printf "Getting Questionnaire\n\n"

curl "$baseUrl/Questionnaire/1" -H "$apiKeyHeader" -H "$authHeader"

