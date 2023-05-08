package com.jingdong.common.utils;


import android.app.Application;
import android.content.Context;

public class BitmapkitUtils {
    public static final String API_KEY = "XJgK2J9rXdmAH37ilm";
    private static final int RETRY_TIMES = 3;
    private static final String TAG = "BitmapkitUtils";


    /* renamed from: a */
    public static Application a;

    private static boolean b;
    public static boolean isBMPLoad;

    /* renamed from: a */
    public static native String m18303a(String... strArr);

    public static native byte[] encodeJni(byte[] bArr, boolean z);

    public static native String getSignFromJni(Context context, String str, String str2, String str3, String str4, String str5);

    public static native String getstring(String str);

    public static void load(Context context){

        a = (Application) context.getApplicationContext();

        System.loadLibrary("jdbitmapkit");

        System.out.println(BitmapkitUtils.getstring("6666"));
    }
}
