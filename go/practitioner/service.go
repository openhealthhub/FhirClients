package practitioner

import (
	"fmt"
	"github.com/google/fhir/go/proto/google/fhir/proto/r4/core/resources/bundle_and_contained_resource_go_proto"
	"github.com/google/fhir/go/proto/google/fhir/proto/r4/core/resources/practitioner_go_proto"
	"openhealthhub.com/go/client"
)

const resource = "Practitioner"

func Read() (*practitioner_go_proto.Practitioner, error) {
	containedResource, err := client.Read(fmt.Sprintf("/%s/1", resource))
	if err != nil {
		return nil, err
	}

	return containedResource.GetPractitioner(), nil
}

func SearchByCareTeam() ([]*bundle_and_contained_resource_go_proto.Bundle_Entry, error) {
	containedResource, err := client.Search(fmt.Sprintf("/%s", resource), "_has:CareTeam:_id=1")
	if err != nil {
		return nil, err
	}

	responses := containedResource.GetBundle().GetEntry()
	return responses, nil
}
