package questionnaire

import (
	"fmt"
	"github.com/google/fhir/go/proto/google/fhir/proto/r4/core/resources/questionnaire_go_proto"
	"openhealthhub.com/go/client"
)

const resource = "Questionnaire"

func Read() (*questionnaire_go_proto.Questionnaire, error) {
	containedResource, err := client.Read(fmt.Sprintf("/%s/1", resource))
	if err != nil {
		return nil, err
	}

	return containedResource.GetQuestionnaire(), nil
}
