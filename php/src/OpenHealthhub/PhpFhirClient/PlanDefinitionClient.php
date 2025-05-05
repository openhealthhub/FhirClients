<?php


namespace OpenHealthhub\PhpFhirClient;


use DCarbone\PHPFHIRGenerated\R4\FHIRResource\FHIRBundle;
use DCarbone\PHPFHIRGenerated\R4\FHIRResource\FHIRDomainResource\FHIRPlanDefinition;

class PlanDefinitionClient
{
    public function getPlanDefinition($id): FHIRPlanDefinition
    {
        $client = new FhirClient();
        $res = $client->get(sprintf('PlanDefinition/%s', $id));
        return new FHIRPlanDefinition($res);
    }

    public function searchPlanDefinition($questionnaire, $publisher): FHIRBundle
    {
        $client = new FhirClient();
        $res = $client->search(sprintf('PlanDefinition?definition=Questionnaire/%s&publisher=%s', $questionnaire, $publisher));
        return new FHIRBundle($res);
    }
}
