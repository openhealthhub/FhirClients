package subscription

import (
	"github.com/google/fhir/go/proto/google/fhir/proto/r4/core/resources/subscription_go_proto"
	"openhealthhub.com/go/client"
	"os"
)

const resource = "Subscription"

func Create() (*subscription_go_proto.Subscription, error) {
	file, err := os.Open("subscription/subscription.json")
	if err != nil {
		return nil, err
	}
	containedResource, err := client.Create(resource, file)
	if err != nil {
		return nil, err
	}

	return containedResource.GetSubscription(), nil
}
