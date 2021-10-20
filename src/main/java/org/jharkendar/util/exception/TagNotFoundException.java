package org.jharkendar.util.exception;

public class TagNotFoundException extends EntityNotFoundException {

    public TagNotFoundException(String id) {
        super(id);
    }

    @Override
    String getType() {
        return "Tag";
    }
}
