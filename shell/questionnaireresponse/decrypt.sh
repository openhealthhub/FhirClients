#!/bin/bash

source ../config.sh

gpg --import "../../sandbox.key"

curl "$baseUrl/QuestionnaireResponse/1" -H "$apiKeyHeader" |
 jq -r '.extension[] | select(.url="http://openhealthhub.com/StructureDefinition/encryptedAnswers") | .valueString' |
 gpg -d
