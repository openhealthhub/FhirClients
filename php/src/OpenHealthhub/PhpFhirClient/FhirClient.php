<?php

namespace OpenHealthhub\PhpFhirClient;

class FhirClient
{
    const FHIR_ENDPOINT = 'https://api-sandbox-staging.openhealthhub.com/OpenHealthhub/fhir/4/';

    public function get($resourceWithParams)
    {
        $url = self::FHIR_ENDPOINT . $resourceWithParams .'?_format=json';
        return $this->request($url);
    }

    public function search($resourceWithParams)
    {
        $url = self::FHIR_ENDPOINT . $resourceWithParams. '&_format=json';
        return $this->request($url);
    }

    public function request(string $url)
    {
        $ch = curl_init($url);
        curl_setopt_array($ch, [
            CURLOPT_RETURNTRANSFER => true,
            CURLOPT_FOLLOWLOCATION => true,
            CURLOPT_HTTPHEADER, array(
                'X-API-Key: ad880601-b7e6-4d86-901d-b6fca96fc725'
            )
        ]);
        $res = curl_exec($ch);
        curl_close($ch);
        return json_decode($res, true);
    }

    public function create($resourceName, $resource)
    {
        $url = self::FHIR_ENDPOINT . $resourceName;
        $ch = curl_init($url);
        curl_setopt_array($ch, [
            CURLOPT_RETURNTRANSFER => true,
            CURLOPT_FOLLOWLOCATION => true,
            CURLOPT_POST => true,
            CURLOPT_POSTFIELDS => json_encode($resource),
            CURLOPT_HTTPHEADER, array(
                'X-API-Key: ad880601-b7e6-4d86-901d-b6fca96fc725'
            )
        ]);
        $res = curl_exec($ch);
        curl_close($ch);
        return json_decode($res, true);
    }
}
