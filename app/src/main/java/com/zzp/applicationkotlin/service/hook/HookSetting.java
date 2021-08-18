package com.zzp.applicationkotlin.service.hook;

import android.content.Context;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashSet;

/**
 * Created by samzhang on 2021/7/29.
 */
public class HookSetting {

    private static final String TAG = "HookSetting";

    public static void hookService(Context context) {

        Settings.Secure.getString(
                context.getContentResolver(), Settings.Secure.ANDROID_ID);

        String IContentProvider = "android.content.IContentProvider";

        boolean success = true;

        try {
            Class cls = Class.forName("android.provider.Settings$Secure");
            Field sProviderHolderField = cls.getDeclaredField("sProviderHolder");
            sProviderHolderField.setAccessible(true);
            Object sProviderHolder = sProviderHolderField.get(Settings.Secure.class);

            Log.d(TAG,"sProviderHolder:" + sProviderHolder);

            Field contentProviderField = sProviderHolder.getClass().getDeclaredField("mContentProvider");
            contentProviderField.setAccessible(true);
            Object contentProvider =  contentProviderField.get(sProviderHolder);

            Class ccontentProvider = Class.forName(IContentProvider);

            Log.d(TAG,"contentProvider:" + contentProvider + " result:" + (contentProvider instanceof IBinder));

            /*IBinder contentProviderProxy =
                    (IBinder) Proxy.newProxyInstance(contentProvider.getClass().getClassLoader(),
                            contentProvider.getClass().getInterfaces(),
                            new ServiceHook(contentProvider, IContentProvider, true, new SettingInvocationHandler()));

            contentProviderField.set(sProviderHolder,contentProviderProxy);*/
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            e.printStackTrace();
            success = false;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            success = false;
        }

        if (success) {
            Log.i(TAG, "HookSetting hook success!");
        } else {
            Log.e(TAG, "HookSetting hook failed!");
        }
    }

    private static class SettingInvocationHandler implements InvocationHandler{

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Log.e(TAG, "SettingInvocationHandler invoke:" + method.getName());
            return method.invoke(proxy, args);
        }
    }
}
