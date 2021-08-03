package com.openhealthhub.vitalsigns;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import com.openhealthhub.util.FhirUtil;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Device;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;

public class VitalSignsClient {
    private final IGenericClient client;

    public static void main(String... args) {
        String action = args.length == 0 ? "get" : args[0];
        VitalSignsClient client = new VitalSignsClient();

        if ("search".equals(action)) {
            FhirUtil.printResource(client.searchObservation(args.length < 2 ? null : args[1], args.length < 3 ? null : args[2]));
            return;
        }

        FhirUtil.printResource(client.getObservation(args.length == 2 ? args[1] : "4"));
    }

    VitalSignsClient() {
        client = FhirUtil.createClient();
    }

    Observation getObservation(String id) {
        return client.read()
                .resource(Observation.class)
                .withId(id)
                .execute();
    }

    Bundle searchObservation(String patientId, String deviceName) {
        return client.search()
                .forResource(Observation.class)
                .where(Patient.IDENTIFIER.exactly().identifier(patientId == null ? "1234" : patientId))
                .where(Device.DEVICE_NAME.matches().value(deviceName == null ? "deviceName" : deviceName))
                .returnBundle(Bundle.class)
                .execute();
    }

}
