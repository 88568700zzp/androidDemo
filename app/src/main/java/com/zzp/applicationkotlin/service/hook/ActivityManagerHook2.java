package com.zzp.applicationkotlin.service.hook;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
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
public class ActivityManagerHook2 {

    private static final String TAG = ActivityManagerHook2.class.getSimpleName();

    public static void hookService(Context context) {
        try {
            Field singletonField;
            Class<?> iActivityManagerClass;

            Class activityManagerClass = ActivityManager.class;
            singletonField = activityManagerClass.getDeclaredField("IActivityManagerSingleton");
            iActivityManagerClass = Class.forName("android.app.IActivityManager");

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
