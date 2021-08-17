#!/bin/bash

source ../config.sh
source ../auth/auth.sh

curl -X POST "$baseUrl/Subscription" \
      -H 'Content-Type: application/json' \
      -H "$apiKeyHeader" \
      -H "$authHeader" \
      --data-binary '@subscription.json'
