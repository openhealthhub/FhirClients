package com.openhealthhub.batch;

import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import com.openhealthhub.util.FhirUtil;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.CareTeam;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Period;
import org.hl7.fhir.r4.model.Reference;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class BatchClient {

    private final IGenericClient client;

    public static void main(String... args) {
        var client = new BatchClient();

        FhirUtil.printResource(client.createBatch());
    }

    public Bundle createBatch() {
        var getCarePlanEntry = new Bundle.BundleEntryComponent().setRequest(
                new Bundle.BundleEntryRequestComponent().setMethod(Bundle.HTTPVerb.GET)
                        .setUrl(client.getServerBase() + "/Careplan/123"));
        var createCarePlanEntry = new Bundle.BundleEntryComponent().setRequest(
                new Bundle.BundleEntryRequestComponent().setMethod(Bundle.HTTPVerb.POST)
                        .setUrl(client.getServerBase() + "/Careplan/456")).setResource(new CarePlan());
        var batchBundle = new Bundle().setType(Bundle.BundleType.BATCH)
                .addEntry(getCarePlanEntry)
                .addEntry(createCarePlanEntry);

        return client.transaction()
                .withBundle(batchBundle)
                .execute();
    }

    public Bundle getCarePlanWithPractitioners() {
        return client.search()
                .forResource(CarePlan.class)
                .where(IAnyResource.RES_ID.exactly().identifier("4"))
                .include(new Include("CarePlan:" + CarePlan.SP_CARE_TEAM))
                .include(new Include("CareTeam:" + CareTeam.SP_PARTICIPANT))
                .returnBundle(Bundle.class)
                .execute();
    }

    private void deleteCarePlan() {
        client.delete().resourceById("CarePlan", "1").execute();
    }

    private MethodOutcome updateCarePlan() {
        var carePlan = createCarePlanResource();
        carePlan.setId("1");

        return client.update().resource(carePlan).execute();
    }

    BatchClient() {
        client = FhirUtil.createClient();
    }

    CarePlan getCarePlan(String id) {
        return client.read()
                .resource(CarePlan.class)
                .withId(id)
                .execute();
    }

    MethodOutcome createCarePlan() {
        CarePlan carePlan = createCarePlanResource();

        return client.create().resource(carePlan).execute();

    }

    private CarePlan createCarePlanResource() {
        CarePlan carePlan = new CarePlan();
        carePlan.setPeriod(new Period().setStart(Date.from(Instant.now())));

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

        carePlan.addContained(patient);
        carePlan.setSubject(new Reference("#patient"));
        carePlan.setInstantiatesCanonical(List.of(new CanonicalType("PlanDefinition/cca2eaf3-03a9-46c0-88c6-e0287917cea6")));
        return carePlan;
    }
}
