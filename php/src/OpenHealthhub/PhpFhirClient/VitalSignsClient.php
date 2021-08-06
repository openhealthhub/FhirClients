<?php


namespace OpenHealthhub\PhpFhirClient;


use DCarbone\PHPFHIRGenerated\R4\FHIRResource\FHIRDomainResource\FHIRObservation;

class VitalSignsClient
{
    public function getVitalSigns($id): FHIRObservation
    {
        $client = new FhirClient();
        $res = $client->get(sprintf('Observation/%s', $id));
        return new FHIRObservation($res);
    }
}
