package vitalsigns

import (
	"fmt"
	"github.com/google/fhir/go/proto/google/fhir/proto/r4/core/resources/observation_go_proto"
	"openhealthhub.com/go/client"
)

const resource = "Observation"

func Read() (*observation_go_proto.Observation, error) {
	containedResource, err := client.Read(fmt.Sprintf("/%s/1", resource))
	if err != nil {
		return nil, err
	}

	return containedResource.GetObservation(), nil
}

func Search() ([]*observation_go_proto.Observation, error) {
	bundle, err := client.Search(resource, "identifier=patient-number", "device-name=deviceName")
	if err != nil {
	}

	observations := bundle.GetBundle().GetEntry()

	result := make([]*observation_go_proto.Observation, 0)
	for _, obs := range observations {
		if obs.GetResource().GetObservation() != nil {
			result = append(result, obs.GetResource().GetObservation())
		}
	}
	return result, nil
}
