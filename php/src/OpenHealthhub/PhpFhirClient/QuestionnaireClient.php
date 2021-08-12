<?php


namespace OpenHealthhub\PhpFhirClient;


use DCarbone\PHPFHIRGenerated\R4\FHIRResource\FHIRDomainResource\FHIRObservation;
use DCarbone\PHPFHIRGenerated\R4\FHIRResource\FHIRDomainResource\FHIRQuestionnaire;

class QuestionnaireClient
{
    public function getQuestionnaire($id): FHIRQuestionnaire
    {
        $client = new FhirClient();
        $res = $client->get(sprintf('Questionnaire/%s', $id));
        return new FHIRQuestionnaire($res);
    }
}
