<?php

namespace OpenHealthhub\PhpFhirClient\Test;

use OpenHealthhub\PhpFhirClient\BinaryClient;
use OpenHealthhub\PhpFhirClient\CarePlanClient;
use OpenHealthhub\PhpFhirClient\PlanDefinitionClient;
use OpenHealthhub\PhpFhirClient\QuestionnaireClient;
use OpenHealthhub\PhpFhirClient\QuestionnaireResponseClient;
use OpenHealthhub\PhpFhirClient\SubscriptionClient;
use OpenHealthhub\PhpFhirClient\VitalSignsClient;
use PHPUnit\Framework\TestCase;

class ClientTest extends TestCase
{
    public function testGetCarePlan()
    {
        $client = new CarePlanClient();
        $resp = $client->getCarePlan('4');
        $this->assertInstanceOf('DCarbone\PHPFHIRGenerated\R4\FHIRResource\FHIRDomainResource\FHIRCarePlan', $resp);
        $this->assertEquals('4', $resp->getId()->getValue()->getValue());
    }

    public function testSearchCarePlan()
    {
        $client = new CarePlanClient();
        $resp = $client->searchCarePlans('PlanDefinition/97f680b9-e397-4298-8c53-de62a284c806');
        $this->assertInstanceOf('DCarbone\PHPFHIRGenerated\R4\FHIRResource\FHIRBundle', $resp);
        $this->assertEquals(1, count($resp->getEntry()));
        $this->assertInstanceOf('DCarbone\PHPFHIRGenerated\R4\FHIRResource\FHIRDomainResource\FHIRCarePlan', $resp->getEntry()[0]->getResource());
    }

    public function testCreateCarePlan()
    {
        $client = new CarePlanClient();
        $resp = $client->createCarePlan();
        $this->assertInstanceOf('DCarbone\PHPFHIRGenerated\R4\FHIRResource\FHIRDomainResource\FHIRCarePlan', $resp);
    }

    public function testUpdateCarePlan()
    {
        $client = new CarePlanClient();
        $resp = $client->updateCarePlan();
        $this->assertInstanceOf('DCarbone\PHPFHIRGenerated\R4\FHIRResource\FHIRDomainResource\FHIRCarePlan', $resp);
    }

    public function testDeleteCarePlan()
    {
        $client = new CarePlanClient();
        $client->deleteCarePlan(4);
        $this->assertTrue(true);
    }

    public function testGetVitalSigns()
    {
        $client = new VitalSignsClient();
        $resp = $client->getVitalSigns(4);
        $this->assertInstanceOf('DCarbone\PHPFHIRGenerated\R4\FHIRResource\FHIRDomainResource\FHIRObservation', $resp);
        $this->assertEquals('4', $resp->getId()->getValue()->getValue());
    }

    public function testSearchVitalSigns()
    {
        $client = new VitalSignsClient();
        $resp = $client->searchVitalSigns('device', '1234');
        $this->assertInstanceOf('DCarbone\PHPFHIRGenerated\R4\FHIRResource\FHIRBundle', $resp);
        $this->assertEquals(4, count($resp->getEntry()));
        $this->assertInstanceOf('DCarbone\PHPFHIRGenerated\R4\FHIRResource\FHIRDomainResource\FHIRObservation', $resp->getEntry()[0]->getResource());
    }

    public function testGetPlanDefinition()
    {
        $client = new PlanDefinitionClient();
        $resp = $client->getPlanDefinition("4944e73f-e447-49ba-a64c-a246b9ef4bdd");
        $this->assertInstanceOf('DCarbone\PHPFHIRGenerated\R4\FHIRResource\FHIRDomainResource\FHIRPlanDefinition', $resp);
        $this->assertEquals('4944e73f-e447-49ba-a64c-a246b9ef4bdd', $resp->getId()->getValue()->getValue());
    }

    public function testSearchPlanDefintion()
    {
        $client = new VitalSignsClient();
        $resp = $client->searchVitalSigns('866683f3-c41b-47c0-b42f-86f9ff978d1d', 'Program Creator');
        $this->assertInstanceOf('DCarbone\PHPFHIRGenerated\R4\FHIRResource\FHIRBundle', $resp);
        $this->assertEquals(1, count($resp->getEntry()));
        $this->assertInstanceOf('DCarbone\PHPFHIRGenerated\R4\FHIRResource\FHIRDomainResource\FHIRPlanDefinition', $resp->getEntry()[0]->getResource());
    }

    public function testGetQuestionnaire()
    {
        $client = new QuestionnaireClient();
        $resp = $client->getQuestionnaire(4);
        $this->assertInstanceOf('DCarbone\PHPFHIRGenerated\R4\FHIRResource\FHIRDomainResource\FHIRQuestionnaire', $resp);
        $this->assertEquals('4', $resp->getId()->getValue()->getValue());
    }

    public function testGetQuestionnaireResponse()
    {
        $client = new QuestionnaireResponseClient();
        $resp = $client->getQuestionnaireResponse('PlanDefinition/57a1f708-d9cf-4d8c-9f25-b5a450e7f0ca');
        $this->assertInstanceOf('DCarbone\PHPFHIRGenerated\R4\FHIRResource\FHIRDomainResource\FHIRQuestionnaireResponse', $resp);
        $this->assertEquals('57a1f708-d9cf-4d8c-9f25-b5a450e7f0ca', $resp->getId()->getValue()->getValue());
    }

    public function testSearchQuestionnaireResponse()
    {
        $client = new QuestionnaireResponseClient();
        $resp = $client->searchQuestionnaireResponses('PlanDefinition/97f680b9-e397-4298-8c53-de62a284c806');
        $this->assertInstanceOf('DCarbone\PHPFHIRGenerated\R4\FHIRResource\FHIRBundle', $resp);
        $this->assertEquals(1, count($resp->getEntry()));
        $this->assertInstanceOf('DCarbone\PHPFHIRGenerated\R4\FHIRResource\FHIRDomainResource\FHIRQuestionnaireResponse', $resp->getEntry()[0]->getResource());
    }

    public function testCreateSubscription()
    {
        $client = new SubscriptionClient();
        $resp = $client->createSubscription();
        $this->assertInstanceOf('DCarbone\PHPFHIRGenerated\R4\FHIRResource\FHIRDomainResource\FHIRSubscription', $resp);
    }

    public function testUploadKey()
    {
        $client = new BinaryClient();
        $resp = $client->uploadKey();
        $this->assertEquals('', $resp);
    }
}
