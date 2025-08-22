package vsue.faults;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;

public class VSRemoteReference implements Serializable {
    private final String host;
    private final int port;
    private final int objectID;
    private int referenceCounter;

    private static final long DEFAULT_LEASE_DURATION = 600000; // 10 minutes in milliseconds 10 * 60 * 1000
    private long leaseExpirationTime;

    public VSRemoteReference(String host, int port, int objectID) {
        this.host = host;
        this.port = port;
        this.objectID = objectID;
        renewLease();
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public int getObjectID() {
        return objectID;
    }

    public VSBuggyObjectConnection openConnectionToServer() throws IOException {
        try {
            Socket s = new Socket(host, port);
            //timeout wird gestartet, wenn der client/server versucht receivedObject aufzurufen bzw bei jeder lese operation
            s.setSoTimeout(5000);
            VSConnection c = new VSConnection(s.getOutputStream(), s.getInputStream(), s);
//            return new VSObjectConnection(c);
            return new VSBuggyObjectConnection(c);
        } catch (UnknownHostException e) {
            throw new IOException("Unknown host: " + host, e);
        }
    }

    public synchronized void incrementReferenceCounter() {
        referenceCounter++;
    }

    public synchronized void decrementReferenceCounter() {
        if (referenceCounter > 0) referenceCounter--;
    }

    public int getReferenceCounter() {
        return referenceCounter;
    }

    public synchronized long getLeaseExpirationTime() {
        return leaseExpirationTime;
    }

    public synchronized long renewLease() {
        leaseExpirationTime = System.currentTimeMillis() + DEFAULT_LEASE_DURATION;
        return leaseExpirationTime;
    }
}
