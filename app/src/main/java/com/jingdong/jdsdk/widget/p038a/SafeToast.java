package com.jingdong.jdsdk.widget.p038a;

import android.app.Application;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager.BadTokenException;
import android.widget.Toast;

import java.lang.reflect.Field;

/* renamed from: com.jingdong.jdsdk.widget.a.d */
public final class SafeToast extends BaseToast {

    /* renamed from: com.jingdong.jdsdk.widget.a.d$a */
    /* compiled from: SafeToast */
    static final class C2138a extends Handler {
        private Handler mHandler;

        C2138a(Handler handler) {
            this.mHandler = handler;
        }

        public void handleMessage(Message message) {
            try {
                this.mHandler.handleMessage(message);
            } catch (BadTokenException unused) {
            }
        }

        public void dispatchMessage(Message message) {
            this.mHandler.dispatchMessage(message);
        }
    }

    SafeToast(Application application) {
        super(application);
        try {
            Field declaredField = Toast.class.getDeclaredField("mTN");
            declaredField.setAccessible(true);
            Object obj = declaredField.get(this);
            Field declaredField2 = declaredField.getType().getDeclaredField("mHandler");
            declaredField2.setAccessible(true);
            declaredField2.set(obj, new C2138a((Handler) declaredField2.get(obj)));
        } catch (Exception unused) {
        }
    }
}
