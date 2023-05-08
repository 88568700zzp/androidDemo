package com.jingdong.jdsdk.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.jingdong.jdsdk.JdSdk;
import com.jingdong.jdsdk.widget.p038a.NewToastUtils;
import com.jingdong.jdsdk.widget.p038a.ToastCenterStyle;
import com.jingdong.jdsdk.widget.p038a.ToastDefaultStyle;

@Deprecated
public class ToastUtils {
    /* access modifiers changed from: private */
    public static JDToast centerToast;
    /* access modifiers changed from: private */
    public static JDToast centerToastNoIcon;
    private static Handler mHandler;
    /* access modifiers changed from: private */
    public static JDToast sToast;

    private static Handler getHandler() {
        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
        }
        return mHandler;
    }

    private static void showToastPrivate(Context context, int i, int i2) {
        if (context != null) {
            showToastPrivate(context, context.getString(i), i2);
        }
    }

    private static void showToastPrivate(Context context, String str, int i) {
        if (!NewToastUtils.bBD) {
            NewToastUtils.m914a(new ToastDefaultStyle());
            NewToastUtils.m918l(str);
            return;
        }
        getHandler().post(new C2137a(context, str, i));
    }

    private static void showToastPrivateY(Context context, String str, int i) {
        if (!NewToastUtils.bBD) {
            NewToastUtils.m914a(new ToastDefaultStyle());
            NewToastUtils.m918l(str);
            return;
        }
        getHandler().post(new C2139b(context, str, i));
    }

    public static void showToastY(String str) {
        showToastPrivateY(JdSdk.getInstance().getApplicationContext(), str, 0);
    }

    public static void showToastY(int i) {
        showToastPrivateY(JdSdk.getInstance().getApplicationContext(), JdSdk.getInstance().getApplicationContext().getString(i), 0);
    }

    public static void longToast(Context context, int i) {
        showToastPrivate(context, i, 1);
    }

    public static void longToast(Context context, String str) {
        showToastPrivate(context, str, 1);
    }

    public static void longToast(int i) {
        showToastPrivate(JdSdk.getInstance().getApplicationContext(), i, 1);
    }

    public static void longToast(String str) {
        showToastPrivate(JdSdk.getInstance().getApplicationContext(), str, 1);
    }

    public static void shortToast(Context context, String str) {
        showToastPrivate(context, str, 0);
    }

    public static void shortToast(String str) {
        shortToast(JdSdk.getInstance().getApplicationContext(), str);
    }

    public static void shortToast(int i) {
        showToastPrivate(JdSdk.getInstance().getApplicationContext(), i, 0);
    }

    public static void shortToast(Context context, int i) {
        showToastPrivate(context, i, 0);
    }

    public static void showToast(Context context, String str) {
        longToast(str);
    }

    public static void showToastWithNetworkAvailable(Context context, String str) {

    }

    public static void showToast(String str) {
        longToast(str);
    }

    public static void showToastInCenter(Context context, byte b, String str, int i) {
        if (NewToastUtils.bBD) {
            try {
                newCenter(context, str, b);
                return;
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
        getHandler().post(new C2140c(context, str, b, i));
    }

    public static void showToastInCenter(Context context, int i, String str, int i2) {
        if (!NewToastUtils.bBD) {
            try {
                newCenter(context, str, i);
                return;
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
        getHandler().post(new C2141d(context, str, i, i2));
    }

    public static void showToastInCenter(Context context, String str, int i) {
        getHandler().post(new C2142e(context, str, i));
    }

    public static void showToastInCenter(String str) {
        showToastInCenter(JdSdk.getInstance().getApplicationContext(), str, 0);
    }

    private static void newCenter(Context context, String str, int i) throws Throwable {

    }

    private static void newCenter(Context context, String str, byte b) throws Throwable {

    }
}
