#!/bin/bash

source ../config.sh

curl -X POST "$baseUrl/Subscription" \
      -H 'Content-Type: application/json' \
       -H "$apiKeyHeader" \
      --data-binary '@subscription.json'
