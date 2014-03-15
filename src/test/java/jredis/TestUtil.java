package jredis;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
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
        ByteArrayInputStream is = new ByteArrayInputStream(command.getBytes());

        when(socket.getInputStream()).thenReturn(is);
        when(socket.getOutputStream()).thenReturn(os);
        return socket;
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
            sb.append(arg.getBytes().length);
            sb.append(CRLF);
            sb.append(arg);
            sb.append(CRLF);
        }

        return sb.toString();
    }

}
