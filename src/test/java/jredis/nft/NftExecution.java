package jredis.nft;

import java.util.concurrent.Callable;

public class NftExecution implements Callable<Long[]> {
    
    private NftTestCase testcase;
    private int times;
    
    public NftExecution(NftTestCase testcase, int times) {
        this.testcase = testcase;
        this.times = times;
        
    }

    @Override
    public Long[] call() throws Exception {
        Long[] time = new Long[times];
        for(int i=0; i<times; i++) {
            Timer timer = new Timer();
            testcase.run();
            time[i] = timer.milliTime();
        }
        
        return time;
    }


}
