package com.openhealthhub;

import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.client.api.IClientInterceptor;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static java.net.http.HttpRequest.BodyPublishers.ofString;

public class AuthorizationInterceptor implements IClientInterceptor {

    private static final String CLIENT_SECRET = "915e87d4-16ee-4ca5-b701-b38b6afce8ff";
    private static final String KEYCLOAK_TOKEN_URI =
            "https://auth-staging.openhealthhub.com/auth/realms/OpenHealthHub/protocol/openid-connect/token";
    public static final String CLIENT_ID = "api-sandbox";

    @Override
    public void interceptRequest(IHttpRequest theRequest) {
        String token = getToken();
        theRequest.addHeader(Constants.HEADER_AUTHORIZATION, (Constants.HEADER_AUTHORIZATION_VALPREFIX_BEARER + token));
    }

    private String getToken() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(KEYCLOAK_TOKEN_URI))
                    .POST(ofString("client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "&grant_type=client_credentials"))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .build();

            String response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body();
            JsonObject respAsJson = JsonParser.parseString(response).getAsJsonObject();

            return respAsJson.getAsJsonPrimitive("access_token").getAsString();
        } catch (Exception e) {
            throw new RuntimeException("failed to retrieve access token", e);
        }
    }

    @Override
    public void interceptResponse(IHttpResponse theResponse) {
        // nothing to intercept
    }

}
