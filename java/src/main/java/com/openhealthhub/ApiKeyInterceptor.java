package com.openhealthhub;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.client.api.IHttpRequest;

@Interceptor
public class ApiKeyInterceptor {
    private static final String API_KEY_HEADER_NAME = "x-api-key";
    private static final String API_KEY = "90da723b-70f7-400b-8ddb-8574c45bff13";

    @Hook(Pointcut.CLIENT_REQUEST)
    public void addApiKey(IHttpRequest request) {
        request.addHeader(API_KEY_HEADER_NAME, API_KEY);
    }
}
