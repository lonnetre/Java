package vsue.rpc;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.rmi.Remote;
import java.rmi.RemoteException;

public class VSInvocationHandler implements InvocationHandler, Serializable {
    
    private final VSRemoteReference remoteReference;

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

        VSObjectConnection oc = null;
        try {
            oc = remoteReference.openConnectionToServer();

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
                        // TODO: COMPLETED: marshalledArgs[i] = stub
//                        VSRemoteReference ref = manager.getReferenceForObject(stub);
                        marshalledArgs[i] = stub;
                    } else {
                        marshalledArgs[i] = arg;
                    }
                }
            }
            // Ende Aufgabe 2.4

            VSRequest request = new VSRequest(remoteReference.getObjectID(), method.toGenericString(), marshalledArgs);
            oc.sendObject(request);

            // Receive response

            Serializable rawResponse = oc.receiveObject();
            if (!(rawResponse instanceof VSResponse)) {
                throw new IOException("Invalid response received from server");
            }

            VSResponse response = (VSResponse) rawResponse;
            if (response.getException() != null) {
                throw response.getException();
            }

            // Aufgabe 2.4
            Object result = response.getReturnValue();
            if (result instanceof VSRemoteReference) {
                VSRemoteReference ref = (VSRemoteReference) result;
                VSRemoteObjectManager manager = VSRemoteObjectManager.getInstance();
                Remote stub = manager.getStubForReference(ref);

                if (stub != null) {
                    // TODO: COMPLETED: stub wurde schon erstellt
                    return stub;
                } else {
                    // (optional) falls noch kein Stub existiert, kannst du hier einen neuen anlegen:
                    Class<?> remoteInterface = VSAuctionEventHandler.class;
                    stub = (Remote) Proxy.newProxyInstance(
                            remoteInterface.getClassLoader(),
                            new Class<?>[]{remoteInterface},
                            new VSInvocationHandler(ref)
                    );
                    return stub;
                }
            }
            // Ende Aufgabe 2.4

            return result;
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

/*
Überprüfung, ob ein Objekt eine bestimmte Schnittstelle
(z. B. Serializable) implementiert
Object o = [...];
if(Serializable.class.isAssignableFrom(o.getClass())) {[...]}
 */

// Timing Dynamic Proxy Example: https://www.baeldung.com/java-dynamic-proxies
