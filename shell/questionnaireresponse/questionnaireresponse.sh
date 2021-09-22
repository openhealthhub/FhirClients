#!/bin/bash

source ../config.sh
source ../auth/auth.sh

printf "Getting QuestionnaireResponse\n\n"

curl "$baseUrl/QuestionnaireResponse/57a1f708-d9cf-4d8c-9f25-b5a450e7f0ca" -H "$apiKeyHeader" -H "$authHeader"

printf "\n\nSearching QuestionnaireResponse\n\n"

curl "$baseUrl/QuestionnaireResponse?based-on.instantiatesCanonical=97f680b9-e397-4298-8c53-de62a284c806" -H "$apiKeyHeader" -H "$authHeader"
