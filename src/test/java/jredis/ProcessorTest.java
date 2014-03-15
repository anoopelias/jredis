package jredis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import org.junit.Before;
import org.junit.Test;

public class ProcessorTest {

    private static final char STAR = '*';
    private static final char DOLLAR = '$';
    private static final char MINUS = '-';
    private static final char COLON = ':';
    private static final String OK = "+OK";
    private static String CRLF = "\r\n";

    private static String[] CMD_SET = { "SET", "Elon", "Musk" };
    private static String[] CMD_GET = { "GET", "Elon" };
    private static String CMD_RESP = OK + CRLF + DOLLAR + "4" + CRLF + "Musk"
            + CRLF;

    private static String[] CMD_GET_INVALID = { "GET", "Elon", "Musk" };

    private static String[] CMD_GET_NIL = { "GET", "Jeff" };
    private static String CMD_RESP_NIL = "" + DOLLAR + MINUS + "1" + CRLF;

    private static String[] CMD_SETBIT = { "SETBIT", "Alpha", "5", "1" };
    private static String[] CMD_GETBIT = { "GETBIT", "Alpha", "5" };
    private static String CMD_BIT_RESP = "" + COLON + "0" + CRLF + COLON + "1"
            + CRLF;

    private static String[] CMD_GETBIT_INVALID = { "GETBIT", "Elon", "5" };
    private static String[] CMD_SETBIT_INVALID = { "SETBIT", "Elon", "5", "1" };

    private static String[] CMD_ZADD_ONE = { "ZADD", "Numbers",  "1.0", "One" };
    private static String[] CMD_ZADD_TWO = { "ZADD", "Numbers", "2.0", "Two" };
    private static String[] CMD_ZADD_THREE = { "ZADD", "Numbers", "3.0", "Three" };
    private static String[] CMD_ZADD_FOUR = { "ZADD", "Numbers", "4.0", "Four" };

    private static String[] CMD_ZCARD = { "ZCARD", "Numbers"};
    private static String[] CMD_ZCOUNT = { "ZCOUNT", "Numbers", "1.5", "3.5"};
    private static String[] CMD_ZRANGE = { "ZRANGE", "Numbers", "1", "5"};

    @Before
    public void setup() {
        DataMap.INSTANCE.clear();
    }

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

    @Test
    public void test_get_invalid() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Socket socket = mockSocket(toCommand(CMD_GET_INVALID), os);

        Processor processor = new Processor(socket, 2L);
        processor.call();

        assertTrue(os.toString().startsWith("" + MINUS + "ERR "));
    }

    @Test
    public void test_setbit_getbit() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Socket socket = mockSocket(toCommand(CMD_SETBIT)
                + toCommand(CMD_GETBIT), os);

        Processor processor = new Processor(socket, 2L);
        processor.call();

        assertEquals(CMD_BIT_RESP, os.toString());
    }

    @Test
    public void test_getbit_invalid() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Socket socket = mockSocket(toCommand(CMD_SET)
                + toCommand(CMD_GETBIT_INVALID), os);

        Processor processor = new Processor(socket, 2L);
        processor.call();

        assertTrue(os.toString().startsWith(OK + CRLF + MINUS + "ERR "));
    }

    @Test
    public void test_setbit_invalid() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Socket socket = mockSocket(toCommand(CMD_SET)
                + toCommand(CMD_SETBIT_INVALID), os);

        Processor processor = new Processor(socket, 2L);
        processor.call();

        assertTrue(os.toString().startsWith(OK + CRLF + MINUS + "ERR "));
    }

    @Test
    public void test_zset() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        StringBuilder commands = new StringBuilder();
        commands.append(toCommand(CMD_ZADD_THREE));
        commands.append(toCommand(CMD_ZADD_TWO));
        commands.append(toCommand(CMD_ZADD_FOUR));
        commands.append(toCommand(CMD_ZADD_ONE));
        
        commands.append(toCommand(CMD_ZCARD));
        commands.append(toCommand(CMD_ZCOUNT));
        commands.append(toCommand(CMD_ZRANGE));
        
        Socket socket = mockSocket(commands.toString(), os);

        Processor processor = new Processor(socket, 2L);
        processor.call();

        String[] output = os.toString().split("\r\n");
        
        assertEquals(13, output.length);
        
        assertEquals(":1", output[0]);
        assertEquals(":1", output[1]);
        assertEquals(":1", output[2]);
        assertEquals(":1", output[3]);
        
        assertEquals(":4", output[4]);
        assertEquals(":2", output[5]);
        
        assertEquals("*3", output[6]);
        assertEquals("$3", output[7]);
        assertEquals("Two", output[8]);

        assertEquals("$5", output[9]);
        assertEquals("Three", output[10]);

        assertEquals("$4", output[11]);
        assertEquals("Four", output[12]);

    }
    
    /*
     * TODO : One invalid command followed by one valid command.
     * 
     */

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
