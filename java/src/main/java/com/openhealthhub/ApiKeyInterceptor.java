package com.openhealthhub;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.client.api.IHttpRequest;

@Interceptor
public class ApiKeyInterceptor {
    public static final String API_KEY_HEADER_NAME = "x-api-key";
    public static final String API_KEY = "ad880601-b7e6-4d86-901d-b6fca96fc725";

    @Hook(Pointcut.CLIENT_REQUEST)
    public void addApiKey(IHttpRequest request) {
        request.addHeader(API_KEY_HEADER_NAME, API_KEY);
    }
}
