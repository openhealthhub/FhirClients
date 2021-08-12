package questionnaireresponse

import (
	"fmt"
	qrproto "github.com/google/fhir/go/proto/google/fhir/proto/r4/core/resources/questionnaire_response_go_proto"
	"openhealthhub.com/go/client"
)

const resource = "QuestionnaireResponse"

func Read() (*qrproto.QuestionnaireResponse, error) {
	containedResource, err := client.Read(fmt.Sprintf("/%s/1", resource))
	if err != nil {
		return nil, err
	}

	return containedResource.GetQuestionnaireResponse(), nil
}

func Search() ([]*qrproto.QuestionnaireResponse, error) {
	containedResource, err := client.Search(resource, "part-of=programUUID", "identifier=moduleUUID")
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
