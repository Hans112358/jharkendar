package org.jharkendar.rest;

import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.METHOD_NOT_ALLOWED;

public class MethodNotAllowedResponse {

    private MethodNotAllowedResponse() {

    }

    public static Response get(String message) {
        return Response
                .status(METHOD_NOT_ALLOWED)
                .entity(message)
                .build();
    }
}
