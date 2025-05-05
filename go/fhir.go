package main

import (
	"github.com/google/fhir/go/proto/google/fhir/proto/r4/core/datatypes_go_proto"
	"github.com/google/fhir/go/proto/google/fhir/proto/r4/core/resources/questionnaire_response_go_proto"
	"openhealthhub.com/go/binary"
	"openhealthhub.com/go/careplan"
	"openhealthhub.com/go/careteam"
	"openhealthhub.com/go/openpgp"
	"openhealthhub.com/go/plandefinition"
	"openhealthhub.com/go/practitioner"
	"openhealthhub.com/go/questionnaire"
	"openhealthhub.com/go/questionnaireresponse"
	"openhealthhub.com/go/subscription"
)

const encryptedExtensionUrl = "https://api.openhealthhub.com/OpenHealthhub/fhir/4/StructureDefinition/encryptedAnswers"
const encryptedProfileUrl = "https://api.openhealthhub.com/OpenHealthhub/fhir/4/StructureDefinition/EncryptedQuestionnaireResponse"

func main() {
	carePlanCalls()

	careTeamCalls()

	practitionerCalls()

	uploadKeyCalls()

	plandefinitionCalls()

	questionnaireCalls()

	questionnaireResponseCalls()

	subscriptionCalls()
}

func uploadKeyCalls() {
	status, err := binary.Create()
	if err != nil {
		panic(err)
	}

	println(status)
}

func carePlanCalls() {
	find, err := careplan.Read()
	if err != nil {
		panic(err)
	}

	println(find.InstantiatesCanonical[0])

	search, err := careplan.Search()
	if err != nil {
		panic(err)
	}

	println(search[0].InstantiatesCanonical[0])

	withPractitioners, err := careplan.ReadWithPractitioners()
	if err != nil {
		panic(err)
	}

	println(withPractitioners)

	create, err := careplan.Create()
	if err != nil {
		panic(err)
	}

	println(create.Id.Value)

	updated, err := careplan.Update()
	if err != nil {
		panic(err)
	}

	println(updated.Id.Value)

	err = careplan.Delete()
	if err != nil {
		panic(err)
	}
}

func careTeamCalls() {
	find, err := careteam.Read()
	if err != nil {
		panic(err)
	}

	println(find)

	withPractitioners, err := careteam.ReadWithPractitioners()
	if err != nil {
		panic(err)
	}

	println(withPractitioners)
}

func practitionerCalls() {
	find, err := practitioner.Read()
	if err != nil {
		panic(err)
	}

	println(find)

	practitioners, err := practitioner.SearchByCareTeam()
	if err != nil {
		panic(err)
	}

	println(practitioners)
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

	if isEncrypted(read) {
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
}

func questionnaireCalls() {
	read, err := questionnaire.Read()
	if err != nil {
		panic(err)
	}

	println(read.Description)
}

func plandefinitionCalls() {
	pd, err := plandefinition.Read()
	if err != nil {
		panic(err)
	}
	println(pd.Id.Value)

	pds, err := plandefinition.Search()
	if err != nil {
		panic(err)
	}

	for _, o := range pds {
		println(o.Id.Value)
	}
}

func isEncrypted(qr *questionnaire_response_go_proto.QuestionnaireResponse) bool {
	for _, p := range qr.Meta.Profile {
		if p.GetValue() == encryptedProfileUrl {
			return true
		}
	}
	return false
}
