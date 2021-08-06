<?php


namespace OpenHealthhub\PhpFhirClient;


use DCarbone\PHPFHIRGenerated\R4\FHIRElement\FHIRAppointmentStatus;
use DCarbone\PHPFHIRGenerated\R4\FHIRResource\FHIRDomainResource\FHIRAppointment;

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
        $status = new FHIRAppointmentStatus();
        $appointment->setStatus($status->setValue('booked'))->setPriority(5)->setDescription("Discussion on the results of your recent MRI");
        $res = $client->create('Appointment', $appointment);
        return new FHIRAppointment($res);
    }
}
