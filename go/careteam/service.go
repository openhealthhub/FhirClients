package careteam

import (
	"fmt"
	"github.com/google/fhir/go/proto/google/fhir/proto/r4/core/resources/bundle_and_contained_resource_go_proto"
	"github.com/google/fhir/go/proto/google/fhir/proto/r4/core/resources/care_team_go_proto"
	"openhealthhub.com/go/client"
)

const resource = "CareTeam"

func Read() (*care_team_go_proto.CareTeam, error) {
	containedResource, err := client.Read(fmt.Sprintf("/%s/1", resource))
	if err != nil {
		return nil, err
	}

	return containedResource.GetCareTeam(), nil
}

func ReadWithPractitioners() ([]*bundle_and_contained_resource_go_proto.Bundle_Entry, error) {
	containedResource, err := client.Search(fmt.Sprintf("/%s", resource), "_id=1", "_include=CareTeam:participant")
	if err != nil {
		return nil, err
	}

	responses := containedResource.GetBundle().GetEntry()
	return responses, nil
}
