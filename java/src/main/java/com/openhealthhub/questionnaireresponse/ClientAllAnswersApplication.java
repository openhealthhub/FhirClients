package com.openhealthhub.questionnaireresponse;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.openhealthhub.util.FhirUtil;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.hl7.fhir.r4.model.Attachment;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.PrimitiveType;
import org.hl7.fhir.r4.model.QuestionnaireResponse;
import org.hl7.fhir.r4.model.Type;
import org.pgpainless.PGPainless;
import org.pgpainless.decryption_verification.DecryptionStream;
import org.pgpainless.key.protection.SecretKeyRingProtector;
import org.pgpainless.util.Passphrase;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;

public class ClientAllAnswersApplication {

    private static final String PRIVATE_KEY_FILE = "/openpgp/sandbox.key";
    private static final String PRIVATE_KEY_PASSPHRASE = "api-sandbox";

    private static final long QUESTIONNAIRE_RESPONSE_ID = 3L;

    private static PGPSecretKeyRing privateKey;
    private static SecretKeyRingProtector keyRingProtector;

    public static void main(String... args) {
        loadPrivateKey();

        IGenericClient client = FhirUtil.createClient();
        QuestionnaireResponse questionnaireResponse = getQuestionnaireResponse(client);
        if (isEncryptedQuestionnaireResponse(questionnaireResponse)) {
            decryptResponse(questionnaireResponse);
        }
        FhirUtil.printResource(questionnaireResponse);
    }

    private static void loadPrivateKey() {
        try {
            InputStream resourceAsStream = ClientAllAnswersApplication.class.getResourceAsStream(PRIVATE_KEY_FILE);
            privateKey = PGPainless.readKeyRing().secretKeyRing(resourceAsStream);
            keyRingProtector = SecretKeyRingProtector.unlockAllKeysWith(Passphrase.fromPassword(PRIVATE_KEY_PASSPHRASE), privateKey);
        } catch (PGPException | IOException e) {
            throw new RuntimeException("failed to load private key");
        }
    }

    private static QuestionnaireResponse getQuestionnaireResponse(IGenericClient client) {
        return client.read().resource(QuestionnaireResponse.class).withId(QUESTIONNAIRE_RESPONSE_ID).execute();
    }

    private static boolean isEncryptedQuestionnaireResponse(QuestionnaireResponse questionnaireResponse) {
        return questionnaireResponse.getMeta()
                .getProfile()
                .stream()
                .anyMatch(profile -> "http://openhealthhub.com/StructureDefinition/EncryptedQuestionnaireResponse".equals(
                        profile.getValue()));
    }

    private static void decryptResponse(QuestionnaireResponse questionnaireResponse) {
        String value = questionnaireResponse.getExtensionByUrl("http://openhealthhub.com/StructureDefinition/encryptedAnswers")
                .getValue()
                .primitiveValue();
        String decryptedValue = decryptValue(value);
        JsonObject json = com.google.gson.JsonParser.parseString(decryptedValue).getAsJsonObject();
        List<QuestionnaireResponse.QuestionnaireResponseItemComponent> items = questionnaireResponse.getItem();
        json.entrySet().forEach(entry -> addToResponse(items, entry.getKey(), entry.getValue().getAsJsonArray()));
    }

    private static void addToResponse(List<QuestionnaireResponse.QuestionnaireResponseItemComponent> items, String key, JsonArray o) {
        List<QuestionnaireResponse.QuestionnaireResponseItemAnswerComponent> answers = getNestedItems(items)
                .filter(item -> item.getLinkId().equals(key))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("no question response with key " + key + " found")).getAnswer();

        for (int i = 0; i < o.size(); i++) {
            setAnswer(answers.get(i), o.get(i).getAsString());

        }
    }

    private static Stream<QuestionnaireResponse.QuestionnaireResponseItemComponent> getNestedItems(
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

    private static String decryptValue(String stringValue) {
        try {
            DecryptionStream decryptionStream = PGPainless.decryptAndOrVerify()
                    .onInputStream(new ByteArrayInputStream(stringValue.getBytes(StandardCharsets.UTF_8)))
                    .decryptWith(keyRingProtector, privateKey)
                    .doNotVerify()
                    .build();
            byte[] bytes = decryptionStream.readAllBytes();
            decryptionStream.close();

            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException | PGPException e) {
            throw new RuntimeException("failed to decrypt value", e);
        }
    }
}
