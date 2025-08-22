package vsue.rmi;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class VSConnection {
    private OutputStream out;
    private InputStream in;

    public VSConnection (OutputStream out, InputStream in) {
        this.out = out;
        this.in = in;
    }

    // double d = 0.47;
    // ByteBuffer buf = ByteBuffer.allocate(Double.BYTES);
    // buf.putDouble(d);
    // byte[] data = buf.array();

    // ByteBuffer buf2 = ByteBuffer.wrap(data);
    // double d2 = buf2.getDouble(); // d2 = 0.47


    public void sendChunk(byte[] chunk) throws IOException{
        // Mit Hilfe der Methode sendChunk() lassen sich Datenpakete beliebiger Größe über eine bestehende Verbindung übertragen

        // schreiben in OutputStream

        // Die Methode OutputStream.write(int) überträgt nur die niedrigsten 8 Bits des übergebenen Integer.
        // => ByteBuffer

        /* Repräsentation von Daten
         * Blockgröße: 4 Bytes
         * Bei Bedarf: Auffüllen mit Null-Bytes
        */
        ByteBuffer buf = ByteBuffer.allocate(4 + chunk.length);
        buf.putInt(chunk.length);
        buf.put(chunk);

        out.write(buf.array());

        // Wichtig, damit die Daten sofort gesendet werden
        out.flush();
    }



    
    public byte[] receiveChunk() throws IOException{

        /* 
        Per receiveChunk() wird ein von der Gegenseite gesendetes Datenpaket empfangen. 
        Die Methode receive-Chunk() blockiert dabei so lange, bis das Datenpaket vollständig übermittelt wurde. 
        Ein Aufruf von sendChunk() lässt sich somit also genau einem Aufruf von receiveChunk() zuordnen.
        */

        // Lesen von InputStream

        /*
        Zuerst die Längeninformation (4 Bytes) aus dem InputStream lesen
        Diese 4 Bytes in einen Integer konvertieren, um die Länge des Chunks zu ermitteln
        Einen Buffer mit der entsprechenden Länge erstellen
        Die eigentlichen Chunk-Daten in der angegebenen Länge lesen
        Das gelesene Byte-Array zurückgeben
         */

        byte[] lengthOfBuffer = in.readNBytes(4);

        if (lengthOfBuffer.length < 4) {
            return null;
        }

        // Konvertiere die 4 Bytes in einen Integer (Big-Endian wird von ByteBuffer standardmäßig verwendet).
        ByteBuffer buf = ByteBuffer.wrap(lengthOfBuffer);
        int chunkLength = buf.getInt();

        if (chunkLength < 0) {
            throw new IOException("Ungültige Chunk-Länge: " + chunkLength);
        }

        // Lese den Daten-Chuck mit der ermittelten Länge.
        byte[] chunkBuffer = in.readNBytes(chunkLength);
        
        if (chunkBuffer.length < chunkLength) {
            // Stream vorzeitig zu Ende
            throw new EOFException("Erwartet: " + chunkLength + " Bytes, bekommen: " + chunkBuffer.length);
        }

        return chunkBuffer;
    }
}

/* 
einfach recieve- und sendChunk 100% (vollstaendig) der Bytesanzahl zulief (symmetrie)
wenn 100 Bytes uebergeben -> 100 Bytes in der Leitung, mussen 100 Bytes zuruckkommen
*/ 

/*
 * Versendet und empfängt Byte-Arrays beliebiger Länge.
 * Nutzt TCP-Verbindung.
*/

/* 
- Die Implementierung von VSConnection soll die Daten direkt auf den OutputStream des TCP-Sockets schreiben
bzw. sie von seinem InputStream lesen. (Keine Verwendung von komplexeren Stream-Klassen!)
- Die Methode OutputStream.write(int) überträgt nur die niedrigsten 8 Bits des übergebenen Integer.
*/

/*
 * receiveChunk()
 *  wenn length of buffer leer ist => buf.getInt throws exception => null rausgeben
    fuer die 2. Aufagbe wichtig
    Das problem ist, wen die verbindung beendet, endofstream bspw
 */