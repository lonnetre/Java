package vsue.rpc;

import java.io.*;

public class VSResponse implements Serializable {
    private final Object returnValue;
    private final Throwable exception;

    public VSResponse(Object returnValue, Throwable exception) {
        this.returnValue = returnValue;
        this.exception = exception;
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
}
