package vsue.rpc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class VSObjectConnection {

    private final VSConnection connection;

    public VSObjectConnection (VSConnection connection) {
        this.connection = connection;
    }

    // Folie 1.3:6
    public void sendObject(Serializable object) throws IOException{
        // Stream → Byte-Array
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // Objekt → Stream
        ObjectOutputStream oos = new ObjectOutputStream(out);

        oos.writeObject(object);
        oos.flush();
        connection.sendChunk(out.toByteArray());
    }

    public Serializable receiveObject() throws IOException, ClassNotFoundException{
        byte[] chunk = connection.receiveChunk();
        if (chunk == null) {
//            throw new IOException("Received null chunk from connection.");
            return null;
        }
        // Byte-Array → Stream
        ByteArrayInputStream bis = new ByteArrayInputStream(chunk);
        // Stream → Objekt
        ObjectInputStream ois = new ObjectInputStream(bis);
        return (Serializable) ois.readObject();
    }

    public void close() throws IOException {
        connection.close();
    }
}
