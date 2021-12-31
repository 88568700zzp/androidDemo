package com.zzp.applicationkotlin;

public class NDKTool {

    static {
        System.loadLibrary("ndkdemotest-jni");
    }

    public static native String getStringFromNDK();
}
