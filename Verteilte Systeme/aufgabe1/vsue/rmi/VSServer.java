package vsue.rmi;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;

public class VSServer {

    private static int PORT = ServerConfig.CS_PORT;

    public static void main(String[] args) throws IOException{
        ServerSocket ss = new ServerSocket(PORT);
        System.out.println("Start VSServer on port " + PORT);

        // running infinite loop for getting client request 
        while(true) {
            Socket clientSocket = null;
            try {
                // socket object to receive incoming client requests 
                clientSocket = ss.accept();
                System.out.println("New connection from " + clientSocket.getRemoteSocketAddress());
                  
                Worker worker = new Worker(clientSocket);
                Thread workerThread = new Thread(worker);
                workerThread.start();
            } catch (Exception e){ 
                clientSocket.close(); 
                System.err.println(e.getMessage()); 
            } 
        }
    }

    // Verbindung Verarbeitung in Worker-Thread
    public static class Worker implements Runnable {
        private Socket s;

        public Worker(Socket s) {
            this.s = s;
        }

        @Override
        public void run() {
            try {
                System.out.println("Start Worker-Thread for " + s.getRemoteSocketAddress());
                // Aufbau der Kommunikationsschicht: 
                // VSConnection arbeitet mit den Socket-Streams
                // VSObjectConnection übernimmt das Marshaling und Unmarshaling
                VSConnection vsConnection = new VSConnection(s.getOutputStream(), s.getInputStream());
                VSObjectConnection objectConnection = new VSObjectConnection(vsConnection);
                // Solange der Client verbunden ist, empfange Objekte und sende sie als Echo zurück

                while (true) {
                    Serializable receivedObject = objectConnection.receiveObject();

                    // auf null pruefen => verbinfung beenden
                    if (receivedObject == null) {
                        System.out.println("[" + s.getRemoteSocketAddress() + "] client connection closed");
                        break;
                    }

                    System.out.println("[" + s.getRemoteSocketAddress() + "] received: " + receivedObject);

                    // Echo = unveraenderte Objekt
                    objectConnection.sendObject(receivedObject);
                    System.out.println("[" + s.getRemoteSocketAddress() + "] sent back");
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Connection with client " + s.getRemoteSocketAddress() + " ended: " + e.getMessage());
            } finally {
                try {
                    s.close();
                } catch (IOException e) {
                    System.err.println("Error closing client socket: " + e.getMessage());
                }
            }  
        } 
    }
}


/* 
Auf Server-Seite ist hierzu eine Klasse VSServer zu erstellen, 
1. die eingehende Verbindungen über einen Server-Socket annimmt und sie jeweils in einem eigenen Worker-Thread bearbeitet. 
2. Die einzige Aufgabe eines solchen Worker-Thread besteht darin, jedes von der Gegenseite eintreffende Objekt unverändert
zurückzuschicken, solange der Client die Verbindung offen hält.
 */