package com.jingdong.jdsdk.widget.p038a;

import android.app.AppOpsManager;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.GradientDrawable;
import android.os.Build.VERSION;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;

/* renamed from: com.jingdong.jdsdk.widget.a.c */
public final class NewToastUtils {
    private static IToastStyle bBA = null;
    private static Toast bBB = null;
    public static Application bBC = null;
    public static boolean bBD = true;
    private static ToastHandler bBz;

    public static void init(Application application) {
        bBC = application;
        try {
            if (bBA == null) {
                m914a(new ToastDefaultStyle());
            }
            if (m917cF(application)) {
                bBD = true;
                if (VERSION.SDK_INT == 25) {
                    m915b(new SafeToast(application));
                } else {
                    m915b(new BaseToast(application));
                }
            } else {
                bBD = false;
                m915b(new SupportToast(application));
            }
            setView((View) m916cE(application.getApplicationContext()));
            setGravity(bBA.getGravity(), bBA.getXOffset(), bBA.getYOffset());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0022, code lost:
        return;
     */
    /* renamed from: l */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized void m918l(CharSequence r3) {
        /*
            java.lang.Class<com.jingdong.jdsdk.widget.a.c> r0 = com.jingdong.jdsdk.widget.p038a.NewToastUtils.class
            monitor-enter(r0)
            m913UR()     // Catch:{ all -> 0x0023 }
            if (r3 == 0) goto L_0x0021
            java.lang.String r1 = ""
            java.lang.String r2 = r3.toString()     // Catch:{ all -> 0x0023 }
            boolean r1 = r1.equals(r2)     // Catch:{ all -> 0x0023 }
            if (r1 == 0) goto L_0x0015
            goto L_0x0021
        L_0x0015:
            com.jingdong.jdsdk.widget.a.h r1 = bBz     // Catch:{ all -> 0x0023 }
            r1.mo12024m(r3)     // Catch:{ all -> 0x0023 }
            com.jingdong.jdsdk.widget.a.h r3 = bBz     // Catch:{ all -> 0x0023 }
            r3.show()     // Catch:{ all -> 0x0023 }
            monitor-exit(r0)
            return
        L_0x0021:
            monitor-exit(r0)
            return
        L_0x0023:
            r3 = move-exception
            monitor-exit(r0)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jingdong.jdsdk.widget.p038a.NewToastUtils.m918l(java.lang.CharSequence):void");
    }

    public static void setGravity(int i, int i2, int i3) {
        m913UR();
        if (VERSION.SDK_INT >= 17) {
            i = Gravity.getAbsoluteGravity(i, bBB.getView().getResources().getConfiguration().getLayoutDirection());
        }
        bBB.setGravity(i, i2, i3);
    }

    public static void setView(int i) {
        m913UR();
        setView(View.inflate(bBB.getView().getContext().getApplicationContext(), i, null));
    }

    public static void setView(View view) {
        m913UR();
        if (view != null && view.getContext() == view.getContext().getApplicationContext()) {
            Toast toast = bBB;
            if (toast != null) {
                toast.cancel();
                bBB.setView(view);
            }
        }
    }

    /* renamed from: a */
    public static void m914a(IToastStyle bVar) {
        bBA = bVar;
        Toast toast = bBB;
        if (toast != null) {
            toast.cancel();
            bBB.setView(m916cE(bBC.getApplicationContext()));
            bBB.setGravity(bBA.getGravity(), bBA.getXOffset(), bBA.getYOffset());
        }
    }

    /* renamed from: b */
    public static void m915b(Toast toast) {
        bBB = toast;
        bBz = new ToastHandler(toast);
    }

    /* renamed from: UR */
    private static void m913UR() {
        if (bBB == null) {
            throw new IllegalStateException("NewToastUtils has not been initialized");
        }
    }

    /* renamed from: cE */
    private static TextView m916cE(Context context) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(bBA.getBackgroundColor());
        gradientDrawable.setCornerRadius(TypedValue.applyDimension(1, (float) bBA.getCornerRadius(), context.getResources().getDisplayMetrics()));
        TextView textView = new TextView(context);
        textView.setId(16908299);
        textView.setTextColor(bBA.getTextColor());
        textView.setTextSize(0, TypedValue.applyDimension(2, bBA.getTextSize(), context.getResources().getDisplayMetrics()));
        textView.setPadding((int) TypedValue.applyDimension(1, (float) bBA.getPaddingLeft(), context.getResources().getDisplayMetrics()), (int) TypedValue.applyDimension(1, (float) bBA.getPaddingTop(), context.getResources().getDisplayMetrics()), (int) TypedValue.applyDimension(1, (float) bBA.getPaddingRight(), context.getResources().getDisplayMetrics()), (int) TypedValue.applyDimension(1, (float) bBA.getPaddingBottom(), context.getResources().getDisplayMetrics()));
        textView.setLayoutParams(new LayoutParams(-2, -2));
        if (VERSION.SDK_INT >= 16) {
            textView.setBackground(gradientDrawable);
        } else {
            textView.setBackgroundDrawable(gradientDrawable);
        }
        if (VERSION.SDK_INT >= 21) {
            textView.setZ((float) bBA.getZ());
        }
        if (bBA.getMaxLines() > 0) {
            textView.setMaxLines(bBA.getMaxLines());
        }
        return textView;
    }

    /* renamed from: cF */
    private static boolean m917cF(Context context) {
        if (VERSION.SDK_INT >= 24) {
            return ((NotificationManager) context.getSystemService("notification")).areNotificationsEnabled();
        }
        boolean z = true;
        if (VERSION.SDK_INT < 19) {
            return true;
        }
        AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService("appops");
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        String packageName = context.getApplicationContext().getPackageName();
        int i = applicationInfo.uid;
        try {
            Class cls = Class.forName(AppOpsManager.class.getName());
            if (((Integer) cls.getMethod("checkOpNoThrow", new Class[]{Integer.TYPE, Integer.TYPE, String.class}).invoke(appOpsManager, new Object[]{Integer.valueOf(((Integer) cls.getDeclaredField("OP_POST_NOTIFICATION").get(Integer.class)).intValue()), Integer.valueOf(i), packageName})).intValue() != 0) {
                z = false;
            }
            return z;
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException | NoSuchMethodException | RuntimeException | InvocationTargetException unused) {
            return true;
        }
    }
}
