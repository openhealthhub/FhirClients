<?php


namespace OpenHealthhub\PhpFhirClient;


use DCarbone\PHPFHIRGenerated\R4\FHIRResource\FHIRDomainResource\FHIRAppointment;

class AppointmentClient
{
    public function getAppointment($id): FHIRAppointment
    {
        $client = new FhirClient();
        $res = $client->get(sprintf('Appointment/%s', $id));
        return new FHIRAppointment($res);
    }
}
