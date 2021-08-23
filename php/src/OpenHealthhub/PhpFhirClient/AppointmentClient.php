<?php


namespace OpenHealthhub\PhpFhirClient;


use DCarbone\PHPFHIRGenerated\R4\FHIRElement\FHIRBackboneElement\FHIRAppointment\FHIRAppointmentParticipant;
use DCarbone\PHPFHIRGenerated\R4\FHIRElement\FHIRContactPoint;
use DCarbone\PHPFHIRGenerated\R4\FHIRElement\FHIRContactPointSystem;
use DCarbone\PHPFHIRGenerated\R4\FHIRElement\FHIRExtension;
use DCarbone\PHPFHIRGenerated\R4\FHIRElement\FHIRHumanName;
use DCarbone\PHPFHIRGenerated\R4\FHIRElement\FHIRIdentifier;
use DCarbone\PHPFHIRGenerated\R4\FHIRElement\FHIRInstant;
use DCarbone\PHPFHIRGenerated\R4\FHIRElement\FHIRParticipationStatus;
use DCarbone\PHPFHIRGenerated\R4\FHIRElement\FHIRReference;
use DCarbone\PHPFHIRGenerated\R4\FHIRResource\FHIRDomainResource\FHIRAppointment;
use DCarbone\PHPFHIRGenerated\R4\FHIRResource\FHIRDomainResource\FHIRPatient;

class AppointmentClient
{
    public function getAppointment($id): FHIRAppointment
    {
        $client = new FhirClient();
        $res = $client->get(sprintf('Appointment/%s', $id));
        return new FHIRAppointment($res);
    }

    public function createAppointment(): FHIRAppointment
    {
        $client = new FhirClient();
        $appointment = new FHIRAppointment();

        $patient = new FHIRPatient();
        $patientName = new FHIRHumanName();
        $patientName->setText('Test Patient');
        $patientEmail = new FHIRContactPoint();
        $telecomType = new FHIRContactPointSystem();
        $telecomType->setValue('email');
        $patientEmail->setSystem($telecomType)->setValue('test@patient.ohh');
        $patientIdentifier = new FHIRIdentifier();
        $patientIdentifier->setSystem('http://openhealthhub.com/fhir/program-patient-id')->setValue('1234');
        $patient->setId('patient')->addName($patientName)->addTelecom($patientEmail)->addIdentifier($patientIdentifier);
        $appointment->addContained($patient);
        $participant = new FHIRAppointmentParticipant();
        $participant->setStatus(new FHIRParticipationStatus('needs-action'))->setActor(new FHIRReference(['#patent']));
        $appointment->addParticipant($participant);

        $pinExtension = new FHIRExtension();
        $pinExtension->setUrl('http://openhealthhub.com/fhir/StructureDefinition/appointment-pin')->setValueString('59gladtc');
        $appointment->addExtension($pinExtension);

        $appointment->addSupportingInformation(new FHIRReference(['PlanDefinition/cca2eaf3-03a9-46c0-88c6-e0287917cea6']));
        $appointment->setStart(new FHIRInstant('2021-03-16T13:32:37.430+01:00'));

        $res = $client->create('Appointment', $appointment);
        return new FHIRAppointment($res);
    }
}
