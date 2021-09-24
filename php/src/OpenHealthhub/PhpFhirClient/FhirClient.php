<?php

namespace OpenHealthhub\PhpFhirClient;

class FhirClient
{
    const FHIR_ENDPOINT = 'https://api-sandbox-staging.openhealthhub.com/OpenHealthhub/fhir-sandbox/4/';
    const KEYCLOAK_OIDC_TOKEN_URL = 'https://auth-staging.openhealthhub.com/auth/realms/OpenHealthHub/protocol/openid-connect/token';
    const CLIENT_ID = 'api-sandbox';
    const CLIENT_SECRET = '915e87d4-16ee-4ca5-b701-b38b6afce8ff';
    const API_KEY = 'ad880601-b7e6-4d86-901d-b6fca96fc725';

    public function get($resourceWithParams)
    {
        $url = self::FHIR_ENDPOINT . $resourceWithParams . '?_format=json';
        return $this->request($url);
    }

    public function search($resourceWithParams)
    {
        $url = self::FHIR_ENDPOINT . $resourceWithParams . '&_format=json';
        return $this->request($url);
    }

    public function request(string $url)
    {
        $ch = curl_init($url);
        $token = $this->get_token();
        curl_setopt_array($ch, [
            CURLOPT_RETURNTRANSFER => true,
            CURLOPT_FOLLOWLOCATION => true,
            CURLOPT_HTTPHEADER => array(
                'X-API-Key: ' . self::API_KEY
            ),
            CURLOPT_XOAUTH2_BEARER => $token,
            CURLOPT_HTTPAUTH => CURLAUTH_BEARER
        ]);
        $res = curl_exec($ch);
        curl_close($ch);
        return json_decode($res, true);
    }

    public function create($resourceName, $resource)
    {
        $body = json_encode($resource);
        $res = $this->createRaw($resourceName, $body, 'application/json');
        return json_decode($res, true);
    }

    public function get_token()
    {
        $ch = curl_init(self::KEYCLOAK_OIDC_TOKEN_URL);
        curl_setopt_array($ch, [
            CURLOPT_RETURNTRANSFER => true,
            CURLOPT_POST => true,
            CURLOPT_POSTFIELDS => 'client_id=' . self::CLIENT_ID . '&client_secret=' . self::CLIENT_SECRET . '&grant_type=client_credentials',
            CURLOPT_HTTPHEADER => [
                'Content-Type: application/x-www-form-urlencoded'
            ],
        ]);
        $res = curl_exec($ch);
        curl_close($ch);
        $decoded_json = json_decode($res, true);
        return $decoded_json['access_token'];
    }

    public function createRaw($resourceName, $body, $contentType)
    {
        $url = self::FHIR_ENDPOINT . $resourceName;
        $ch = curl_init($url);
        $token = $this->get_token();
        curl_setopt_array($ch, [
            CURLOPT_RETURNTRANSFER => true,
            CURLOPT_FOLLOWLOCATION => true,
            CURLOPT_POST => true,
            CURLOPT_POSTFIELDS => $body,
            CURLOPT_HTTPHEADER => array(
                'X-API-Key: ' . self::API_KEY,
                'Content-Type: ' . $contentType
            ),
            CURLOPT_XOAUTH2_BEARER => $token,
            CURLOPT_HTTPAUTH => CURLAUTH_BEARER
        ]);
        $res = curl_exec($ch);
        curl_close($ch);
        return $res;
    }
}
