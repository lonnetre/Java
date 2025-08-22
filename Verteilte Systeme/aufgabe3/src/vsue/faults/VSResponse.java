package vsue.faults;

import java.io.*;

public class VSResponse implements Serializable {
    private final Object returnValue;
    private final Throwable exception;

    private final String remoteCallID;
    private final int sequenceNumber;

    public VSResponse(Object returnValue, Throwable exception, String remoteCallID, int sequenceNumber) {
        this.returnValue = returnValue;
        this.exception = exception;
        this.remoteCallID = remoteCallID;
        this.sequenceNumber = sequenceNumber;
    }

    public Object getReturnValue() {
        return returnValue;
    }

    public Throwable getException() {
        return exception;
    }

    public boolean isSuccessful() {
        return exception == null;
    }

    @Override
    public String toString() {
        return "VSResponse [returnValue=" + returnValue + ", exception=" + exception + "]";
    }

    public String getRemoteCallID() {
        return remoteCallID;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }
}
