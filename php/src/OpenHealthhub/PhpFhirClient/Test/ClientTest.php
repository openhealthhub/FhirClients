<?php

namespace OpenHealthhub\PhpFhirClient\Test;

use OpenHealthhub\PhpFhirClient\AppointmentClient;
use OpenHealthhub\PhpFhirClient\QuestionnaireClient;
use OpenHealthhub\PhpFhirClient\QuestionnaireResponseClient;
use OpenHealthhub\PhpFhirClient\VitalSignsClient;
use PHPUnit\Framework\TestCase;

class ClientTest extends TestCase
{
    public function testGetAppointment()
    {
        $client = new AppointmentClient();
        $resp = $client->getAppointment(4);
        $this->assertInstanceOf('DCarbone\PHPFHIRGenerated\R4\FHIRResource\FHIRDomainResource\FHIRAppointment', $resp);
        $this->assertEquals('4', $resp->getId()->getValue()->getValue());
    }

    public function testGetVitalSigns()
    {
        $client = new VitalSignsClient();
        $resp = $client->getVitalSigns(4);
        $this->assertInstanceOf('DCarbone\PHPFHIRGenerated\R4\FHIRResource\FHIRDomainResource\FHIRObservation', $resp);
        $this->assertEquals('4', $resp->getId()->getValue()->getValue());
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
        $resp = $client->getQuestionnaireResponse(4);
        $this->assertInstanceOf('DCarbone\PHPFHIRGenerated\R4\FHIRResource\FHIRDomainResource\FHIRQuestionnaireResponse', $resp);
        $this->assertEquals('4', $resp->getId()->getValue()->getValue());
    }
}
