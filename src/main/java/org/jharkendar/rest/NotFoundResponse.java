package org.jharkendar.rest;

import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

public class NotFoundResponse {


    private NotFoundResponse() {

    }

    public static Response get(String message) {
        return Response
                .status(NOT_FOUND)
                .entity(message)
                .build();
    }
}
