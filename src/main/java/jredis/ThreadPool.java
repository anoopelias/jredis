package jredis;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class ThreadPool {
    
    public static ThreadPool INSTANCE = new ThreadPool();
    private ExecutorService service = null;
    
    private ThreadPool() {
        // TODO : Make threadpool size configurable.
        service = Executors.newFixedThreadPool(5);
        
    }
    
    public <T> Future<T> submit(Callable<T> task) {
        return service.submit(task);
    }
}
