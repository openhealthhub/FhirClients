#!/bin/bash

source ../config.sh
source ../auth/auth.sh

gpg --import "../../sandbox.key"

curl "$baseUrl/QuestionnaireResponse/1" -H "$apiKeyHeader"  -H "$authHeader" |
 jq -r '.extension[] | select(.url="https://api.openhealthhub.com/OpenHealthhub/fhir/4/StructureDefinition/encryptedAnswers") | .valueString' |
 gpg -d
