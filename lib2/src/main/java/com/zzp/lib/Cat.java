package com.zzp.lib;

/**
 * Created by samzhang on 2021/7/27.
 */
public class Cat implements ICat{

    public Runnable runnable;

    @Override
    public void doPrint(){
        System.out.println("doCat");

        runnable = new Runnable() {
            @Override
            public void run() {
            }
        };
    }


}
