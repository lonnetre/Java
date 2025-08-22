package vsue.rmi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.Arrays;

public class VSClient {

    private static String HOST = ServerConfig.CS_HOST; 
    private static int PORT = ServerConfig.CS_PORT;

    public static void main(String[] args) throws IOException{
        try {
            // Verbindung zum Server herstellen
            System.out.println("\nClient startet connection for " + HOST + " " + PORT);
            Socket s = new Socket(HOST, PORT);
            VSConnection vsConnection = new VSConnection(s.getOutputStream(), s.getInputStream());
            VSObjectConnection objectConnection = new VSObjectConnection(vsConnection);
            System.out.println("Connected to server\n");

            Serializable[] tests = {
                10,
                "Hallo",
                new int[]{1,2,-3,4,-5},
                new String[]{"a", "b", "c", "d"},
                new double[]{1.1, 2.2, 3.3, 4.4},
                new VSAuction("Banana", 10)
            };

            for (Serializable t : tests) {
                runTest(objectConnection, t);
            }

        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void runTest (VSObjectConnection objectConnection, Serializable object) throws IOException, ClassNotFoundException{
        System.out.println("Sent: " + object);
        objectConnection.sendObject(object);

        Serializable reply = objectConnection.receiveObject();
        System.out.println("Received: " + reply);

        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream objectOut = new ObjectOutputStream(byteOut);
        objectOut.writeObject(object);
        objectOut.flush();
        byte[] obj1 = byteOut.toByteArray();

        ByteArrayOutputStream byteOut2 = new ByteArrayOutputStream();
        ObjectOutputStream objectOut2 = new ObjectOutputStream(byteOut2);
        objectOut2.writeObject(reply);
        objectOut2.flush();
        byte[] obj2 = byteOut2.toByteArray();

        System.out.println(Arrays.equals(obj1, obj2) ? "Equal\n" : "Not equal\n");
    }
}

/*
Auf Client-Seite ist eine Klasse VSClient zu realisieren, 
1. die eine Verbindung zum Server herstellt und 
2. verschiedene Objekte (z. B. Integers, Strings, Arrays, VSAuctions) über diese sendet. 

Bei den daraufhin vom Server zurückgeschickten Objekten ist zu überprüfen, ob ihre Zustände jeweils exakte Kopien der Originalobjekte repräsentieren.
 */

 