package com.jingdong.jdsdk.widget.p038a;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.WindowManager;

@TargetApi(19)
/* renamed from: com.jingdong.jdsdk.widget.a.j */
final class WindowHelper implements ActivityLifecycleCallbacks {
    private final ToastHelper bBE;
    private final ArrayMap<String, Activity> bBH = new ArrayMap<>();
    private String bBI;

    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    public void onActivityStopped(Activity activity) {
    }

    private WindowHelper(ToastHelper iVar) {
        this.bBE = iVar;
    }

    /* renamed from: a */
    static WindowHelper m923a(ToastHelper iVar, Application application) {
        WindowHelper jVar = new WindowHelper(iVar);
        application.registerActivityLifecycleCallbacks(jVar);
        return jVar;
    }

    /* access modifiers changed from: 0000 */
    public WindowManager getWindowManager() throws NullPointerException {
        String str = this.bBI;
        if (str != null) {
            Activity activity = (Activity) this.bBH.get(str);
            if (activity != null) {
                return m921I(activity);
            }
        }
        throw new NullPointerException();
    }

    public void onActivityCreated(Activity activity, Bundle bundle) {
        this.bBI = m922K(activity);
        this.bBH.put(this.bBI, activity);
    }

    public void onActivityStarted(Activity activity) {
        this.bBI = m922K(activity);
    }

    public void onActivityResumed(Activity activity) {
        this.bBI = m922K(activity);
    }

    public void onActivityPaused(Activity activity) {
        this.bBE.cancel();
    }

    public void onActivityDestroyed(Activity activity) {
        this.bBH.remove(m922K(activity));
        if (m922K(activity).equals(this.bBI)) {
            this.bBI = null;
        }
    }

    /* renamed from: K */
    private static String m922K(Object obj) {
        StringBuilder sb = new StringBuilder();
        sb.append(obj.getClass().getName());
        sb.append(Integer.toHexString(obj.hashCode()));
        return sb.toString();
    }

    /* renamed from: I */
    private static WindowManager m921I(Activity activity) {
        return (WindowManager) activity.getSystemService("window");
    }
}
