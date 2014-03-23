package jredis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import jredis.nft.Timer;

import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

public class ServerTest {

    private static final int SERVER_START_TIMEOUT = 2000; // mSecs

    private static String HOST = "localhost";
    private static int PORT = 15000;

    public ServerTest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Server server = new Server();
                server.start();
            }
        }).start();

        waitForServerStartup();
    }

    @Before
    public void setup() {
        DataMap.INSTANCE.clear();
    }

    private void waitForServerStartup() {
        Socket tempSocket = null;
        Timer timer = new Timer();
        while (tempSocket == null && timer.milliTime() < SERVER_START_TIMEOUT) {

            try (Socket kkSocket = new Socket(HOST, PORT)) {

                tempSocket = kkSocket;
            } catch (UnknownHostException e) {
                throw new AssertionError("Unknown Host", e);
            } catch (IOException e) {
                // Eat the exception as we are waiting for server to start.
            }
        }

        if (tempSocket == null)
            throw new AssertionError("Failed to start server in time");
    }

    @Test
    public void test_server_request() throws UnknownHostException, IOException {
        Jedis jedis = new Jedis(HOST, PORT);
        Jedis jedis2 = new Jedis(HOST, PORT, 200000);
        
        keyTests(jedis, jedis2);
        bitTests(jedis);
        zTests(jedis2);
        setStringBitCombinationTests(jedis);
    }

    private void setStringBitCombinationTests(Jedis jedis) {
        jedis.set("Jack", "Nicholson");
        assertEquals("Nicholson", jedis.get("Jack"));
        jedis.setbit("Jack", 74, true);
        jedis.setbit("Jack", 75, true);
        assertEquals("Nicholson0", jedis.get("Jack"));
    }

    private void zTests(Jedis jedis2) {
        String key = "Numbers";
        Map<Double, String> val = new HashMap<>();
        val.put(5.0, "Five");
        jedis2.zadd(key, val);
        
        val.clear();
        val.put(8.0, "Eight");
        jedis2.zadd(key, val);
        
        assertEquals(Long.valueOf(2), jedis2.zcard(key));
        assertEquals(Long.valueOf(1), jedis2.zcount(key, 1.0, 6.0));
        
        Set<String> keys = jedis2.zrange(key, 0, 5);
        assertEquals(2, keys.size());
        Iterator<String> iterKeys = keys.iterator();
        assertEquals("Five", iterKeys.next());
        assertEquals("Eight", iterKeys.next());

        keys = jedis2.zrange(key, 0, 5);
        assertEquals(2, keys.size());
        iterKeys = keys.iterator();
        assertEquals("Five", iterKeys.next());
        assertEquals("Eight", iterKeys.next());

        Set<Tuple> tuples = jedis2.zrangeWithScores(key, 0, 0);
        assertEquals(1, tuples.size());
        Iterator<Tuple> iterTuples = tuples.iterator();
        
        Tuple tuple = iterTuples.next();
        assertEquals("Five", tuple.getElement());
        assertEquals(5.0, tuple.getScore(), 0.0);
    }

    private void bitTests(Jedis jedis) {
        String key = "Keys";
        assertFalse(jedis.setbit(key, 10, true));
        assertTrue(jedis.getbit(key, 10));
        assertFalse(jedis.getbit(key, 20));
    }

    private void keyTests(Jedis jedis, Jedis jedis2) {
        assertEquals("OK", jedis.set("Jack", "Dorsey").trim());
        assertEquals("OK", jedis2.set("Elon", "Musk").trim());

        assertEquals("Dorsey", jedis.get("Jack").trim());
        assertEquals("Musk", jedis2.get("Elon").trim());
        assertNull(jedis.get("Filipe"));
    }

}
