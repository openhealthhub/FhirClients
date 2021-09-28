<?php


namespace OpenHealthhub\PhpFhirClient;


class BinaryClient
{
    public function uploadKey()
    {
        $client = new FhirClient();

        $keyASCII = file_get_contents(dirname(__FILE__) . '/../../../../sandbox.pub');

        $encodedPubKey = base64_encode($keyASCII);
        return $client->createRaw("Binary", $encodedPubKey, 'text/plain');
    }
}
