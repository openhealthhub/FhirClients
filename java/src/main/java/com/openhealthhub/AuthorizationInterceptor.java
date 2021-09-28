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

import static com.openhealthhub.util.AuthorizationUtil.getToken;
import static java.net.http.HttpRequest.BodyPublishers.ofString;

public class AuthorizationInterceptor implements IClientInterceptor {

    @Override
    public void interceptRequest(IHttpRequest theRequest) {
        String token = getToken();
        theRequest.addHeader(Constants.HEADER_AUTHORIZATION, (Constants.HEADER_AUTHORIZATION_VALPREFIX_BEARER + token));
    }

    @Override
    public void interceptResponse(IHttpResponse theResponse) {
        // nothing to intercept
    }

}
