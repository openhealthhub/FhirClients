package plandefinition

import (
	"fmt"
	qrproto "github.com/google/fhir/go/proto/google/fhir/proto/r4/core/resources/questionnaire_response_go_proto"
	"openhealthhub.com/go/client"
)

const resource = "PlanDefinition"

func Read() (*qrproto.PlanDefinition, error) {
	containedResource, err := client.Read(fmt.Sprintf("/%s/4944e73f-e447-49ba-a64c-a246b9ef4bdd", resource))
	if err != nil {
		return nil, err
	}

	return containedResource.GetPlanDefinition(), nil
}

func Search() ([]*qrproto.PlanDefinition, error) {
	containedResource, err := client.Search(resource, "definition=Questionnaire/866683f3-c41b-47c0-b42f-86f9ff978d1d", "publisher=Program Creator")
	if err != nil {
		return nil, err
	}

	responses := containedResource.GetBundle().GetEntry()
	result := make([]*qrproto.PlanDefinition, len(responses))
	for idx, entry := range responses {
		result[idx] = entry.GetResource().GetPlanDefinition()
	}
	return result, nil
}
