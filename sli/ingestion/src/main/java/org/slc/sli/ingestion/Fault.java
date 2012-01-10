package org.slc.sli.ingestion;

/**
 * Fault that represents a warning or error in the system.
 *
 * @author okrook
 *
 */
public class Fault {

    protected String message;
    protected FaultType type;

    protected Fault(FaultType type, String message) {
        this.type = type;
        this.message = message;
    }

    public static Fault createWarning(String message) {
        return new Fault(FaultType.TYPE_WARNING, message);
    }

    public static Fault createError(String message) {
        return new Fault(FaultType.TYPE_ERROR, message);
    }

    public String getMessage() {
        return message;
    }

    public boolean isWarning() {
        return this.type == FaultType.TYPE_WARNING;
    }

    public boolean isError() {
        return this.type == FaultType.TYPE_ERROR;
    }

    @Override
    public String toString() {
        return type.getName() + ": " + getMessage();
    }
}
