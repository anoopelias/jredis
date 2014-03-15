package jredis;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import org.junit.Test;

public class ProcessorTest {

    private static final char STAR = '*';
    private static final char DOLLAR = '$';
    private static final char MINUS = '-';
    private static final String OK = "+OK";
    private static String CRLF = "\r\n";

    private static String[] CMD_SET = { "SET", "Elon", "Musk" };
    private static String[] CMD_GET = { "GET", "Elon" };
    private static String CMD_RESP = OK + CRLF + DOLLAR + "4" + CRLF + "Musk"
            + CRLF;

    private static String[] CMD_GET_NIL = { "GET", "Jeff" };
    private static String CMD_RESP_NIL = "" + DOLLAR + MINUS + "1" + CRLF;

    @Test
    public void test_set_get() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Socket socket = mockSocket(toCommand(CMD_SET) + toCommand(CMD_GET), os);

        Processor processor = new Processor(socket, 1L);
        processor.call();

        verify(socket).getInputStream();
        verify(socket).getOutputStream();
        verify(socket).close();

        assertEquals(CMD_RESP, os.toString());
    }

    @Test
    public void test_get_nil() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Socket socket = mockSocket(toCommand(CMD_GET_NIL), os);

        Processor processor = new Processor(socket, 2L);
        processor.call();

        assertEquals(CMD_RESP_NIL, os.toString());
    }

    private Socket mockSocket(String command, OutputStream os)
            throws IOException {

        Socket socket = mock(Socket.class);
        ByteArrayInputStream is = new ByteArrayInputStream(command.getBytes());

        when(socket.getInputStream()).thenReturn(is);
        when(socket.getOutputStream()).thenReturn(os);
        return socket;
    }

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
