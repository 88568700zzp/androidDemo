package com.zzp.lib;

/**
 * Created by samzhang on 2021/12/1.
 */
public class WaitTest {

    private final String TAG = "WaitTest";

    private Object lock = new Object();

    public void doTest(){
        try {
            new Thread(){
                @Override
                public void run() {
                    System.out.println("run");
                    try {
                        sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (lock) {
                        lock.notify();
                    }
                    System.out.println("lock.notify()");
                }
            }.start();
            synchronized (lock) {
                lock.wait();
            }
            System.out.println("doTest end");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



}
