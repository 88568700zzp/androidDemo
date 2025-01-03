package com.zzp.applicationkotlin.service.hook;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import java.lang.reflect.Field;
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
        try {
            Field singletonField;
            Class<?> iActivityManagerClass;
            // 1，获取Instrumentation中调用startActivity(,intent,)方法的对象
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                Thread.sleep(500);
                // 10.0以上是ActivityTaskManager中的IActivityTaskManagerSingleton
                Class<?> activityTaskManagerClass = Class.forName("android.app.ActivityTaskManager");
                singletonField = activityTaskManagerClass.getDeclaredField("IActivityTaskManagerSingleton");
                iActivityManagerClass = Class.forName("android.app.IActivityTaskManager");
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // 8.0,9.0在ActivityManager类中IActivityManagerSingleton
                Class activityManagerClass = ActivityManager.class;
                singletonField = activityManagerClass.getDeclaredField("IActivityManagerSingleton");
                iActivityManagerClass = Class.forName("android.app.IActivityManager");
            } else {
                // 8.0以下在ActivityManagerNative类中 gDefault
                Class<?> activityManagerNative = Class.forName("android.app.ActivityManagerNative");
                singletonField = activityManagerNative.getDeclaredField("gDefault");
                iActivityManagerClass = Class.forName("android.app.IActivityManager");
            }
            singletonField.setAccessible(true);
            Object singleton = singletonField.get(null);

            // 2，获取Singleton中的mInstance，也就是要代理的对象
            Class<?> singletonClass = Class.forName("android.util.Singleton");
            Field mInstanceField = singletonClass.getDeclaredField("mInstance");
            mInstanceField.setAccessible(true);
            /* Object mInstance = mInstanceField.get(singleton); */
            Method getMethod = singletonClass.getDeclaredMethod("get");
            Object mInstance = getMethod.invoke(singleton);
            if (mInstance == null) {
                return;
            }
            // 3，对IActivityManager进行动态代理
            Object proxyInstance = Proxy.newProxyInstance(context.getClassLoader(), new Class[]{iActivityManagerClass},
                    new ActivityManagerHookHandler(mInstance));
            // 4，把代理赋值给IActivityManager类型的mInstance对象
            mInstanceField.set(singleton, proxyInstance);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class ActivityManagerHookHandler implements InvocationHandler {

        private Object mInstance;

        public ActivityManagerHookHandler(Object instance){
            mInstance = instance;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Log.e(TAG, "ActivityManagerHook invoke:" + method.getName());
            Exception e = new Exception("method show");
            e.printStackTrace();
            return method.invoke(mInstance, args);
        }
    }
}
