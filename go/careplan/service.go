package careplan

import (
	"fmt"
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
