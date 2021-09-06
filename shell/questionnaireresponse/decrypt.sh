#!/bin/bash

source ../config.sh
source ../auth/auth.sh

gpg --import "../../sandbox.key"

curl "$baseUrl/QuestionnaireResponse/1" -H "$apiKeyHeader"  -H "$authHeader" |
 jq -r '.extension[] | select(.url="http://openhealthhub.com/fhir/StructureDefinition/encryptedAnswers") | .valueString' |
 gpg -d
