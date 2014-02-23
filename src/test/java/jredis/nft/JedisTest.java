package jredis.nft;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Test;

import redis.clients.jedis.Jedis;

public class JedisTest {

    private static final int CONCURRENCY = 70;
    private static final int REPEAT = 50;
    private static final int TEST_POOL_SIZE = 200;

    @Test
    public void test_jedis_concurrent() throws InterruptedException,
            ExecutionException {
        for(int i=0; i<30; i++) {
            
            testConcurrent(6379);
            testConcurrent(15000);
            System.out.println();
            
        }
    }
    
    private void testConcurrent(int port) throws InterruptedException,
            ExecutionException {
        
        String key = "MyKey";
        String value = "MyValue";

        List<Callable<Object>> callables = new ArrayList<Callable<Object>>();
        ExecutorService executor = Executors.newFixedThreadPool(TEST_POOL_SIZE);

        for (int i = 0; i < CONCURRENCY * REPEAT; i += REPEAT) {
            Callable<Object> call = new PutAndGet(port, key, value, i);
            callables.add(call);
        }

        Timer timer = new Timer();
        List<Future<Object>> futures = executor.invokeAll(callables);
        for (Future<Object> future : futures)
            future.get();

        System.out.print("Total Time (" + port + ") : \t" + timer.time() + "\t");
    }

    private class PutAndGet implements Callable<Object> {

        private int port;
        private String key;
        private String value;
        private int index;

        private PutAndGet(int port, String key, String value, int index) {
            this.port = port;
            this.key = key;
            this.value = value;
            this.index = index;
        }

        public Object call() throws Exception {
            Jedis jedis = new Jedis("localhost", port, 2000);

            for (int i = 0; i < REPEAT; i++) {
                String indexString = (index + i) + "";
                String keyString = key + indexString;
                String valueString = value + indexString;

                jedis.set(keyString, valueString);
                assertEquals(valueString, jedis.get(keyString));
            }

            return value;
        }

    }

}
