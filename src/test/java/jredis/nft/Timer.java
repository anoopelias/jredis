package jredis.nft;

public class Timer {

    private long time;
    private long totalTime;

    public Timer() {
        reset();
    }

    public void reset() {
        totalTime = 0;
        start();
    }
    
    public void start() {
        time = System.nanoTime();
    }

    public void add() {
        totalTime += (System.nanoTime() - time);
    }

    public long time() {
        return (System.nanoTime() - time);
    }

    public long total() {
        return totalTime;
    }

}
