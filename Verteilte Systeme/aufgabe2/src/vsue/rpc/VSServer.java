package vsue.rpc;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Proxy;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Remote;

public class VSServer {

    // ServerSocket als Klassenvariable, damit er innerhalb derselben JVM zugänglich ist.
    private static ServerSocket serverSocket;
    private static final Object START_LOCK = new Object();
    private static boolean started = false;

    public static void main(String[] args) throws IOException{

        // We can use random ports here because the serverSocket is exposed and we can use the getter.
        serverSocket = new ServerSocket(0);
        System.out.println("Start VSServer on port " + serverSocket.getLocalPort());

        // Mark server as started and notify all waiting threads
        synchronized (START_LOCK) {
            started = true;
            START_LOCK.notifyAll();
        }

        while(true) {
            try {
                Socket clientSocket = serverSocket.accept();
                new Thread(new Worker(clientSocket)).start();
                System.out.println("New connection from " + clientSocket.getRemoteSocketAddress());
            } catch (Exception e){
                System.err.println("Error in accept loop: " + e.getMessage());
                e.printStackTrace();
            } 
        }
    }

    // Getter um port zu holen
    public static int getPort() {
        return serverSocket.getLocalPort();
    }

    public static Object getStartLock() {
        return START_LOCK;
    }

    public static boolean isStarted() {
        return started;
    }

    public static class Worker implements Runnable {
        private final Socket clientSocket;

        public Worker(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                VSConnection vsConnection = new VSConnection(clientSocket.getOutputStream(), clientSocket.getInputStream());
                VSObjectConnection objectConnection = new VSObjectConnection(vsConnection);

                while (true) {
                    Serializable receivedObject = objectConnection.receiveObject();
                    if (receivedObject == null) {
                        System.out.println("[" + clientSocket.getRemoteSocketAddress() + "] connection closed");
                        break;
                    }

                    if (VSRequest.class.isAssignableFrom(receivedObject.getClass())) {
                        VSRequest request = (VSRequest) receivedObject;

                        int objectID = request.getObjectID();
                        String methodName = request.getMethodName();
                        Object[] args = request.getArgs();
                        // Aufgabe 2.4
                        if (args != null) {
                            for (int i = 0; i < args.length; i++) {
                                if (args[i] instanceof VSRemoteReference) {
                                    VSRemoteReference ref = (VSRemoteReference) args[i];
                                    args[i] = Proxy.newProxyInstance(
                                            VSAuctionClient.class.getClassLoader(),
                                            VSAuctionClient.class.getInterfaces(),
                                            new VSInvocationHandler(ref)
                                    );
                                }
                            }
                        }
                        // Ende Aufgabe 2.4


                        VSRemoteObjectManager localObject = VSRemoteObjectManager.getInstance();
                        VSResponse response;
                        try {
                            Object result = localObject.invokeMethod(objectID, methodName, args);
                            response = new VSResponse(result, null);
                        } catch (Throwable e) {
                            System.err.println(e.getMessage());
                            response = new VSResponse(null, e);
                        }
                        objectConnection.sendObject(response);
                    } else {
                        objectConnection.sendObject(receivedObject);
                    }
                }
            } catch (Exception e) {
                System.err.println("Connection with client " + clientSocket.getRemoteSocketAddress() + " ended: " + e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    System.err.println("Error closing client socket: " + e.getMessage());
                }
            }
        }
    }
}
