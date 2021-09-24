package com.openhealthhub.binary;

import com.openhealthhub.util.FhirUtil;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

import static com.openhealthhub.ApiKeyInterceptor.API_KEY;
import static com.openhealthhub.ApiKeyInterceptor.API_KEY_HEADER_NAME;
import static com.openhealthhub.util.AuthorizationUtil.getToken;
import static java.net.http.HttpRequest.BodyPublishers.ofString;

public class KeyUploadClient {

    public static void main(String... args) throws IOException, URISyntaxException, InterruptedException {
        String publicKey = Base64.getEncoder().encodeToString(IOUtils.resourceToByteArray("/sandbox.pub"));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(FhirUtil.FHIR_ENDPOINT + "/Binary"))
                .POST(ofString(publicKey))
                .header("Content-Type", "text/plain")
                .header("Authorization", "Bearer " + getToken())
                .header(API_KEY_HEADER_NAME, API_KEY)
                .build();

        HttpResponse<Void> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.discarding());

        System.out.println("response status code " + response.statusCode());
    }
}
