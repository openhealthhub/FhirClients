package com.openhealthhub.subscription;

import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import com.openhealthhub.util.FhirUtil;
import org.hl7.fhir.r4.model.Subscription;

public class SubscriptionClient {

    private final IGenericClient client;

    public static void main(String... args) {
        SubscriptionClient client = new SubscriptionClient();

        FhirUtil.printResource(client.createSubscription());
    }

    SubscriptionClient() {
        client = FhirUtil.createClient();
    }

    MethodOutcome createSubscription() {
        Subscription.SubscriptionChannelComponent channel = new Subscription.SubscriptionChannelComponent()
                .setType(Subscription.SubscriptionChannelType.RESTHOOK)
                .setEndpoint("https://your-webhook/endpoint");
        Subscription subscription = new Subscription()
                .setCriteria("Appointment?name=test")
                .setChannel(channel);

        return client.create()
                .resource(subscription)
                .execute();

    }
}
