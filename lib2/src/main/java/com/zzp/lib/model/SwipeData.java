package com.zzp.lib.model;

/**
 * Created by samzhang on 2021/12/8.
 */
public class SwipeData {

    public static final int MAX_COUNT = 10;


    private int index = 0;
    private String name = "";

    private static Object mLock = new Object();

    public SwipeData next;

    private static int count;
    private static SwipeData mPool;

    public SwipeData(){
        //System.out.println("------SwipeData create------");
    }

    public static SwipeData obtain(){
        synchronized (mLock) {
            if (mPool != null) {
                SwipeData outData = mPool;
                mPool = mPool.next;
                outData.next = null;
                count--;
                return outData;
            }
        }
        return new SwipeData();
    }

    public void recycle(){
        synchronized (mLock) {
            if(mPool != null){
                if(count < MAX_COUNT){
                    SwipeData nextData = mPool;
                    mPool = this;
                    mPool.next = nextData;
                    count++;
                }
            }else{
                mPool = this;
                count++;
            }
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("------SwipeData finalize------");
    }
}
