package com.openhealthhub.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.r4.formats.JsonCreatorDirect;
import org.hl7.fhir.r4.formats.JsonParser;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Resource;

import java.io.StringWriter;

public class FhirUtil {

    private static final String FHIR_ENDPOINT = "https://api-sandbox-staging.openhealthhub.com/OpenHealthhub/fhir/4";

    public static IGenericClient createClient() {
        FhirContext ctx = FhirContext.forR4();
        return ctx.newRestfulGenericClient(FHIR_ENDPOINT);
    }

    public static void printResource(Resource resource) {
        try {
            StringWriter writer = new StringWriter();
            JsonCreatorDirect jsonCreator = new JsonCreatorDirect(writer);
            jsonCreator.setIndent("\t");
            new JsonParser().compose(jsonCreator, resource);
            System.out.println(writer.toString());
        } catch (Exception e) {
            System.out.println("failed to print resource " + resource);
        }
    }

    public static void printResource(MethodOutcome outcome) {
        try {
            StringWriter writer = new StringWriter();
            JsonCreatorDirect jsonCreator = new JsonCreatorDirect(writer);
            jsonCreator.setIndent("\t");
            Resource resource = (Resource) outcome.getResource();
            System.out.println(outcome.getId());
            if (resource != null) {
                new JsonParser().compose(jsonCreator, resource);
            }
            Resource operationOutcome = (Resource) outcome.getOperationOutcome();
            if (operationOutcome != null) {
                new JsonParser().compose(jsonCreator, operationOutcome);
            }
            System.out.println(writer.toString());
        } catch (Exception e) {
            System.out.println("failed to print outcome " + outcome);
            e.printStackTrace();
        }
    }

    public static void printResource(Bundle bundle) {
        System.out.println("Bundle with " + bundle.getEntry().size() + " resources");
        bundle.getEntry().forEach(entry -> printResource(entry.getResource()));
    }
}
