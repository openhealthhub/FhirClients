package appointment

import (
	"github.com/google/fhir/go/jsonformat"
	"github.com/google/fhir/go/proto/google/fhir/proto/r4/core/resources/appointment_go_proto"
	r4pb "github.com/google/fhir/go/proto/google/fhir/proto/r4/core/resources/bundle_and_contained_resource_go_proto"
	"openhealthhub.com/go/client"
	"openhealthhub.com/go/config"
	"os"
)

const RESOURCE = "Appointment"

func Read() (*appointment_go_proto.Appointment, error) {
	bytes, err := client.Read(RESOURCE + "/1")
	if err != nil {
		return nil, err
	}

	return toAppointment(bytes)
}

func toAppointment(bytes []byte) (*appointment_go_proto.Appointment, error) {
	unmarshaller, err := jsonformat.NewUnmarshaller(config.TimeZone, config.FhirVersion)
	if err != nil {
		return nil, err
	}

	unmarshal, err := unmarshaller.Unmarshal(bytes)
	if err != nil {
		return nil, err
	}

	appointment := unmarshal.(*r4pb.ContainedResource).GetAppointment()
	return appointment, nil
}

func Create() (*appointment_go_proto.Appointment, error) {
	file, err := os.Open("appointment/appointment.json")
	if err != nil {
		return nil, err
	}

	created, err := client.Create(RESOURCE, file)
	if err != nil {
		return nil, err
	}
	return toAppointment(created)
}
