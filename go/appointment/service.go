package appointment

import (
	"fmt"
	"github.com/google/fhir/go/proto/google/fhir/proto/r4/core/resources/appointment_go_proto"
	"openhealthhub.com/go/client"
	"os"
)

const resource = "Appointment"

func Read() (*appointment_go_proto.Appointment, error) {
	containedResource, err := client.Read(fmt.Sprintf("/%s/1", resource))
	if err != nil {
		return nil, err
	}

	return containedResource.GetAppointment(), nil
}

func Create() (*appointment_go_proto.Appointment, error) {
	file, err := os.Open("appointment/appointment.json")
	if err != nil {
		return nil, err
	}

	created, err := client.Create(resource, file)
	if err != nil {
		return nil, err
	}
	return created.GetAppointment(), nil
}
