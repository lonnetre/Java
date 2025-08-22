package vsue.rpc;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class VSConnection {
    private final OutputStream out;
    private final InputStream in;

    public VSConnection (OutputStream out, InputStream in) {
        this.out = out;
        this.in = in;
    }

    public void sendChunk(byte[] chunk) throws IOException{
        ByteBuffer buf = ByteBuffer.allocate(4 + chunk.length);
        buf.putInt(chunk.length);
        buf.put(chunk);

        out.write(buf.array());
        out.flush();
    }
    
    public byte[] receiveChunk() throws IOException{
        byte[] lengthOfBuffer = in.readNBytes(4);

        if (lengthOfBuffer.length < 4) {
            return null;
        }

        ByteBuffer buf = ByteBuffer.wrap(lengthOfBuffer);
        int chunkLength = buf.getInt();

        if (chunkLength < 0) {
            throw new IOException("Invalid chunk length: " + chunkLength);
        }

        byte[] chunkBuffer = in.readNBytes(chunkLength);
        
        if (chunkBuffer.length < chunkLength) {
            // Stream vorzeitig zu Ende
            throw new EOFException("Expected: " + chunkLength + " Bytes, received: " + chunkBuffer.length);
        }
        return chunkBuffer;
    }

    public void close() throws IOException {
        in.close();
        out.close();
    }
}
