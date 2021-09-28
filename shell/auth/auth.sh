#!/bin/sh

token=$(curl --location --request POST 'https://auth.openhealthhub.com/auth/realms/OpenHealthHub/protocol/openid-connect/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'client_id=api-sandbox' \
--data-urlencode 'client_secret=95810e52-4307-41f5-99a4-d873ab63b536' \
--data-urlencode 'grant_type=client_credentials'  | jq -r '.access_token')

authHeader="Authorization: Bearer $token"

