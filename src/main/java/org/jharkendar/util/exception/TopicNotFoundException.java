package org.jharkendar.util.exception;

public class TopicNotFoundException extends EntityNotFoundException {

    public TopicNotFoundException(String id) {
        super(id);
    }

    @Override
    String getType() {
        return "Topic";
    }
}
