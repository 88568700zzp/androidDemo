package com.zzp.lib;

import com.zzp.lib.model.SwipeData;

/**
 * Created by samzhang on 2021/12/8.
 */
public class SwipeTest {

    private static SwipeTest SAVE_HOOK = null;

    public static void doTest(){
        /*for(int i = 0;i < 1000;i++){
            SwipeData swipeData = new SwipeData();
        }*/
        SAVE_HOOK = new SwipeTest();
        SAVE_HOOK = null;
        System.gc();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(SAVE_HOOK != null){
            System.out.println("Yes , I am still alive");
        }else{
            System.out.println("No , I am dead");
        }
    }

    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();
        System.out.println("execute method finalize()");
        SAVE_HOOK = this;
    }
}
