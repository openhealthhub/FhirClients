package main

import (
	"github.com/google/fhir/go/proto/google/fhir/proto/r4/core/datatypes_go_proto"
	"openhealthhub.com/go/appointment"
	"openhealthhub.com/go/openpgp"
	"openhealthhub.com/go/questionnaire"
	"openhealthhub.com/go/questionnaireresponse"
	"openhealthhub.com/go/subscription"
	"openhealthhub.com/go/vitalsigns"
)

const encryptedExtensionUrl = "http://openhealthhub.com/StructureDefinition/encryptedAnswers"

func main() {
	appointmentCalls()

	observationCalls()

	questionnaireCalls()

	questionnaireResponseCalls()

	subscriptionCalls()
}

func subscriptionCalls() {
	create, err := subscription.Create()
	if err != nil {
		panic(err)
	}

	println(create.Criteria.Value)
}

func questionnaireResponseCalls() {
	read, err := questionnaireresponse.Read()
	if err != nil {
		panic(err)
	}

	println(read.Id.Value)

	search, err := questionnaireresponse.Search()
	if err != nil {
		panic(err)
	}
	for _, qr := range search {
		println(qr.Id.Value)
	}

	var extension *datatypes_go_proto.Extension
	for _, ext := range read.Extension {
		if ext.Url.Value == encryptedExtensionUrl {
			extension = ext
			break
		}
	}
	decrypt, err := openpgp.Decrypt(extension.GetValue().GetStringValue().Value)
	if err != nil {
		panic(err)
	}
	print(decrypt)
}

func questionnaireCalls() {
	read, err := questionnaire.Read()
	if err != nil {
		panic(err)
	}

	println(read.Description)
}

func observationCalls() {
	observation, err := vitalsigns.Read()
	if err != nil {
		panic(err)
	}
	println(observation.Id.Value)

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
	println(apt.Description.Value)

	apt, err = appointment.Create()
	if err != nil {
		panic(err)
	}
	println(apt.Description.Value)
}
