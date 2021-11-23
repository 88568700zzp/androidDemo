package com.zzp.applicationkotlin.service.hook;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Description:
 *
 * @author Shawn_Dut
 * @since 2017-02-21
 */
public class PackageManagerHook {

    private static final String TAG = PackageManagerHook.class.getSimpleName();

    private static Object mStaticInstance = null;

    public static void hookService(Context context) {
        IBinder packageService =  ServiceManager.getService("package");

        if (packageService != null) {
            try {
                Class iPackageManagerClass = Class.forName("android.content.pm.IPackageManager");

                Class activityThread = Class.forName("android.app.ActivityThread");
                Field field = activityThread.getDeclaredField("sPackageManager");
                field.setAccessible(true);


                if(mStaticInstance == null) {
                    Method method = activityThread.getDeclaredMethod("getPackageManager");
                    mStaticInstance = method.invoke(activityThread);
                }

                Object proxyInstance = Proxy.newProxyInstance(context.getClassLoader(), new Class[]{iPackageManagerClass},
                        new PackageManagerHookHandler(mStaticInstance));

                field.set(activityThread,proxyInstance);

                PackageManager packageManager =context.getPackageManager();
                Field mPM = packageManager.getClass().getDeclaredField("mPM");
                mPM.setAccessible(true);
                mPM.set(packageManager,proxyInstance);
            } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            Log.e(TAG, "packageService hook failed!");
        }
    }

    private static void nullPackageManager(Context context){
        try {
            Class contextClass = Class.forName("android.app.ContextImpl");
            Field field = contextClass.getDeclaredField("mPackageManager");
            field.setAccessible(true);
            field.set(context,null);
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static class PackageManagerHookHandler implements InvocationHandler {

        private Object mInstance;

        public PackageManagerHookHandler(Object instance){
            mInstance = instance;
        }


        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Log.e(TAG, "PackageManagerHookHandler invoke:" + method.getName());
            Exception e = new Exception("method show");
            e.printStackTrace();
            return method.invoke(mInstance, args);
        }
    }
}
