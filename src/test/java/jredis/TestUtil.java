package jredis;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

/**
 * Utilities for test.
 * 
 * @author anoopelias
 * 
 */
public class TestUtil {

    public static final char STAR = '*';
    public static final char DOLLAR = '$';
    public static final char MINUS = '-';
    public static final char COLON = ':';
    public static String CRLF = "\r\n";
    public static final String OK = "+OK";

    private static final String CHARSET = "UTF-8";

    /**
     * Create a mock socket which will send the specified command string and
     * responds to the output stream.
     * 
     * @param command
     * @param os
     * @return
     * @throws IOException
     */
    public static Socket mockSocket(String command, OutputStream os)
            throws IOException {

        Socket socket = mock(Socket.class);
        InputStream is = toInputStream(command);

        when(socket.getInputStream()).thenReturn(is);
        when(socket.getOutputStream()).thenReturn(os);
        return socket;
    }

    /**
     * Convert a command string array to input stream.
     * 
     * @param command
     * @return
     */
    public static InputStream toInputStream(String[] command) {
        return toInputStream(toCommand(command));
    }

    /**
     * Convert a String to input stream.
     * 
     * @param command
     * @return
     */
    public static InputStream toInputStream(String command) {
        try {
            return new ByteArrayInputStream(command.getBytes(CHARSET));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Combine the args together to form a command.
     * 
     * @param args
     * @return
     */
    public static String toCommand(String[] args) {
        StringBuilder sb = new StringBuilder();
        sb.append(STAR);
        sb.append(args.length);
        sb.append(CRLF);
        for (String arg : args) {
            sb.append(DOLLAR);
            sb.append(Protocol.toBytes(arg).length);
            sb.append(CRLF);
            sb.append(arg);
            sb.append(CRLF);
        }

        return sb.toString();
    }

    /**
     * Get bytes from a string.
     * 
     * @param s
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] toBytes(String s) throws UnsupportedEncodingException {
        return s.getBytes(Protocol.CHARSET);
    }

    /**
     * Combine byte arrays.
     * 
     * @param byt
     * @return
     */
    public static byte[] c(byte[]... byt) {
        int size = 0;
        for (byte[] by : byt)
            size += by.length;

        byte[] ret = new byte[size];
        int index = 0;
        for (int i = 0; i < byt.length; i++) {
            for (int j = 0; j < byt[i].length; j++) {
                ret[index++] = byt[i][j];
            }
        }

        return ret;
    }

}
