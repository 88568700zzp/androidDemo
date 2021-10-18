package com.monitor.aptdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;

import com.zzp.annotation.Hello;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@Hello(value = "MainActivity")
public class MainActivity extends AppCompatActivity {

    PowerManager powerManager = null;

    @Hello(value = "zzp")
    String zzp = "p2312";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        Log.d("zzp123","enter powerManager:" + powerManager.isPowerSaveMode());
        Method[] methods = PowerManager.class.getDeclaredMethods();
        Method saveMode = null;
        for(Method method:methods){
            if(TextUtils.equals(method.getName(),"setPowerSaveMode")){
                saveMode = method;
            }
            Log.d("zzp123","method:" + method.getName());
        }
        if(saveMode != null){
            try {
                saveMode.invoke(powerManager,true);
                Log.d("zzp123","saveMode success");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(PowerManager.ACTION_POWER_SAVE_MODE_CHANGED);
        filter.addAction("miui.intent.action.POWER_SAVE_MODE_CHANGED");
        registerReceiver(mPowerReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mPowerReceiver);
    }

    private BroadcastReceiver mPowerReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("zzp123","powerManager:" + powerManager.isPowerSaveMode());
            isBackground(context);
            isBackgroundTask(context);
        }
    };

    public static boolean isBackground(Context context) {

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        boolean isBackground = true;
        String processName = "empty";
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            Log.d("zzp1234","runningAppProcessInfo:"+ appProcess.processName);
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_CACHED) {
            } else if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                    || appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
                Log.d("zzp1234","isBackground:"+ appProcess.processName);
            } else {
            }
            if (appProcess.processName.equals(context.getPackageName())) {
                processName = appProcess.processName;
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_CACHED) {
                    isBackground = true;
                } else if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                        || appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
                    isBackground = false;
                } else {
                    isBackground = true;
                }
            }
        }
        if (isBackground) {
            Log.d("zzp123", "后台:" + processName);
        } else {
            Log.d("zzp123", "前台+" + processName);
        }
        return isBackground;
    }

    public boolean isBackgroundTask(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = ((ActivityManager.RunningTaskInfo) tasks.get(0)).topActivity;
            Log.d("zzp1234","isBackgroundTask:"+ topActivity.getPackageName());
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                Log.d("zzp123", "isBackgroundTask 后台");
                return true;
            }
        }

        Log.d("zzp123", "isBackgroundTask 前台");
        return false;
    }


}