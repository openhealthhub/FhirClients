<?php

namespace OpenHealthhub\PhpFhirClient;

class FhirClient
{
    public function get($resourceWithParams)
    {
        $url = sprintf('https://api-sandbox-staging.openhealthhub.com/fhir/%s?_format=json', $resourceWithParams);
        $ch = curl_init($url);
        curl_setopt_array($ch, [
            CURLOPT_RETURNTRANSFER => true,
            CURLOPT_FOLLOWLOCATION => true,
        ]);
        $res = curl_exec($ch);
        curl_close($ch);
        return json_decode($res, true);
    }
}
