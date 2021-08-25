#!/bin/sh

token=$(curl --location --request POST 'https://auth-staging.openhealthhub.com/auth/realms/OpenHealthHub/protocol/openid-connect/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'client_id=api-sandbox' \
--data-urlencode 'client_secret=915e87d4-16ee-4ca5-b701-b38b6afce8ff' \
--data-urlencode 'grant_type=client_credentials'  | jq -r '.access_token')

authHeader="Authorization: Bearer $token"

