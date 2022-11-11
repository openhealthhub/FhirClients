package com.openhealthhub.questionnaireresponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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
        String value = questionnaireResponse.getExtensionByUrl("https://api.openhealthhub.com/OpenHealthhub/fhir/4/StructureDefinition/encryptedAnswers")
                .getValue()
                .primitiveValue();
        String decryptedValue = decryptValue(value);
        JsonObject json = com.google.gson.JsonParser.parseString(decryptedValue).getAsJsonObject();
        List<QuestionnaireResponse.QuestionnaireResponseItemComponent> items = questionnaireResponse.getItem();
        json.entrySet().forEach(entry -> addToResponse(items, entry.getKey(), entry.getValue().getAsJsonArray()));
    }

    private void addToResponse(List<QuestionnaireResponse.QuestionnaireResponseItemComponent> items, String key, JsonArray values) {
        QuestionnaireResponse.QuestionnaireResponseItemComponent item = getNestedItems(items)
                .filter(i -> i.getLinkId().equals(key))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("no question response with key " + key + " found"));
        List<QuestionnaireResponse.QuestionnaireResponseItemAnswerComponent> answers = item.getAnswer();

        for (int i = 0; i < values.size(); i++) {
            JsonObject answerObj = values.get(i).getAsJsonObject();
            setAnswer(answers.get(i), getAsString(answerObj, "value"));
            setAnswerCode(item, answerObj.get("codes").getAsJsonArray());
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

    private static void setAnswerCode(QuestionnaireResponse.QuestionnaireResponseItemComponent item, JsonArray codes) {
        codes.forEach(code -> {
            JsonObject codeObj = code.getAsJsonObject();
            item.addAnswer().setValue(new Coding()
                    .setCode(getAsString(codeObj, "code"))
                    .setSystem(getAsString(codeObj, "system"))
                    .setVersion(getAsString(codeObj, "version"))
                    .setDisplay(getAsString(codeObj, "display")));
        });
    }

    private static String getAsString(JsonObject codeObj, String prop) {
        JsonElement elem = codeObj.get(prop);
        return elem == null ? null : elem.getAsString();
    }

}
