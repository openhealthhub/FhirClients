package config

import "github.com/google/fhir/go/fhirversion"

const Api = "https://api-sandbox-staging.openhealthhub.com/OpenHealthhub/fhir/4"
const ApiKey = "90da723b-70f7-400b-8ddb-8574c45bff13"
const TimeZone = "Europe/Amsterdam"
const FhirVersion = fhirversion.R4

const KeycloakTokenUri = "https://auth-staging.openhealthhub.com/auth/realms/OpenHealthHub/protocol/openid-connect/token"
const KeycloakClientId = "api-sandbox"
const KeycloakClientSecret = "915e87d4-16ee-4ca5-b701-b38b6afce8ff"
