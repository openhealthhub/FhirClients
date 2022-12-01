<?php


namespace OpenHealthhub\PhpFhirClient;


use DCarbone\PHPFHIRGenerated\R4\FHIRElement\FHIRBackboneElement\FHIRAppointment\FHIRAppointmentParticipant;
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
use DCarbone\PHPFHIRGenerated\R4\FHIRResource\FHIRDomainResource\FHIRAppointment;
use DCarbone\PHPFHIRGenerated\R4\FHIRResource\FHIRDomainResource\FHIRCarePlan;
use DCarbone\PHPFHIRGenerated\R4\FHIRResource\FHIRDomainResource\FHIRPatient;

class CarePlanClient
{
    public function getCarePlan($id): FHIRCarePlan
    {
        $client = new FhirClient();
        $res = $client->get(sprintf('CarePlan/%s', $id));
        return new FHIRCarePlan($res);
    }

    public function searchCarePlans($planDefReference): FHIRBundle
    {
        $client = new FhirClient();
        $res = $client->search(sprintf('CarePlan?instantiates-canonical=PlanDefinition/%s', $planDefReference));
        return new FHIRBundle($res);
    }

    public function searchCarePlanWithPractitioners($id): FHIRBundle
    {
        $client = new FhirClient();
        $res = $client->search(sprintf('CarePlan?_id=%s&_include=CarePlan:care-team&_include=CareTeam:participant', $id));
        return new FHIRBundle($res);
    }

    public function createCarePlan(): FHIRCarePlan
    {
        $client = new FhirClient();
        $careplan = $this->createCarePlanResource();

        $res = $client->create('CarePlan', $careplan);
        return new FHIRCarePlan($res);
    }

    public function updateCarePlan(): FHIRCarePlan
    {
        $client = new FhirClient();
        $careplan = $this->createCarePlanResource();
        $careplan->setId("1");

        $res = $client->update('CarePlan', $careplan);
        return new FHIRCarePlan($res);
    }

    public function deleteCarePlan($id)
    {
        $client = new FhirClient();
        $res = $client->delete(sprintf('CarePlan/%s', $id));
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
