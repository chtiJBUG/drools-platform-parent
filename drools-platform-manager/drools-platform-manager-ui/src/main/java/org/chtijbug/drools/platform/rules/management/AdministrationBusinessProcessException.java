package org.chtijbug.drools.platform.rules.management;

public class AdministrationBusinessProcessException extends RuntimeException {
    private BusinessProcessError error;

    public AdministrationBusinessProcessException(BusinessProcessError error, String message) {
        super(message);
        this.error = error;
    }

    public BusinessProcessError getError() {
        return error;
    }
}
