#!/bin/bash

baseUrl="https://api-sandbox-staging.openhealthhub.com/fhir"

gpg --import "../../sandbox.key"

curl "$baseUrl/QuestionnaireResponse/1" |
 jq -r '.extension[] | select(.url="http://openhealthhub.com/StructureDefinition/encryptedAnswers") | .valueString' |
 gpg -d
