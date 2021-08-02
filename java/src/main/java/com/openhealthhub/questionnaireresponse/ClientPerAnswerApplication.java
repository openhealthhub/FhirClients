package com.openhealthhub.questionnaireresponse;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.hl7.fhir.r4.formats.JsonCreatorDirect;
import org.hl7.fhir.r4.formats.JsonParser;
import org.hl7.fhir.r4.model.Attachment;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.QuestionnaireResponse;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Type;
import org.pgpainless.PGPainless;
import org.pgpainless.decryption_verification.DecryptionStream;
import org.pgpainless.key.protection.SecretKeyRingProtector;
import org.pgpainless.util.Passphrase;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

public class ClientPerAnswerApplication {

    private static final String FHIR_ENDPOINT = "https://api-sandbox-staging.openhealthhub.com/fhir";

    private static final String PRIVATE_KEY_FILE = "/openpgp/sandbox.key";
    private static final String PRIVATE_KEY_PASSPHRASE = "api-sandbox";

    private static final long QUESTIONNAIRE_RESPONSE_ID = 3L;

    private static PGPSecretKeyRing privateKey;
    private static SecretKeyRingProtector keyRingProtector;

    public static void main(String... args) throws IOException {
        loadPrivateKey();

        IGenericClient client = createFhirCLient();
        QuestionnaireResponse questionnaireResponse = getQuestionnaireResponse(client);
        if (isEncryptedQuestionnaireResponse(questionnaireResponse)) {
            decryptResponse(questionnaireResponse);
        }
        printResponse(questionnaireResponse);
    }

    private static void loadPrivateKey() {
        try {
            privateKey = PGPainless.readKeyRing().secretKeyRing(ClientPerAnswerApplication.class.getResourceAsStream(PRIVATE_KEY_FILE));
            keyRingProtector = SecretKeyRingProtector.unlockAllKeysWith(Passphrase.fromPassword(PRIVATE_KEY_PASSPHRASE), privateKey);
        } catch (PGPException | IOException e) {
            throw new RuntimeException("failed to load private key");
        }
    }

    private static IGenericClient createFhirCLient() {
        FhirContext ctx = FhirContext.forR4();
        return ctx.newRestfulGenericClient(FHIR_ENDPOINT);
    }

    private static QuestionnaireResponse getQuestionnaireResponse(IGenericClient client) {
        return client.read().resource(QuestionnaireResponse.class).withId(QUESTIONNAIRE_RESPONSE_ID).execute();
    }

    private static boolean isEncryptedQuestionnaireResponse(QuestionnaireResponse questionnaireResponse) {
        return questionnaireResponse.getMeta()
                .getProfile()
                .stream()
                .anyMatch(profile -> "http://openhealthhub.com/StructureDefinition/EncryptedQuestionnaireResponse".equals(profile.getValue()));
    }

    private static void decryptResponse(QuestionnaireResponse questionnaireResponse) {
        questionnaireResponse.getItem().parallelStream().forEach(item -> item.getAnswer().parallelStream().forEach(ClientPerAnswerApplication::decryptAnswer));
    }

    private static void printResponse(QuestionnaireResponse questionnaireResponse) throws IOException {
        StringWriter writer = new StringWriter();
        JsonCreatorDirect jsonCreator = new JsonCreatorDirect(writer);
        jsonCreator.setIndent("\t");
        new JsonParser().compose(jsonCreator, questionnaireResponse);
        System.out.println(writer.toString());
    }

    private static void decryptAnswer(QuestionnaireResponse.QuestionnaireResponseItemAnswerComponent answer) {
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

    private static String decryptValue(Type value, String extensionUrl) {
        try {
            DecryptionStream decryptionStream = PGPainless.decryptAndOrVerify()
                    .onInputStream(new ByteArrayInputStream(value.getExtensionByUrl(extensionUrl).getValue().primitiveValue().getBytes(StandardCharsets.UTF_8)))
                    .decryptWith(keyRingProtector, privateKey)
                    .doNotVerify()
                    .build();
            byte[] bytes = decryptionStream.readAllBytes();
            decryptionStream.close();

            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException | PGPException e) {
            throw new InternalErrorException("failed to decrypt value", e);
        }
    }
}
