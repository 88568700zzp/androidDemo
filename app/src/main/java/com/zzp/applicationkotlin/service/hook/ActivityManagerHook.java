package com.zzp.applicationkotlin.service.hook;

import android.content.Context;
import android.os.IBinder;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Description: 用来监控 ActivityManager 的所有行为
 *
 * @author samzhang
 * @since 2021-09-15
 */
public class ActivityManagerHook {

    private static final String TAG = ActivityManagerHook.class.getSimpleName();

    public static void hookService(Context context) {
        IBinder activityService = ServiceManager.getService(Context.ACTIVITY_SERVICE);
        String IActivity = "android.app.IActivityManager";

        if (activityService != null) {
            IBinder activityManagerService =
                    (IBinder) Proxy.newProxyInstance(activityService.getClass().getClassLoader(),
                            activityService.getClass().getInterfaces(),
                            new ServiceHook(activityService, IActivity, true, new ActivityManagerHookHandler()));
            ServiceManager.setService(Context.ACTIVITY_SERVICE, activityManagerService);
        } else {
            Log.e(TAG, "ActivityManagerHook hook failed!");
        }
    }

    public static class ActivityManagerHookHandler implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Log.e(TAG, "ActivityManagerHook invoke:" + method.getName());
            Exception e = new Exception("method show");
            e.printStackTrace();
            return method.invoke(proxy, args);
        }
    }
}
