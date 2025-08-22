package vsue.faults;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.SocketTimeoutException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

public class VSInvocationHandler implements InvocationHandler, Serializable {
    
    private final VSRemoteReference remoteReference;
    private final int maxAttempts = VSConfig.MAX_ATTEMPTS;

    public VSInvocationHandler (VSRemoteReference remoteReference) {
        this.remoteReference = remoteReference;
    }

    public Object invoke (Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getDeclaringClass() == Object.class) {
            switch (method.getName()) {
                case "hashCode": return System.identityHashCode(proxy);
                case "equals": return proxy == args[0];
                case "toString": return "Remote proxy for " + remoteReference;
            }
        }

        String callID = UUID.randomUUID().toString();
        int sequenceNumber = 0;
//        VSObjectConnection oc = null;
        VSBuggyObjectConnection oc = null;
        int attempts = 0;

        try {
            while (attempts < maxAttempts) {
                attempts++;
                oc = remoteReference.openConnectionToServer();
                System.out.println("[Exercise 3: invoke] - Attempt " + attempts + " of " + maxAttempts);

                // #############
                // # TESTCASES #
                // #############

                if (VSConfig.isTest("client1") && attempts == 1) {
                    oc.dropNextSend();
                } else if (VSConfig.isTest("client2") && attempts == 1) {
                    oc.dropNextRecv();
                } else if (VSConfig.isTest("client3") && attempts == 1) {
                    oc.dropConnection();
                } else if (VSConfig.isTest("client4") && attempts == 1) {
                    oc.delayNextSent(6000);
                } else if (VSConfig.isTest("client5") && (attempts == 1 || attempts == 2)) {
                    if (attempts == 1) oc.dropNextSend();
                    else oc.dropConnection();
                } else if (VSConfig.isTest("client6") && (attempts == 1 || attempts == 2)) {
                    if (attempts == 1) oc.dropNextRecv();
                    else oc.dropConnection();
                } else if (VSConfig.isTest("client7") && (attempts == 1 || attempts == 2 || attempts == 3)) {
                    if (attempts == 1) oc.dropNextSend();
                    else if (attempts == 2) oc.dropConnection();
                    else oc.delayNextSent(6000);
                } else if (VSConfig.isTest("client8") && (attempts == 1 || attempts == 2 || attempts == 3)) {
                    if (attempts == 1) oc.dropNextRecv();
                    else if (attempts == 2) oc.dropConnection();
                    else oc.delayNextSent(6000);
                }

                //  Aufgabe 2.4
                Object[] marshalledArgs = null;
                if (args != null) {
                    marshalledArgs = new Object[args.length];
                    for (int i = 0; i < args.length; i++) {
                        Object arg = args[i];
                        if (arg instanceof Remote) {
                            Remote remoteArg = (Remote) arg;
                            VSRemoteObjectManager manager = VSRemoteObjectManager.getInstance();
                            Remote stub = manager.exportObject(remoteArg);
                            marshalledArgs[i] = stub;
                        } else {
                            marshalledArgs[i] = arg;
                        }
                    }
                }
                // Ende Aufgabe 2.4

                VSRequest request = new VSRequest(remoteReference.getObjectID(), method.toGenericString(), marshalledArgs, callID, sequenceNumber);
                oc.sendObject(request);
                System.out.println("[Exercise 3: invoke] - Sent request to " + remoteReference.getObjectID() + " objectID");

                try {
                    //Receive response - in receivedObject haben wir die lese operation -> setTimeout von der VSRemoteReference(openConnection) wird gestartet
                    //return (Serializable) ois.readObject();
                    Serializable rawResponse = oc.receiveObject();

                    if (!(rawResponse instanceof VSResponse)) {
                        throw new IOException("Invalid response received from server");
                    }

                    VSResponse response = (VSResponse) rawResponse;

                    System.out.println("[Exercise 3: invoke] - Request received");

                    // alte oder falsche Antwort -> ignorieren (kommt durch delays zustande - entwender von uns oder server antworten speater als wir packete verschicken)
                    if (!callID.equals(response.getRemoteCallID()) || response.getSequenceNumber() != sequenceNumber) {
                        System.out.println("[Exercise 3: invoke] - Old or wrong answer");
                        //wenn das package nicht das richtige ist, dann duerfen nicht continuen, sondern muessen das package neu senden
                        //und attempt nicht inkrementieren, weil die connection sonst nicht regelrecht geclosed wird.
                        //wenn attemp 2 falsch ist machen wir continue und attempt 3 wird durchgefuehrt, obwohl attempt 2 nicht fertig ist.
                        //wenn jetzt attempt 3 erfolgreich etwas verschickt und received und dann den serverclosen, dann wird auch der
                        //2 attempt server geclosed, das wollen wir nicht -> attempt in zeile 38 weiter unten inkrementieren und nicht am anfang
                        continue;
                    }

                    if (response.getException() != null) {
                        throw response.getException();
                    }

                    return response.getReturnValue();
                } catch (SocketTimeoutException ste) { //Wird geworfen, wenn receivedObject fehlschlaegt (in unseren fall, wenn wir nach 5 sec nichts receiven, dann exeption)
                    System.err.println("[Exercise 3: invoke] - SocketTimeoutException in attempt " + attempts + " of " + maxAttempts);
                } catch (IOException ioe) {
                    System.err.println("[Exercise 3: invoke] - IOException in attempt " + attempts + " of " + maxAttempts);
                }
                oc.close();
                sequenceNumber++;
                Thread.sleep(1000);
            }
            throw new RemoteException("Remote call failed after " + maxAttempts + " attempts");
        } catch (IOException e) {
            throw new RemoteException(e.getMessage());
        } finally {
            if (oc != null) {
                try {
                    oc.close();
                } catch (IOException e) {
                    System.err.println("Error closing VSObjectConnection: " + e.getMessage());
                }
            }
        }
    }
}
