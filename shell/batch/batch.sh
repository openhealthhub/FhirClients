#!/bin/bash

source ../config.sh
source ../auth/auth.sh

printf 'Executing Batch\n'
curl -X POST "$baseUrl" -H "Content-Type: application/json" -H "$apiKeyHeader" -H "$authHeader" --data-binary "@batch.json"
