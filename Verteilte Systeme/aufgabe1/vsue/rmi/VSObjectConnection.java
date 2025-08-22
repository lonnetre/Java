package vsue.rmi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class VSObjectConnection {

    private VSConnection connection;

    public VSObjectConnection (VSConnection connection) {
        this.connection = connection;
    }

    // Folie 1.3:6
    public void sendObject(Serializable object) throws IOException{
        // Stream → Byte-Array
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // Objekt → Stream
        ObjectOutputStream oos = new ObjectOutputStream(out);

        // Objekt serialisieren (Marshalling)
        oos.writeObject(object);
        oos.flush();

        // Rueckgabe des Byte-Array
        connection.sendChunk(out.toByteArray());
    }

    public Serializable receiveObject() throws IOException, ClassNotFoundException{
        byte[] chunk = connection.receiveChunk();
        // Byte-Array → Stream
        ByteArrayInputStream bis = new ByteArrayInputStream(chunk);
        // Stream → Objekt
        ObjectInputStream ois = new ObjectInputStream(bis);
        // Objekt deserialisieren (Unmarshalling)
        return (Serializable) ois.readObject();
    }
}

// Semantik?
// wenn verbinfung abbricht, was passiert?

/* 
 * Versendet und empfängt beliebige Objekte.
 * Übernimmt Marshaling und Unmarshaling (Objekt <-> Nachricht).
 */

/*
Darauf aufbauend gibt es dann die VSObjectConnection, die beliebige Objekte senden und empfangen können soll. 
Dazu muss sich diese um das Marshaling und Unmarshaling kümmern, sodass die Objekte als Nachrichten zwischen den Rechnern übertragen werden können. 
Hierzu gibt es mehr Details im entsprechenden Video.
 */