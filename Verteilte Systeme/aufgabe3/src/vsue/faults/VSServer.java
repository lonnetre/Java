package vsue.faults;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;

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
                VSConnection vsConnection = new VSConnection(clientSocket.getOutputStream(), clientSocket.getInputStream(), clientSocket);
//                VSObjectConnection objectConnection = new VSObjectConnection(vsConnection);
                VSBuggyObjectConnection objectConnection = new VSBuggyObjectConnection(vsConnection);

                while (true) {
                    Serializable receivedObject = objectConnection.receiveObject();
                    if (receivedObject == null) {
                        System.out.println("[" + clientSocket.getRemoteSocketAddress() + "] connection closed");
                        break;
                    }

                    if (VSRequest.class.isAssignableFrom(receivedObject.getClass())) {
                        VSRequest request = (VSRequest) receivedObject;

                        // #############
                        // # TESTCASES #
                        // #############

                        if (VSConfig.isTest("server1") && request.getSequenceNumber() == 1) {
                            objectConnection.delayNextSent(6000);
                        }

                        int objectID = request.getObjectID();
                        String methodName = request.getMethodName();
                        Object[] args = request.getArgs();
                        
                        VSRemoteObjectManager localObject = VSRemoteObjectManager.getInstance();
                        VSResponse response;
                        System.out.println("[Exercise 3] - Request with " + request.getRemoteCallID() + " callID and " + request.getSequenceNumber() + " sequenceNumber");
                        try {
                            Object result = localObject.invokeMethod(objectID, methodName, args);
                            response = new VSResponse(result, null, request.getRemoteCallID(), request.getSequenceNumber());
                        } catch (Throwable e) {
                            System.err.println(e.getMessage());
                            response = new VSResponse(null, e, request.getRemoteCallID(), request.getSequenceNumber());
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
