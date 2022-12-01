package com.openhealthhub.practitioner;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import com.openhealthhub.util.FhirUtil;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Practitioner;

import static com.openhealthhub.util.FhirUtil.FHIR_ENDPOINT;

public class PractitionerClient {

    private final IGenericClient client;

    public static void main(String... args) {
        String action = args.length == 0 ? "get" : args[0];
        PractitionerClient client = new PractitionerClient();

        if ("getInclude".equals(action)) {
            FhirUtil.printResource(client.searchPractitionersByCareTeamId());
        } else {
            FhirUtil.printResource(client.getPractitioner(args.length == 2 ? args[1] : "4"));
        }
    }

    public Bundle searchPractitionersByCareTeamId() {
        return client.search()
                .byUrl(String.format("Practitioner?_has:CareTeam:_id=%s", 4))
                .returnBundle(Bundle.class)
                .execute();
    }

    PractitionerClient() {
        client = FhirUtil.createClient();
    }

    Practitioner getPractitioner(String id) {
        return client.read()
                .resource(Practitioner.class)
                .withId(id)
                .execute();
    }

}
