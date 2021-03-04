package com.zzp.applicationkotlin.util;

import android.util.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by samzhang on 2021/3/4.
 */
public class ThreadPoolUtil {

    private static class SingletonHoler {
        /**
         * 静态初始化器，由JVM来保证线程安全
         */
        private static ThreadPoolUtil instance = new ThreadPoolUtil();
    }

    private ThreadPoolExecutor executor;

    public static ThreadPoolUtil getInstance(){
        return SingletonHoler.instance;
    }

    public ThreadPoolUtil() {
        this.executor = new ThreadPoolExecutor(3, Integer.MAX_VALUE, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {
            int index = 0;

            @Override
            public Thread newThread(Runnable r) {
                String name = "Thread-" + index;
                Thread thread = new Thread(r);
                thread.setName(name);
                Log.d("ThreadPoolUtil","create thread:" + name);
                index++;
                return thread;
            }
        });
    }

    public void execute(Runnable runnable){
        this.executor.execute(runnable);
    }
}
