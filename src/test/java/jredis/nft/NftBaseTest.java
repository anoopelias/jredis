package jredis.nft;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import jredis.BaseTest;

import org.junit.After;
import org.junit.Before;

/**
 * Base classes for non-functional test cases.
 * 
 * @author anoopelias
 * 
 */
public class NftBaseTest extends BaseTest {

    @Before
    public void baseSetup() {
        startServer();
    }

    @After
    public void baseTearDown() {
        stopServer();
    }

    /**
     * Print distribution.
     * 
     * @param dist
     * @return
     */
    protected void printDist(Long[][] times) {
        int[] dist = new int[100];
        for(int i=0; i<times.length; i++) {
            for(int j=0; j<times[i].length; j++) {
                dist[times[i][j].intValue()]++;
            }
        }
        
        int total = times.length * times[0].length;
        int cumulative = 0;
        for(int i=0; i<dist.length; i++) {
            cumulative += dist[i];
            System.out.println( i + "\t : " + (((double)cumulative / total) * 100));
            
            if(cumulative == total)
                break;
        }
    }

    /**
     * Execute the given testcase as many number of times and return the
     * results.
     * 
     * @param testCase
     * @param threads
     * @param times
     * @return
     */
    protected Long[][] execute(NftTestCase testCase, int threads, int times) {
        try {
            Long[][] time = new Long[threads][times];
            List<Future<Long[]>> executions = new ArrayList<>();
            ExecutorService executorService = Executors
                    .newFixedThreadPool(threads);

            for (int i = 0; i < threads; i++) {
                Callable<Long[]> c = new NftExecution(testCase, times);
                executions.add(executorService.submit(c));
            }

            for (int i = 0; i < threads; i++)
                time[i] = executions.get(i).get();

            return time;
            
        } catch (InterruptedException | ExecutionException e) {
            throw new AssertionError(e);
        }
    }

}
