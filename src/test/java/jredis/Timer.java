package jredis;

/**
 * Timer can be used to time an operation.
 * 
 * @author anoopelias
 *
 */
public class Timer {
    
    private long startTime;
    
    /**
     * Constructor sets the timer by default.
     * 
     */
    public Timer() {
        set();
    }
    
    /**
     * Set (or Reset) the timer.
     * 
     */
    public void set() {
        startTime = System.nanoTime();
    }
    
    /**
     * Get time in milli seconds.
     * 
     * @return time in milli seconds.
     */
    public long time() {
        return nanoTime() / 1000000 ;
    }
    
    /**
     * Get time in nano seconds.
     * 
     * @return
     */
    public long nanoTime() {
        return (System.nanoTime() - startTime);
    }

}
