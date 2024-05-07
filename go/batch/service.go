package careplan

import (
	"fmt"
	"github.com/google/fhir/go/proto/google/fhir/proto/r4/core/resources/bundle_and_contained_resource_go_proto"
	"github.com/google/fhir/go/proto/google/fhir/proto/r4/core/resources/care_plan_go_proto"
	"openhealthhub.com/go/client"
	"os"
)

const resource = "CarePlan"

func Batch() (*care_plan_go_proto.Bundle, error) {
	bundle, err := os.Open("batch/batch.json")
	if err != nil {
		return nil, err
	}

	marshaller, err := jsonformat.NewMarshaller(jsonformat.STU3, "", "", jsonformat.ParserConfig{})
  	if err != nil {
  		return nil, err
  	}

  	bundleBytes, err := marshaller.MarshalResource(bundle)
  	if err != nil {
  		return nil, err
  	}

  	resp, err := client.Post(, bundleBytes)
  	if err != nil {
  		return nil, err
  	}

  	unmarshalledBundle := bundle_and_contained_resource_go_proto.Bundle{}
  	if err := proto.Unmarshal(resp, unmarshalledBundle); err != nil {
  		return nil, err
  	}

  	return unmarshalledBundle, nil
}

