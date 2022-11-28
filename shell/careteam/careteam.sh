#!/bin/bash

source ../config.sh
source ../auth/auth.sh

printf "Getting CareTeam\n"
curl "$baseUrl/CareTeam/3" -H "$apiKeyHeader" -H "$authHeader"

printf "\n\n"

printf "Getting CareTeam with Practitioners\n"
curl "$baseUrl/CareTeam/?_id=3&_include=CareTeam:participant" -H "$apiKeyHeader" -H "$authHeader"
