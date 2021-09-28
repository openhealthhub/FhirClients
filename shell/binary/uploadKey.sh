#!/bin/bash

source ../config.sh
source ../auth/auth.sh

printf 'Uploading Public Key\n'
base64Key=$(base64 -w 0 ../../sandbox.pub)
curl -X POST "$baseUrl/Binary" -H "Content-Type: text/plain" -H "$apiKeyHeader" -H "$authHeader" --data-raw "$base64Key"
