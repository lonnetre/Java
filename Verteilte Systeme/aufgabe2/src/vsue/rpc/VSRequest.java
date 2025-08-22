package vsue.rpc;

import java.io.*;

public class VSRequest implements Serializable{
    private final int objectID;
    private final String methodName;
    private final Object[] args;

    public VSRequest (int objectID, String methodName, Object[] args) {
        this.objectID = objectID;
        this.methodName = methodName;
        this.args = args;
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

}
