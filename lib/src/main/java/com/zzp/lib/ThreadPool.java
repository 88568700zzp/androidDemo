package com.zzp.lib;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by samzhang on 2021/8/17.
 */
public class ThreadPool {

    public static ThreadPool mInstance = new ThreadPool();

    private ExecutorService service = new ThreadPoolExecutor(4, 4, Integer.MAX_VALUE, TimeUnit.SECONDS, new PriorityBlockingQueue<Runnable>(), new ThreadFactory() {
        @Override
        public Thread newThread(@NotNull Runnable runnable) {
            Thread thread = new Thread();
            return thread;
        }
    });

    private ThreadPool(){}

    public void executor(Runnable runnable){
        service.execute(runnable);
    }

    public void quit(){
        if(!service.isShutdown()) {
            service.shutdown();
        }
    }

}
