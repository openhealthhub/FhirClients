package main

import (
	"openhealthhub.com/go/appointment"
	"openhealthhub.com/go/vitalsigns"
)

func main() {
	appointmentCalls()

	observationCalls()

}

func observationCalls() {
	observation, err := vitalsigns.Read()
	if err != nil {
		panic(err)
	}
	print(observation.Id.Value)

	obs, err := vitalsigns.Search()
	if err != nil {
		panic(err)
	}

	for _, o := range obs {
		println(o.Id.Value)
	}
}

func appointmentCalls() {
	apt, err := appointment.Read()
	if err != nil {
		panic(err)
	}
	print(apt.Description.Value)

	apt, err = appointment.Create()
	if err != nil {
		panic(err)
	}
	print(apt.Description.Value)
}
