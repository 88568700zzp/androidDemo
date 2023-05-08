package com.jingdong.jdsdk.widget.p038a;

import android.app.Application;

/* renamed from: com.jingdong.jdsdk.widget.a.e */
public final class SupportToast extends BaseToast {
    private final ToastHelper bBE;

    public SupportToast(Application application) {
        super(application);
        this.bBE = new ToastHelper(this, application);
    }

    public void show() {
        this.bBE.show();
    }

    public void cancel() {
        this.bBE.cancel();
    }
}
