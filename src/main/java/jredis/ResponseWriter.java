package jredis;

import static jredis.Utils.COLON;
import static jredis.Utils.CRLF;
import static jredis.Utils.DOLLAR;
import static jredis.Utils.ERROR;
import static jredis.Utils.MINUS;
import static jredis.Utils.NULL_STRING;
import static jredis.Utils.PLUS;
import static jredis.Utils.STAR;
import static jredis.Utils.toBytes;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

import jredis.exception.InvalidCommand;

/**
 * Response writer to handle response protocol.
 * 
 * @author anoopelias
 * 
 */
public class ResponseWriter {

    private OutputStream out;

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
        if (elementRange.isScored())
            outputSize *= 2;

        out.write(STAR);
        out.write(toBytes(outputSize));
        out.write(CRLF);

        for (Element element : elements) {
            writeString(element.getMember());

            if (elementRange.isScored())
                writeString(String.valueOf(element.getScore()));
        }

        out.flush();
    }

    /**
     * Write an error back to the client.
     * 
     * @param output
     * @throws IOException
     */
    public void write(InvalidCommand e) throws IOException {

        out.write(MINUS);
        out.write(ERROR);
        if (e.getMessage() != null) {
            out.write(toBytes(e.getMessage()));
            out.write(CRLF);
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
            byte[] bValue = toBytes(value);
            out.write(DOLLAR);
            out.write(toBytes(bValue.length));
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
        out.write(toBytes(ResponseOk.OK));
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
        out.write(toBytes(number));
        out.write(CRLF);
        out.flush();
    }

}
