package jredis.nft;

/**
 * Base classes for non-functional test cases.
 * 
 * @author anoopelias
 * 
 */
public class NftBaseTest {

    /**
     * Print distribution.
     * 
     * @param dist
     * @return
     */
    protected int printDist(int[] dist) {
        int max = -1;
        for (int i = dist.length - 1; i >= 0; i--) {
            if (dist[i] != 0) {
                max = i + 1;
                break;
            }
        }

        for (int i = 0; i < max; i++) {
            System.out.println(i + " : " + dist[i]);
        }
        return max;
    }

    /**
     * Execute the given testcase as many number of times and return the
     * results.
     * 
     * @param testCase
     * @param warmUp
     * @param count
     * @return
     */
    protected int[] execute(TestCase testCase, int warmUp, int count) {
        int[] dist = new int[1000];

        for (int i = 0; i < warmUp; i++)
            testCase.run();

        for (int i = 0; i < count; i++) {
            Timer timer = new Timer();
            testCase.run();
            dist[(int) timer.milliTime()]++;
        }

        return dist;
    }

    protected interface TestCase {
        void run();
    }

}
