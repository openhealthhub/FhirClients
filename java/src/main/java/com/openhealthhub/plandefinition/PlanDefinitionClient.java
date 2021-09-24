package com.openhealthhub.plandefinition;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import com.openhealthhub.util.FhirUtil;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.PlanDefinition;

public class PlanDefinitionClient {

    private final IGenericClient client;

    public static void main(String... args) {
        PlanDefinitionClient client = new PlanDefinitionClient();

        String action = args.length == 0 ? "get" : args[1];

        if ("search".equals(action)) {
            FhirUtil.printResource(client.searchPlanDefinition());
            return;
        }

        FhirUtil.printResource(client.getPlanDefinition());
    }

    PlanDefinitionClient() {
        client = FhirUtil.createClient();
    }

    PlanDefinition getPlanDefinition() {
        return client.read()
                .resource(PlanDefinition.class)
                .withId("4944e73f-e447-49ba-a64c-a246b9ef4bdd")
                .execute();
    }

    public Bundle searchPlanDefinition() {
        return client.search()
                .forResource(PlanDefinition.class)
                .where(PlanDefinition.DEFINITION.hasId("Questionnaire/866683f3-c41b-47c0-b42f-86f9ff978d1d"))
                .and(PlanDefinition.PUBLISHER.matches().value("Program Creator"))
                .returnBundle(Bundle.class)
                .execute();
    }

}
