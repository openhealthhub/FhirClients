package com.openhealthhub.questionnaireresponse;

import org.hl7.fhir.r4.model.Attachment;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.QuestionnaireResponse;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Type;

public class ClientPerAnswerDecryptClient extends QueryResponseDecryptClient {

    @Override
    void decryptResponse(QuestionnaireResponse questionnaireResponse) {
        questionnaireResponse.getItem().parallelStream().forEach(this::decryptItem);
    }

    private void decryptItem(QuestionnaireResponse.QuestionnaireResponseItemComponent item) {
        item.getAnswer().parallelStream().forEach(this::decryptAnswer);
        item.getItem().parallelStream().forEach(this::decryptItem);
    }

    private void decryptAnswer(QuestionnaireResponse.QuestionnaireResponseItemAnswerComponent answer) {
        Type value = answer.getValue();
        if (value instanceof DecimalType) {
            String decrpytedValue = decryptValue(value, "http://openhealthhub.com/fhir/StructureDefinition/encrypted-decimalType");
            ((DecimalType) value).fromStringValue(decrpytedValue);
        }

        if (value instanceof StringType) {
            String decrpytedValue = decryptValue(value, "http://openhealthhub.com/fhir/StructureDefinition/encrypted-stringType");
            ((StringType) value).setValue(decrpytedValue);
        }

        if (value instanceof DateType) {
            String decrpytedValue = decryptValue(value, "http://openhealthhub.com/fhir/StructureDefinition/encrypted-dateType");
            ((DateType) value).fromStringValue(decrpytedValue);
        }

        if (value instanceof Attachment) {
            String decrpytedValue = decryptValue(value, "http://openhealthhub.com/fhir/StructureDefinition/encrypted-attachment");
            ((Attachment) value).getDataElement().setValueAsString(decrpytedValue);
        }

        if (value instanceof Coding) {
            String decrpytedValue = decryptValue(value, "http://openhealthhub.com/fhir/StructureDefinition/encrypted-coding");
            ((Coding) value).setCode(decrpytedValue);
        }
    }

    private String decryptValue(Type value, String extensionUrl) {
        return decryptValue(value.getExtensionByUrl(extensionUrl).getValue().primitiveValue());
    }
}
