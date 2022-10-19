package com.zzp.applicationkotlin.service.hook;

import android.content.Context;
import android.os.IBinder;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Description: 用来监控 TelephonyManager 的所有行为
 *
 * @author Shawn_Dut
 * @since 2019-02-13
 */
public class TelephonyManagerHook {

    private static final String TAG = TelephonyManagerHook.class.getSimpleName();

    public static void hookService(Context context) {
        IBinder telephonyService = ServiceManager.getService(Context.TELEPHONY_SERVICE);
        String ITelephony = "com.android.internal.telephony.ITelephony";

        if (telephonyService != null) {
            IBinder telephonyManagerService =
                    (IBinder) Proxy.newProxyInstance(telephonyService.getClass().getClassLoader(),
                            telephonyService.getClass().getInterfaces(),
                            new ServiceHook(telephonyService, ITelephony, true, new TelephonyHookHandler()));
            ServiceManager.setService(Context.TELEPHONY_SERVICE, telephonyManagerService);
        } else {
            Log.e(TAG, "TelephonyManagerHook hook failed!");
        }
    }

    public static class TelephonyHookHandler implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Log.e(TAG, "TelephonyHookHandler invoke:" + method.getName());
            Exception e = new Exception("method show");
            e.printStackTrace();
            return method.invoke(proxy, args);
        }
    }
}
