package jredis;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SynchronizationTest {
    
    @Before
    public void setup() {
        DB.INSTANCE.clear();
    }

    @After
    public void tearDown() {
        DB.INSTANCE.clear();
    }

    @Test
    public void test_map_synch() throws InterruptedException,
            ExecutionException {
        
        List<Future<Boolean>> futures = new ArrayList<>();
        int n = 100;

        ExecutorService executorService = Executors.newFixedThreadPool(n);

        for (int i = 0; i < n; i++) {
            Future<Boolean> future = executorService
                    .submit(new Case(i));
            
            futures.add(future);
        }

        for (Future<Boolean> future : futures)
            assertTrue(future.get());

    }

    private class Case implements Callable<Boolean> {

        private int id;

        private Case(int id) {
            this.id = id;
        }

        @Override
        public Boolean call() throws Exception {
            Boolean isConsistent = true;

            for (int i = 0; i < 100; i++) {
                String key = id + "_key_" + i;
                String val = id + "_val_" + i;

                DB.INSTANCE.put(key, val);
                String newVal = DB.INSTANCE.get(key, String.class);

                if (newVal == null || !val.equals(newVal)) {
                    isConsistent = false;
                    break;
                }

            }
            return isConsistent;
        }

    }


}
