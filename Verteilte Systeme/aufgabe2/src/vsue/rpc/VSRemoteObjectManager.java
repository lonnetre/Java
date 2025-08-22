package vsue.rpc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class VSRemoteObjectManager {

    private static VSRemoteObjectManager instance;

    private final Map<Remote, Remote> objectToStub = new HashMap<>();
    private final Map<Integer, Remote> idToObject = new HashMap<>();
    private final Map<Integer, VSRemoteReference> idToReference = new HashMap<>();
    private final Map<Remote, Integer> objectToID = new HashMap<>();

    private static final AtomicInteger idGenerator =  new AtomicInteger(1);
    private boolean serverStarted = false;

    public static synchronized VSRemoteObjectManager getInstance() {
        if (instance == null) {
            instance = new VSRemoteObjectManager();
        }
        return instance;
    }

    public VSRemoteObjectManager() {
        ScheduledExecutorService leaseMonitor = Executors.newSingleThreadScheduledExecutor();
        leaseMonitor.scheduleAtFixedRate(() -> {
            try {
                checkLeases();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.MINUTES);
    }

    public synchronized Remote exportObject(Remote object) throws InterruptedException {
        if (!serverStarted) {
            new Thread(() -> {
                try {
                    VSServer.main(new String[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
            serverStarted = true;
        }

        // TODO: COMPLETED: waitNotify (acquire, release)
        synchronized (VSServer.getStartLock()) {
            while (!VSServer.isStarted()) {
                VSServer.getStartLock().wait();
            }
        }

        if (objectToStub.containsKey(object)) {
            return objectToStub.get(object);
        }

        int objectID = idGenerator.getAndIncrement();
        //int objectID = object.hashCode();

        try {
            VSRemoteReference ref = new VSRemoteReference(
                    InetAddress.getLocalHost().getHostName(),
                    VSServer.getPort(),
                    objectID
            );
            ClassLoader loader = object.getClass().getClassLoader();
            Remote stub = (Remote) Proxy.newProxyInstance(
                    loader,
                    // TODO: COMPLETED: Interfaces von allen parents holen. schleif mit get superclass (die, die Remote implementieren (Remote.isAssignableFrom (siehe VL)))
                    getAllRemoteInterfaces(object.getClass()),
                    new VSInvocationHandler(ref)
            );

            objectToStub.put(object, stub);
            idToObject.put(objectID, object);
            idToReference.put(objectID, ref);
            objectToID.put(object, objectID);

            return stub;
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to export object: " + object.getClass().getName());
            e.printStackTrace();
            return null;
        }
    }

    public synchronized void unexportObject (Remote object) {
        Integer objectID = objectToID.remove(object);
        if (objectID != null) {
            objectToStub.remove(object);
            idToObject.remove(objectID);
            idToReference.remove(objectID);
        }
    }

    public synchronized Object invokeMethod(int objectID, String genericMethodName, Object [] args) throws Throwable {
        VSRemoteReference ref = idToReference.get(objectID);
        Remote object = idToObject.get(objectID);

        if (ref == null || object == null) {
            throw new RemoteException("[InvokeMethod] No reference or object found for ID: " + objectID);
        }

        try {
            // TODO: COMPLETED: Filterung nach Remote + superClass
            for (Class<?> iface : getAllRemoteInterfaces(object.getClass())) {
                for (Method method : iface.getMethods()) {
                    if (method.toGenericString().equals(genericMethodName)) {
                        Object result = method.invoke(object, args);
                        System.out.println("[DEBUG] Invoked " + method + " → result: " + result);

                        // TODO: COMPLETED: nicht selbst exportieren, muessen nur schauen, ob Object schon exportiert wurde (z.B. getStub)
                        if (result instanceof Remote) {
                            Remote remoteResult = (Remote) result;

                            if (objectToStub.containsKey(remoteResult)) {
                                return objectToStub.get(remoteResult); // Objekt wurde bereits exportiert → vorhandenes Stub zurückgeben
                            } else {
                                Remote stub = exportObject(remoteResult); // Noch nicht exportiert → Stub erzeugen
                                return stub;
                            }
                        }
                        return result;
                    }
                }
            }
            throw new NoSuchMethodException("[InvokeMethod] Method not found: " + genericMethodName);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            System.err.println("[invokeMethod] Method threw exception: " + cause);
            throw cause;
        } catch (Throwable t) {
            System.err.println("[invokeMethod] Unexpected error: " + t);
            throw t;
        }
    }

    private Class<?>[] getAllRemoteInterfaces(Class<?> clazz) {
        Set<Class<?>> interfaces = new HashSet<>();
        while (clazz != null) {
            for (Class<?> iface : clazz.getInterfaces()) {
                if (Remote.class.isAssignableFrom(iface)) {
                    interfaces.add(iface);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return interfaces.toArray(new Class<?>[0]);
    }

    // TODO: COMPLETED: braucht man nicht
    // Aufgabe 2.4
//    public synchronized VSRemoteReference getReferenceForObject(Remote stub) {
//        for (Map.Entry<Remote, Remote> entry : objectToStub.entrySet()) {
//            if (entry.getValue().equals(stub)) {
//                Integer objectID = objectToID.get(entry.getKey());
//                return idToReference.get(objectID);
//            }
//        }
//        return null;
//    }
    // Ende Aufgabe 2.4

    public synchronized Remote getStubForReference(VSRemoteReference ref) {
        int objectID = ref.getObjectID();
        Remote original = idToObject.get(objectID);
        if (original == null) {
            return null;
        }
        return objectToStub.get(original);
    }

    public synchronized void dirty(int objectID) throws RemoteException {
        VSRemoteReference ref = idToReference.get(objectID);
        if (ref != null) {
            ref.incrementReferenceCounter();
            long newExpiry = ref.renewLease();
            System.out.println("Dirty: ID " + objectID + " now has " + ref.getReferenceCounter() + " refs; lease extended to" + newExpiry);
        } else {
            System.err.println("[WARNING] Dirty call on unknown objectID " + objectID);
        }
    }
    
    public synchronized void clean(int objectID) throws RemoteException {
        VSRemoteReference ref = idToReference.get(objectID);
        if (ref != null) {
            ref.decrementReferenceCounter();
            System.out.println("Clean: ID " + objectID + " now has " + ref.getReferenceCounter() + " refs");
            if (ref.getReferenceCounter() == 0) {
                Remote object = idToObject.get(objectID);
                if (object != null) {
                    unexportObject(object); // Unexport the object to prevent further remote calls
                    System.out.println("Object with ID " + objectID + " fully unexported");
                }
            }
        } else {
            System.err.println("[Warning] Clean call on unknown objectID " + objectID);
        }
    }

    private synchronized void checkLeases() throws RemoteException {
        long now = System.currentTimeMillis();
        List<Integer> expired = new ArrayList<>();

        for (Map.Entry<Integer, VSRemoteReference> entry : idToReference.entrySet()) {
            if (entry.getValue().getLeaseExpirationTime() < now) {
                expired.add(entry.getKey());
            }
        }

        for (Integer id : expired) {
            System.out.println("Lease expired for object ID " + id);
            clean(id);
        }
    }

    public synchronized int getExportedObjectID(Remote object) {
        return objectToID.getOrDefault(object, -1);
    }
}
