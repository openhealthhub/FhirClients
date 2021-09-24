package com.openhealthhub.questionnaireresponse;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import com.openhealthhub.util.FhirUtil;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.QuestionnaireResponse;
import org.pgpainless.PGPainless;
import org.pgpainless.decryption_verification.DecryptionStream;
import org.pgpainless.key.protection.SecretKeyRingProtector;
import org.pgpainless.util.Passphrase;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public abstract class QueryResponseDecryptClient {
    private static final String PRIVATE_KEY_FILE = "/sandbox.key";
    private static final String PRIVATE_KEY_PASSPHRASE = "api-sandbox";

    private static final String QUESTIONNAIRE_RESPONSE_ID = "57a1f708-d9cf-4d8c-9f25-b5a450e7f0ca";

    protected final IGenericClient client;

    private PGPSecretKeyRing privateKey;
    private SecretKeyRingProtector keyRingProtector;

    protected QueryResponseDecryptClient() {
        loadPrivateKey();
        client = FhirUtil.createClient();
    }

    protected void loadPrivateKey() {
        try {
            InputStream resourceAsStream = QueryResponseDecryptClient.class.getResourceAsStream(PRIVATE_KEY_FILE);
            privateKey = PGPainless.readKeyRing().secretKeyRing(resourceAsStream);
            keyRingProtector = SecretKeyRingProtector.unlockAllKeysWith(
                    Passphrase.fromPassword(PRIVATE_KEY_PASSPHRASE), privateKey);
        } catch (PGPException | IOException e) {
            throw new RuntimeException("failed to load private key");
        }
    }

    public QuestionnaireResponse getQuestionnaireResponse() {
        return client.read()
                .resource(QuestionnaireResponse.class)
                .withId(QUESTIONNAIRE_RESPONSE_ID)
                .execute();
    }

    public Bundle searchQuestionnaireResponse() {
        return client.search()
                .forResource(QuestionnaireResponse.class)
                .where(QuestionnaireResponse.BASED_ON.hasChainedProperty(CarePlan.INSTANTIATES_CANONICAL.hasId("PlanDefinition/97f680b9-e397-4298-8c53-de62a284c806")))
                .returnBundle(Bundle.class)
                .execute();
    }

    public boolean isEncryptedQuestionnaireResponse(QuestionnaireResponse questionnaireResponse) {
        return questionnaireResponse.getMeta()
                .getProfile()
                .stream()
                .anyMatch(profile -> "http://openhealthhub.com/fhir/StructureDefinition/EncryptedQuestionnaireResponse".equals(
                        profile.getValue()));
    }

    abstract void decryptResponse(QuestionnaireResponse questionnaireResponse);

    protected String decryptValue(String stringValue) {
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
