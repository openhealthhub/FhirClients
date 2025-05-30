<?php

namespace OpenHealthhub\PhpFhirClient;


use DCarbone\PHPFHIRGenerated\R4\FHIRElement\FHIRBackboneElement\FHIRSubscription\FHIRSubscriptionChannel;
use DCarbone\PHPFHIRGenerated\R4\FHIRElement\FHIRSubscriptionChannelType;
use DCarbone\PHPFHIRGenerated\R4\FHIRElement\FHIRSubscriptionStatus;
use DCarbone\PHPFHIRGenerated\R4\FHIRResource\FHIRDomainResource\FHIRSubscription;

class SubscriptionClient
{
    public function createSubscription(): FHIRSubscription
    {
        $client = new FhirClient();
        $subscription = new FHIRSubscription();
        $channel = new FHIRSubscriptionChannel();
        $channelType = new FHIRSubscriptionChannelType();
        $subscription->setCriteria('QuestionnaireResponse?author=johndoe')->setChannel($channel->setEndpoint('https://your-webhook/endpoint')->setType($channelType->setValue('rest-hook'))->addHeader(['Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8']));
        $status = new FHIRSubscriptionStatus(['requested']);
        $subscription->setStatus($status);
        $res = $client->create('Subscription', $subscription);
        return new FHIRSubscription($res);
    }
}
