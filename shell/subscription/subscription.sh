#!/bin/bash

baseUrl="https://api-sandbox-staging.openhealthhub.com/OpenHealthhub/fhir/4"

curl -X POST "$baseUrl/Subscription" \
      -H 'Content-Type: application/json' \
      --data-binary '@subscription.json'
