package com.jingdong.jdsdk.widget.p038a;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.WindowManager.BadTokenException;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;


/* renamed from: com.jingdong.jdsdk.widget.a.i */
public final class ToastHelper extends Handler {
    private final WindowHelper bBG;
    private boolean isShow;
    private final String mPackageName;
    private final Toast mToast;

    public ToastHelper(Toast toast, Application application) {
        super(Looper.getMainLooper());
        this.mToast = toast;
        this.mPackageName = application.getPackageName();
        this.bBG = WindowHelper.m923a(this, application);
    }

    public void handleMessage(Message message) {
        cancel();
    }

    public void show() {
        if (!this.isShow) {
            LayoutParams layoutParams = new LayoutParams();
            layoutParams.height = -2;
            layoutParams.width = -2;
            layoutParams.format = -3;
            layoutParams.windowAnimations = 16973828;
            layoutParams.setTitle("Toast");
            layoutParams.flags = 152;
            layoutParams.packageName = this.mPackageName;
            layoutParams.gravity = this.mToast.getGravity();
            layoutParams.x = this.mToast.getXOffset();
            layoutParams.y = this.mToast.getYOffset();
            try {
                this.bBG.getWindowManager().addView(this.mToast.getView(), layoutParams);
                this.isShow = true;
                sendEmptyMessageDelayed(0, Toast.LENGTH_SHORT);
            } catch (BadTokenException | IllegalStateException | NullPointerException unused) {
            }
        }
    }

    public void cancel() {
        removeMessages(0);
        if (this.isShow) {
            try {
                this.bBG.getWindowManager().removeView(this.mToast.getView());
            } catch (IllegalArgumentException | NullPointerException unused) {
            }
            this.isShow = false;
        }
    }
}
