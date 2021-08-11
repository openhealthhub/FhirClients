package appointment

import (
	"errors"
	"github.com/google/fhir/go/fhirversion"
	"github.com/google/fhir/go/jsonformat"
	"github.com/google/fhir/go/proto/google/fhir/proto/r4/core/resources/appointment_go_proto"
	r4pb "github.com/google/fhir/go/proto/google/fhir/proto/r4/core/resources/bundle_and_contained_resource_go_proto"
	"openhealthhub.com/go/client"
	"openhealthhub.com/go/config"
)

const RESOURCE = "Appointment"

func Read() (*appointment_go_proto.Appointment, error) {
	bytes, err := client.Read(RESOURCE + "/1")
	if err != nil {
		return nil, err
	}

	unmarshaller, err := jsonformat.NewUnmarshaller(config.TimeZone, fhirversion.R4)
	if err != nil {
		return nil, err
	}

	unmarshal, err := unmarshaller.Unmarshal(bytes)
	if err != nil {
		return nil, err
	}

	appointment := unmarshal.(*r4pb.ContainedResource).GetAppointment()

	if appointment == nil {
		return nil, errors.New("no appointment found")
	}
	return appointment, nil
}
