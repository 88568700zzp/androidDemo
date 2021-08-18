package com.zzp.applicationkotlin.service.hook;

import android.content.Context;
import android.os.IBinder;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Description: 用来监控 LocationManagerHook 的所有行为
 *
 * @author Shawn_Dut
 * @since 2019-02-13
 */
public class LocationManagerHook {

    private static final String TAG = LocationManagerHook.class.getSimpleName();

    public static void hookService(Context context) {
        IBinder locationService = ServiceManager.getService(Context.LOCATION_SERVICE);
        String ILocation = "android.location.ILocationManager";

        if (locationService != null) {
            IBinder locationManagerService =
                    (IBinder) Proxy.newProxyInstance(locationService.getClass().getClassLoader(),
                            locationService.getClass().getInterfaces(),
                            new ServiceHook(locationService, ILocation, true, new LocationManagerHookHandler()));
            ServiceManager.setService(Context.LOCATION_SERVICE, locationManagerService);
        } else {
            Log.e(TAG, "LocationManagerHook hook failed!");
        }
    }

    public static class LocationManagerHookHandler implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Log.e(TAG, "LocationManagerHookHandler invoke:" + method.getName());
            Exception e = new Exception("method show");
            e.printStackTrace();
            return method.invoke(proxy, args);
        }
    }
}
