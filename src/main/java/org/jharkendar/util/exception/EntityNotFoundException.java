package org.jharkendar.util.exception;

import lombok.RequiredArgsConstructor;

import javax.ws.rs.NotFoundException;

@RequiredArgsConstructor
public abstract class EntityNotFoundException extends NotFoundException {

    private final String id;

    abstract String getType();

    public String getMessage() {
        return "No " + getType() + " found for id " + id;
    }

}
