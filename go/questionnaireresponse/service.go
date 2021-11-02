package questionnaireresponse

import (
	"fmt"
	qrproto "github.com/google/fhir/go/proto/google/fhir/proto/r4/core/resources/questionnaire_response_go_proto"
	"openhealthhub.com/go/client"
)

const resource = "QuestionnaireResponse"

func Read() (*qrproto.QuestionnaireResponse, error) {
	containedResource, err := client.Read(fmt.Sprintf("/%s/57a1f708-d9cf-4d8c-9f25-b5a450e7f0ca", resource))
	if err != nil {
		return nil, err
	}

	return containedResource.GetQuestionnaireResponse(), nil
}

func Search() ([]*qrproto.QuestionnaireResponse, error) {
	containedResource, err := client.Search(resource, "based-on.instantiates-canonical=CarePlan/97f680b9-e397-4298-8c53-de62a284c806")
	if err != nil {
		return nil, err
	}

	responses := containedResource.GetBundle().GetEntry()
	result := make([]*qrproto.QuestionnaireResponse, len(responses))
	for idx, entry := range responses {
		result[idx] = entry.GetResource().GetQuestionnaireResponse()
	}
	return result, nil
}
