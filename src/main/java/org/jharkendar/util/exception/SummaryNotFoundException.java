package org.jharkendar.util.exception;

public class SummaryNotFoundException extends EntityNotFoundException {

    public SummaryNotFoundException(String id) {
        super(id);
    }

    @Override
    String getType() {
        return "Summary";
    }
}
