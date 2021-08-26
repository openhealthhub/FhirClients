package com.openhealthhub.appointment;

import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import com.openhealthhub.util.FhirUtil;
import org.hl7.fhir.r4.model.Appointment;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

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
        Appointment appointment = new Appointment();
        appointment.setStart(Date.from(Instant.now()));

        Patient patient = new Patient();
        patient.setId("patient");
        HumanName humanName = new HumanName();
        humanName.setText("Test Patient");
        patient.setName(Collections.singletonList(humanName));

        ContactPoint email = new ContactPoint();
        email.setSystem(ContactPoint.ContactPointSystem.EMAIL);
        email.setValue("test@patient.ohh");
        patient.addTelecom(email);

        Identifier programPatientId = new Identifier();
        programPatientId.setSystem("urn:oid:2.16.840.1.113883.2.4.99");
        programPatientId.setValue("1234");
        patient.addIdentifier(programPatientId);

        appointment.addContained(patient);
        Appointment.AppointmentParticipantComponent participantComponent = new Appointment.AppointmentParticipantComponent();
        participantComponent.setActor(new Reference("#patient"));
        participantComponent.setStatus(Appointment.ParticipationStatus.NEEDSACTION);
        appointment.addParticipant(participantComponent);
        appointment.setSupportingInformation(
                Collections.singletonList(new Reference().setReference("PlanDefinition/cca2eaf3-03a9-46c0-88c6-e0287917cea6")));
        appointment.addExtension("http://openhealthhub.com/fhir/StructureDefinition/appointment-pin", new StringType("59gladtc"));

        return client.create().resource(appointment).execute();

    }
}
