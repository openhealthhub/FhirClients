<?php

use DCarbone\PHPFHIRGenerated\R4\FHIRElement\FHIRCanonical;
use DCarbone\PHPFHIRGenerated\R4\FHIRElement\FHIRContactPoint;
use DCarbone\PHPFHIRGenerated\R4\FHIRElement\FHIRContactPointSystem;
use DCarbone\PHPFHIRGenerated\R4\FHIRElement\FHIRExtension;
use DCarbone\PHPFHIRGenerated\R4\FHIRElement\FHIRHumanName;
use DCarbone\PHPFHIRGenerated\R4\FHIRElement\FHIRIdentifier;
use DCarbone\PHPFHIRGenerated\R4\FHIRElement\FHIRInstant;
use DCarbone\PHPFHIRGenerated\R4\FHIRElement\FHIRParticipationStatus;
use DCarbone\PHPFHIRGenerated\R4\FHIRElement\FHIRPeriod;
use DCarbone\PHPFHIRGenerated\R4\FHIRElement\FHIRReference;
use DCarbone\PHPFHIRGenerated\R4\FHIRResource\FHIRBundle;
use DCarbone\PHPFHIRGenerated\R4\FHIRResource\FHIRDomainResource\FHIRCarePlan;
use DCarbone\PHPFHIRGenerated\R4\FHIRResource\FHIRDomainResource\FHIRPatient;

class BatchClient
{

    public function batchRequest(): FHIRBundle
    {
        $transactionBundle = array(
            'resourceType' => 'Bundle',
            'type' => 'transaction',
            'entry' => array(
                array(
                    'request' => array(
                        'method' => 'POST',
                        'url' => 'CarePlan',
                    ),
                    'resource' => $this->createCarePlanResource()->getValues(),
                ),
                array(
                    'request' => array(
                        'method' => 'GET',
                        'url' => 'CarePlan/1',
                    ),
                ),
            )
        );

        $client = new \OpenHealthhub\PhpFhirClient\FhirClient();
        $res = $client->create('Bundle', $transactionBundle);
        return new FHIRBundle($res);
    }

    private function createCarePlanResource(): FHIRCarePlan
    {
        $careplan = new FHIRCarePlan();

        $patient = new FHIRPatient();
        $patientName = new FHIRHumanName();
        $patientName->setText('Test Patient');
        $patientEmail = new FHIRContactPoint();
        $telecomType = new FHIRContactPointSystem();
        $telecomType->setValue('email');
        $patientEmail->setSystem($telecomType)->setValue('test@patient.ohh');
        $patientIdentifier = new FHIRIdentifier();
        $patientIdentifier->setSystem('urn:oid:2.16.840.1.113883.2.4.99')->setValue('1234');
        $patient->setId('patient')->addName($patientName)->addTelecom($patientEmail)->addIdentifier($patientIdentifier);
        $careplan->addContained($patient);
        $careplan->setSubject(new FHIRReference(['#patient']));

        $careplan->setInstantiatesCanonical([new FHIRCanonical('PlanDefinition/cca2eaf3-03a9-46c0-88c6-e0287917cea6')]);
        $period = new FHIRPeriod();
        $period->setStart([new FHIRInstant('2021-03-16T13:32:37.430+01:00')]);
        $careplan->setPeriod($period);
        return $careplan;
    }
}
