package org.chtijbug.drools.platform.rules.management;

/**
 * Created by alexandre on 23/06/2014.
 */
public enum RuntimeStatus {
    INITMODE,
    STARTED,
    NOT_JOIGNABLE,
    STOPPED,
    CRASHED,
    NONE;

    public static RuntimeStatus getEnum(String runtimeStatus) {
        for (RuntimeStatus status : RuntimeStatus.values()) {
            if (status.name().equals(runtimeStatus))
                return status;
        }
        return null;
    }

    public RuntimeStatus getNextStatus() {
        RuntimeStatus result = null;
        switch (this) {
            case INITMODE:
                result = STARTED;
                break;
            case STARTED:
                result = NOT_JOIGNABLE;
                break;
            case NOT_JOIGNABLE:
                result = STOPPED;
                break;
            case STOPPED:
                result = CRASHED;
                break;

        }
        return result;
    }

    public RuntimeStatus getPreviousStatus() {
        RuntimeStatus result = null;
        switch (this) {
            case STARTED:
                result = INITMODE;
                break;
            case NOT_JOIGNABLE:
                result = STARTED;
                break;
            case STOPPED:
                result = NOT_JOIGNABLE;
                break;
            case CRASHED:
                result = STOPPED;
                break;
        }
        return result;
    }
}
