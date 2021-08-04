package com.openhealthhub.appointment;

import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import com.openhealthhub.util.FhirUtil;
import org.hl7.fhir.r4.model.Appointment;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Reference;

import java.util.Date;

public class AppointmentClient {

    private final IGenericClient client;

    public static void main(String... args) {
        String action = args.length == 0 ? "get" : args[0];
        AppointmentClient client = new AppointmentClient();

        if ("create".equals(action)) {
            FhirUtil.printResource(client.createAppointment());
            return;
        }

        FhirUtil.printResource(client.getAppointment(args.length == 2 ? args[1] : "4"));
    }

    AppointmentClient() {
        client = FhirUtil.createClient();
    }

    Appointment getAppointment(String id) {
        return client.read()
                .resource(Appointment.class)
                .withId(id)
                .execute();
    }

    MethodOutcome createAppointment() {
        Appointment appointment = new Appointment()
                .setStatus(Appointment.AppointmentStatus.BOOKED)
                .setAppointmentType(new CodeableConcept().addCoding(new Coding().setSystem("http://terminology.hl7.org/CodeSystem/v2-0276")
                        .setCode("FOLLOWUP")
                        .setDisplay("A follow up visit from a previous appointment")))
                .addReasonReference(new Reference("Condition/example").setDisplay("Severe burn of left ear"))
                .setPriority(5)
                .setDescription("Discussion on the results of your recent MRI")
                .setStart(new Date())
                .setEnd(new Date())
                .setComment("Further expand on the results of the MRI and determine the next actions that may be appropriate.")
                .addParticipant(new Appointment.AppointmentParticipantComponent().setActor(new Reference("Patient/1234"))
                        .setRequired(Appointment.ParticipantRequired.REQUIRED)
                        .setStatus(Appointment.ParticipationStatus.ACCEPTED))
                .addParticipant(new Appointment.AppointmentParticipantComponent().setActor(new Reference("Practitioner/example")));

        return client.create()
                .resource(appointment)
                .execute();

    }
}
