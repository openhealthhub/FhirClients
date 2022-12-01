package careplan

import (
	"fmt"
	"github.com/google/fhir/go/proto/google/fhir/proto/r4/core/resources/bundle_and_contained_resource_go_proto"
	"github.com/google/fhir/go/proto/google/fhir/proto/r4/core/resources/care_plan_go_proto"
	"openhealthhub.com/go/client"
	"os"
)

const resource = "CarePlan"

func Read() (*care_plan_go_proto.CarePlan, error) {
	containedResource, err := client.Read(fmt.Sprintf("/%s/1", resource))
	if err != nil {
		return nil, err
	}

	return containedResource.GetCarePlan(), nil
}

func Search() ([]*care_plan_go_proto.CarePlan, error) {
	containedResource, err := client.Search(fmt.Sprintf("/%s", resource), "instantiates-canonical=PlanDefinition/af1080ea-5f92-43cc-9d6e-653b6e0f5074", "patient.identifier=1234")
	if err != nil {
		return nil, err
	}

	responses := containedResource.GetBundle().GetEntry()
	result := make([]*care_plan_go_proto.CarePlan, len(responses))
	for idx, entry := range responses {
		result[idx] = entry.GetResource().GetCarePlan()
	}
	return result, nil
}

func ReadWithPractitioners() ([]*bundle_and_contained_resource_go_proto.Bundle_Entry, error) {
	containedResource, err := client.Search(fmt.Sprintf("/%s", resource), "_id=1", "_include=CareTeam:participant", "_include=CarePlan:care-team")
	if err != nil {
		return nil, err
	}

	responses := containedResource.GetBundle().GetEntry()
	return responses, nil
}

func Create() (*care_plan_go_proto.CarePlan, error) {
	file, err := os.Open("careplan/careplan.json")
	if err != nil {
		return nil, err
	}

	created, err := client.Create(resource, file)
	if err != nil {
		return nil, err
	}
	return created.GetCarePlan(), nil
}

func Update() (*care_plan_go_proto.CarePlan, error) {
	file, err := os.Open("careplan/careplan.json")
	if err != nil {
		return nil, err
	}

	updated, err := client.Update(1, resource, file)
	if err != nil {
		return nil, err
	}
	return updated.GetCarePlan(), nil
}

func Delete() error {
	err := client.Delete(fmt.Sprintf("/%s/1"))
	return err
}
