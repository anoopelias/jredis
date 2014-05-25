package jredis;

import static jredis.Protocol.COLON;
import static jredis.Protocol.CRLF;
import static jredis.Protocol.DOLLAR;
import static jredis.Protocol.ERROR;
import static jredis.Protocol.MINUS;
import static jredis.Protocol.NULL_STRING;
import static jredis.Protocol.PLUS;
import static jredis.Protocol.STAR;
import static jredis.Protocol.toBytes;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

import jredis.data.ElementRange;
import jredis.data.ResponseOk;
import jredis.domain.BinaryString;
import jredis.domain.Element;
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
            write(element.getMember());

            if (elementRange.isScored())
                write(String.valueOf(element.getScore()));
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
    private void write(String value) throws IOException {
        writeString(new BinaryString(value));
    }

    /**
     * Write a BinaryString back to the client.
     * 
     * @param output
     * @throws IOException
     */
    public void write(BinaryString value) throws IOException {
        writeString(value);
        out.flush();
    }

    private void writeString(BinaryString value) throws IOException {
        if (value == null) {
            out.write(NULL_STRING);
            out.write(CRLF);
        } else {
            out.write(DOLLAR);
            out.write(toBytes(value.length()));
            out.write(CRLF);
            value.write(out);
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
