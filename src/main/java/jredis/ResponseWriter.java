package jredis;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

/**
 * Response writer to handle response protocol.
 * 
 * @author anoopelias
 * 
 */
public class ResponseWriter {

    private OutputStream out;

    private static final byte DOLLAR = '$';
    private static final byte PLUS = '+';
    private static final byte COLON = ':';
    private static final byte STAR = '*';
    
    private static final byte[] CRLF = { '\r', '\n' };
    private static final byte[] NULL_STRING = { DOLLAR, '-', '1' };

    /**
     * Construct a writer using output stream.
     * 
     * @param os
     */
    public ResponseWriter(OutputStream os) {
        out = new BufferedOutputStream(os);
    }
    
    /**
     * Write a element range back to client.
     * 
     * @param output
     * @throws IOException
     */
    public void write(ElementRange elementRange) throws IOException {
        
        Set<Element> elements = elementRange.getElements();
        
        long outputSize = elements.size();
        if(elementRange.isScored())
            outputSize *= 2;
        
        out.write(STAR);
        out.write(String.valueOf(outputSize).getBytes());
        out.write(CRLF);
        
        for(Element element : elements) {
            writeString(element.getMember());
            
            if(elementRange.isScored())
                writeString(String.valueOf(element.getScore()));
        }
        
        out.flush();
    }


    /**
     * Write a string back to the client.
     * 
     * @param output
     * @throws IOException
     */
    public void write(String value) throws IOException {
        writeString(value);
        out.flush();
    }

    private void writeString(String value) throws IOException {
        if (value == null) {
            out.write(NULL_STRING);
            out.write(CRLF);
        } else {
            byte[] bValue = value.getBytes();
            out.write(DOLLAR);
            out.write(String.valueOf(bValue.length).getBytes());
            out.write(CRLF);
            out.write(bValue);
            out.write(CRLF);
        }
    }

    /**
     * Write a string back to the client.
     * 
     * @param output
     * @throws IOException
     */
    public void writeOk() throws IOException {
        out.write(PLUS);
        out.write(ResponseOk.OK.getBytes());
        out.write(CRLF);

        out.flush();
    }

    /**
     * Write a number back to the client.
     * 
     * @param output
     * @throws IOException
     */
    public void write(int number) throws IOException {
        out.write(COLON);
        out.write(String.valueOf(number).getBytes());
        out.write(CRLF);
        out.flush();
    }

}
