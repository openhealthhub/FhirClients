<?php


namespace OpenHealthhub\PhpFhirClient;


use DCarbone\PHPFHIRGenerated\R4\FHIRResource\FHIRDomainResource\FHIRQuestionnaireResponse;

class QuestionnaireResponseClient
{
    public function getQuestionnaireResponse($id): FHIRQuestionnaireResponse
    {
        $client = new FhirClient();
        $res = $client->get(sprintf('QuestionnaireResponse/%s', $id));
        return new FHIRQuestionnaireResponse($res);
    }
}