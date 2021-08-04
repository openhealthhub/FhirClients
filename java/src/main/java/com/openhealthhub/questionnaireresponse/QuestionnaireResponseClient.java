package com.openhealthhub.questionnaireresponse;

import com.openhealthhub.util.FhirUtil;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.QuestionnaireResponse;

public class QuestionnaireResponseClient {

    public static void main(String... args) {
        QueryResponseDecryptClient client = args.length > 0 && "answer".equals(args[0]) ? new ClientPerAnswerDecryptClient() :
                new ClientAllAnswersDecryptClient();

        String action = args.length == 0 ? "get" : args[1];

        if ("search".equals(action)) {
            client.searchQuestionnaireResponse()
                    .getEntry()
                    .stream()
                    .map(Bundle.BundleEntryComponent::getResource)
                    .map(QuestionnaireResponse.class::cast)
                    .forEach(questionnaireResponse -> handleQuestionnaireResponse(client, questionnaireResponse));
            return;
        }

        QuestionnaireResponse questionnaireResponse = client.getQuestionnaireResponse();
        handleQuestionnaireResponse(client, questionnaireResponse);
    }

    private static void handleQuestionnaireResponse(QueryResponseDecryptClient clientApp, QuestionnaireResponse questionnaireResponse) {
        if (clientApp.isEncryptedQuestionnaireResponse(questionnaireResponse)) {
            clientApp.decryptResponse(questionnaireResponse);
        }
        FhirUtil.printResource(questionnaireResponse);
    }
}
