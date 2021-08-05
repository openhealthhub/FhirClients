package com.openhealthhub.questionnaire;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import com.openhealthhub.util.FhirUtil;
import org.hl7.fhir.r4.model.Questionnaire;

public class QuestionnaireClient {

    private final IGenericClient client;

    public static void main(String... args) {
        QuestionnaireClient client = new QuestionnaireClient();

        FhirUtil.printResource(client.getQuestionnaire(args.length == 1 ? args[0] : "4"));
    }

    QuestionnaireClient() {
        client = FhirUtil.createClient();
    }

    Questionnaire getQuestionnaire(String id) {
        return client.read()
                .resource(Questionnaire.class)
                .withId(id)
                .execute();
    }

}
