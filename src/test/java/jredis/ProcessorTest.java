package jredis;

import static jredis.TestUtil.COLON;
import static jredis.TestUtil.CRLF;
import static jredis.TestUtil.DOLLAR;
import static jredis.TestUtil.MINUS;
import static jredis.TestUtil.OK;
import static jredis.TestUtil.mockSocket;
import static jredis.TestUtil.toCommand;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayOutputStream;
import java.net.Socket;

import org.junit.Before;
import org.junit.Test;

public class ProcessorTest {


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

    private static String[] CMD_ZADD_ONE = { "ZADD", "Numbers", "1.0", "One" };
    private static String[] CMD_ZADD_TWO = { "ZADD", "Numbers", "2.0", "Two" };
    private static String[] CMD_ZADD_THREE = { "ZADD", "Numbers", "3.0",
            "Three" };
    private static String[] CMD_ZADD_FOUR = { "ZADD", "Numbers", "4.0", "Four" };

    private static String[] CMD_ZCARD = { "ZCARD", "Numbers" };
    private static String[] CMD_ZCOUNT = { "ZCOUNT", "Numbers", "1.5", "3.5" };
    private static String[] CMD_ZRANGE = { "ZRANGE", "Numbers", "1", "5" };

    private static String[] CMD_ZADD_INVALID = { "ZADD", "Elon", "1.0", "One" };
    private static String[] CMD_ZCARD_INVALID = { "ZCARD", "Elon" };
    private static String[] CMD_ZCOUNT_INVALID = { "ZCOUNT", "Elon", "1.5",
            "3.5" };
    private static String[] CMD_ZRANGE_INVALID = { "ZRANGE", "Elon", "1", "5" };

    @Before
    public void setup() {
        DB.INSTANCE.clear();
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

        assertTrue(os.toString().startsWith(OK + CRLF + COLON + "1"));
    }

    @Test
    public void test_setbit_invalid() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Socket socket = mockSocket(toCommand(CMD_SET)
                + toCommand(CMD_SETBIT_INVALID), os);

        Processor processor = new Processor(socket, 2L);
        processor.call();

        assertTrue(os.toString().startsWith(OK + CRLF + COLON + "1"));
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

        String[] output = os.toString().split(CRLF);

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

    @Test
    public void test_zadd_invalid() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Socket socket = mockSocket(toCommand(CMD_SET)
                + toCommand(CMD_ZADD_INVALID), os);

        Processor processor = new Processor(socket, 2L);
        processor.call();

        assertTrue(os.toString().startsWith(OK + CRLF + MINUS + "ERR "));
    }

    @Test
    public void test_zcard_invalid() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Socket socket = mockSocket(toCommand(CMD_SET)
                + toCommand(CMD_ZCARD_INVALID), os);

        Processor processor = new Processor(socket, 2L);
        processor.call();

        assertTrue(os.toString().startsWith(OK + CRLF + MINUS + "ERR "));
    }

    @Test
    public void test_zcount_invalid() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Socket socket = mockSocket(toCommand(CMD_SET)
                + toCommand(CMD_ZCOUNT_INVALID), os);

        Processor processor = new Processor(socket, 2L);
        processor.call();

        assertTrue(os.toString().startsWith(OK + CRLF + MINUS + "ERR "));
    }

    @Test
    public void test_zrange_invalid() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Socket socket = mockSocket(toCommand(CMD_SET)
                + toCommand(CMD_ZRANGE_INVALID), os);

        Processor processor = new Processor(socket, 2L);
        processor.call();

        assertTrue(os.toString().startsWith(OK + CRLF + MINUS + "ERR "));
    }

    @Test
    public void test_recovery_after_error() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Socket socket = mockSocket(toCommand(CMD_GET_INVALID)
                + toCommand(CMD_SET) + toCommand(CMD_GET), os);

        Processor processor = new Processor(socket, 2L);
        processor.call();

        String[] output = os.toString().split(CRLF);
        assertEquals(4, output.length);

        assertTrue(output[0].startsWith("" + MINUS + "ERR "));
        assertEquals(OK, output[1]);
        assertEquals("$4", output[2]);
        assertEquals("Musk", output[3]);
    }

}
