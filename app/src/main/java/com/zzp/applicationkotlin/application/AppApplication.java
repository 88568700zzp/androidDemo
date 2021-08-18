package com.zzp.applicationkotlin.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.Printer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.multidex.MultiDex;

import com.zzp.applicationkotlin.BitmapActivity;
import com.zzp.applicationkotlin.util.TimeMonitor;

import org.jetbrains.annotations.NotNull;

/**
 * Created by samzhang on 2021/2/24.
 */
public class AppApplication extends Application implements Application.ActivityLifecycleCallbacks {

    private final String TAG = "AppApplication";


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Log.d(TAG,"attachBaseContext");
        MultiDex.install(this);
        //CrashCatchHandler.getInstance().init(this);
        registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"onCreate");
        TimeMonitor.getGlobalInstance().start("AppApplication onCreate" );
        getMainLooper().setMessageLogging(new Printer() {
            @Override
            public void println(String x) {
                //Log.d("getMainLooper","println:" + x);
            }
        });
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        Log.d(TAG,"onActivityCreated:" + activity.getComponentName());
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        Log.d(TAG,"onActivityStarted:" + activity.getComponentName());
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        Log.d(TAG,"onActivityResumed:" + activity.getComponentName());
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        Log.d(TAG,"onActivityPaused:" + activity.getComponentName());
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        Log.d(TAG,"onActivityStopped:" + activity.getComponentName());
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
        Log.d(TAG,"onActivitySaveInstanceState:" + activity.getComponentName());
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        Log.d(TAG,"onActivityDestroyed:" + activity.getComponentName());
    }
}
