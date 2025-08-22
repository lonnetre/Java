package vsue.faults;

import java.io.*;

public class VSRequest implements Serializable{
    private final int objectID;
    private final String methodName;
    private final Object[] args;

    private final String remoteCallID;
    private final int sequenceNumber;

    public VSRequest (int objectID, String methodName, Object[] args, String remoteCallID, int sequenceNumber) {
        this.objectID = objectID;
        this.methodName = methodName;
        this.args = args;
        this.remoteCallID = remoteCallID;
        this.sequenceNumber = sequenceNumber;
    }

    public int getObjectID() {
        return objectID;
    }

    public String getMethodName() {
        return methodName;
    }

    public Object[] getArgs() {
        return args;
    }

    public String getRemoteCallID() {
        return remoteCallID;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }
}
