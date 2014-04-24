package org.chtijbug.drools.platform.rules.management;

public class AdministrationBusinessProcessException extends RuntimeException {
    /** Related enum ERROR Code */
    private BusinessProcessError error;

    public AdministrationBusinessProcessException(BusinessProcessError error, String message) {
        super(message);
        this.error = error;
    }

    public BusinessProcessError getError() {
        return error;
    }
}
