package jredis.nft;

import org.junit.Test;

import redis.clients.jedis.Jedis;

public class SingleKeyGet extends NftBaseTest {

    private static final int PORT = 15000;

    @Test
    public void test_single_key_get() {
        final Jedis jedis = new Jedis("localhost", PORT);
        
        NftTestCase testCase = new NftTestCase() {
            @Override
            public void run() {
                jedis.get("MyKey");
            }
        };
        
        Long[][] times = execute(testCase, 1, 10000);
        printDist(times);
        jedis.quit();
    }

    @Test
    public void test_single_key_set() {
        final Jedis jedis = new Jedis("localhost", PORT);
        
        NftTestCase testCase = new NftTestCase() {
            @Override
            public void run() {
                jedis.set("Venga", "Kia");
            }
        };
        
        Long[][] times = execute(testCase, 1, 10000);
        printDist(times);
        jedis.quit();
    }


}
