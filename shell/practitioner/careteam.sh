#!/bin/bash

source ../config.sh
source ../auth/auth.sh

printf "Getting Practitioner\n"
curl "$baseUrl/Practitioner/3" -H "$apiKeyHeader" -H "$authHeader"

printf "\n\n"

printf "searching Practitioners by CareTeam id\n"
curl "$baseUrl/Practitioner/?_has:CareTeam:_id=3" -H "$apiKeyHeader" -H "$authHeader"
