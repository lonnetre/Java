package vsue.faults;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class VSBuggyObjectConnection extends VSObjectConnection {

    private boolean dropNextSend = false;
    private boolean dropNextRecv = false;
    private boolean dropConnection = false;
    private int delayNextSent = 0;

    public void dropNextSend() {
        dropNextSend = true;
    }
    public void dropNextRecv() {
        dropNextRecv = true;
    }
    public void dropConnection() {
        dropConnection = true;
    }
    public void delayNextSent(int ms) {
        delayNextSent = ms;
    }

    public VSBuggyObjectConnection(VSConnection connection) {
        super(connection);
    }

    // Folie 1.3:6
    public void sendObject(Serializable object) throws IOException{
        // Verlust einer Anfrage
        if (dropNextSend) {
            dropNextSend = false;
            System.out.println("[BUGGY] - Request thrown away");
            return;
        }

        if (dropConnection) {
            dropConnection = false;
            System.out.println("[BUGGY] - Connection closed");
            super.close();
            return;
        }

        // Verzögerung
        //delay reicht hier ur aus - wir brauchen keinen extra delay bei receiveObject(), die logik ist die selbe
        //macht sinn wie wir es haben, wegen sockettimeoutexecption - wenn receivedObject kleiner ist als die timout selbst, dann bekmmen wir eine exeption
        if (delayNextSent > 0) {
            System.out.println("[BUGGY] - Package delayed");
            try {
                Thread.sleep(delayNextSent);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            delayNextSent = 0;
        }
        super.sendObject(object);
    }

    public Serializable receiveObject() throws IOException, ClassNotFoundException {
        // Verlust einer Antwort
        if (dropNextRecv) {
            dropNextRecv = false;
            System.out.println("[BUGGY] - Answer thrown away");
            return null;
        }

        if (dropConnection) {
            dropConnection = false;
            System.out.println("[BUGGY] - Connection closed");
            super.close();
        }

        return super.receiveObject();
    }
}
