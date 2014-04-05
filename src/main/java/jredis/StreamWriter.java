package jredis;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class StreamWriter {
    
    private OutputStream os = null;
    
    public StreamWriter(OutputStream os) {
        this.os = new BufferedOutputStream(os);
    }
    
    public void write(ByteString b) throws IOException {
        ByteArray by = b.toByteArray();
        os.write((byte)(by.length()));
        by.write(os);
        os.flush();
    }

}
