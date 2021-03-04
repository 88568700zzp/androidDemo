package com.zzp.applicationkotlin.instrumentation;

import android.app.Activity;
import android.app.Instrumentation;
import android.util.Log;

/**
 * Created by samzhang on 2021/2/25.
 */
public class InfoInstrumentation extends Instrumentation {

    private final String TAG = "InfoInstrumentation";

    public InfoInstrumentation() {
        super();
    }

    @Override
    public void callActivityOnStart(Activity activity) {
        super.callActivityOnStart(activity);
        Log.d(TAG,"callActivityOnStart:" + activity.getComponentName());
    }

    @Override
    public void callActivityOnResume(Activity activity) {
        super.callActivityOnResume(activity);
        Log.d(TAG,"callActivityOnResume:" + activity.getComponentName());
    }

    @Override
    public void callActivityOnStop(Activity activity) {
        super.callActivityOnStop(activity);
        Log.d(TAG,"callActivityOnStop:" + activity.getComponentName());
    }

    @Override
    public void callActivityOnPause(Activity activity) {
        super.callActivityOnPause(activity);
        Log.d(TAG,"callActivityOnPause:" + activity.getComponentName());
    }
}
