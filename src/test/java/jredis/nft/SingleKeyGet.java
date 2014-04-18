package jredis.nft;

import static org.junit.Assert.assertTrue;
import redis.clients.jedis.Jedis;

public class SingleKeyGet extends NftBase {

    private static final int PORT = 15000;
    private static final int REQUESTS = 10000;
    private static final int WARM_UP = 100;

    //@Test
    public void test_single_key_get() {
        final Jedis jedis = new Jedis("localhost", PORT);
        
        TestCase testCase = new TestCase() {
            @Override
            public void run() {
                jedis.get("MyKey");
            }
        };
        
        int[] dist = execute(testCase, WARM_UP, REQUESTS);
        int max = printDist(dist);
        
        assertTrue(max < 7);
        
        jedis.quit();
    }

    //@Test
    public void test_single_key_set() {
        final Jedis jedis = new Jedis("localhost", PORT);
        
        TestCase testCase = new TestCase() {
            @Override
            public void run() {
                jedis.set("Venga", "Kia");
            }
        };
        
        int[] dist = execute(testCase, WARM_UP, REQUESTS);
        int max = printDist(dist);
        
        assertTrue(max < 7);
        
        jedis.quit();
    }


}
