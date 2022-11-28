package com.openhealthhub.careteam;

import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import com.openhealthhub.util.FhirUtil;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CareTeam;

public class CareTeamClient {

    private final IGenericClient client;

    public static void main(String... args) {
        String action = args.length == 0 ? "get" : args[0];
        CareTeamClient client = new CareTeamClient();

        if ("getInclude".equals(action)) {
            FhirUtil.printResource(client.getCareTeamWithPractitioners());
        } else {
            FhirUtil.printResource(client.getCareTeam(args.length == 2 ? args[1] : "4"));
        }
    }

    public Bundle getCareTeamWithPractitioners() {
        return client.search()
                .forResource(CareTeam.class)
                .where(IAnyResource.RES_ID.exactly().identifier("4"))
                .include(new Include("CareTeam:" + CareTeam.SP_PARTICIPANT))
                .returnBundle(Bundle.class)
                .execute();
    }

    CareTeamClient() {
        client = FhirUtil.createClient();
    }

    CareTeam getCareTeam(String id) {
        return client.read()
                .resource(CareTeam.class)
                .withId(id)
                .execute();
    }

}
