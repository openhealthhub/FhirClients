package com.openhealthhub.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static java.net.http.HttpRequest.BodyPublishers.ofString;

public class AuthorizationUtil {
    private static final String CLIENT_SECRET = "95810e52-4307-41f5-99a4-d873ab63b536";
    private static final String KEYCLOAK_TOKEN_URI =
            "https://auth.openhealthhub.com/auth/realms/OpenHealthHub/protocol/openid-connect/token";
    public static final String CLIENT_ID = "api-sandbox";

    public static String getToken() {
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
}
