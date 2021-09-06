package com.openhealthhub.questionnaireresponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.hl7.fhir.r4.model.Attachment;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.PrimitiveType;
import org.hl7.fhir.r4.model.QuestionnaireResponse;
import org.hl7.fhir.r4.model.Type;

import java.util.List;
import java.util.stream.Stream;

public class ClientAllAnswersDecryptClient extends QueryResponseDecryptClient {

    @Override
    public void decryptResponse(QuestionnaireResponse questionnaireResponse) {
        String value = questionnaireResponse.getExtensionByUrl("http://openhealthhub.com/fhir/StructureDefinition/encryptedAnswers")
                .getValue()
                .primitiveValue();
        String decryptedValue = decryptValue(value);
        JsonObject json = com.google.gson.JsonParser.parseString(decryptedValue).getAsJsonObject();
        List<QuestionnaireResponse.QuestionnaireResponseItemComponent> items = questionnaireResponse.getItem();
        json.entrySet().forEach(entry -> addToResponse(items, entry.getKey(), entry.getValue().getAsJsonArray()));
    }

    private void addToResponse(List<QuestionnaireResponse.QuestionnaireResponseItemComponent> items, String key, JsonArray o) {
        List<QuestionnaireResponse.QuestionnaireResponseItemAnswerComponent> answers = getNestedItems(items)
                .filter(item -> item.getLinkId().equals(key))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("no question response with key " + key + " found")).getAnswer();

        for (int i = 0; i < o.size(); i++) {
            setAnswer(answers.get(i), o.get(i).getAsString());

        }
    }

    private Stream<QuestionnaireResponse.QuestionnaireResponseItemComponent> getNestedItems(
            List<QuestionnaireResponse.QuestionnaireResponseItemComponent> items) {
        Stream<QuestionnaireResponse.QuestionnaireResponseItemComponent> nestedItems = items.stream().flatMap(item -> {
            List<QuestionnaireResponse.QuestionnaireResponseItemComponent> nestedItem = item.getItem();
            if (nestedItem.isEmpty()) {
                return Stream.empty();
            }
            return getNestedItems(nestedItem);
        });

        return Stream.concat(items.stream(), nestedItems);
    }

    private static void setAnswer(QuestionnaireResponse.QuestionnaireResponseItemAnswerComponent answer, String answerValue) {
        Type value = answer.getValue();
        if (value instanceof PrimitiveType) {
            ((PrimitiveType<?>) value).fromStringValue(answerValue);
        }

        if (value instanceof Attachment) {
            ((Attachment) value).getDataElement().setValueAsString(answerValue);
        }

        if (value instanceof Coding) {
            ((Coding) value).setCode(answerValue);
        }
    }

}
