#!/bin/bash

baseUrl="https://api-sandbox-staging.openhealthhub.com/fhir"

curl -X POST "$baseUrl/Subscription" \
      -H 'Content-Type: application/json' \
      --data-binary '@subscription.json'
