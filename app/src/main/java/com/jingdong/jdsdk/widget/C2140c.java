package com.jingdong.jdsdk.widget;

import android.content.Context;
import android.text.TextUtils;

/* renamed from: com.jingdong.jdsdk.widget.c */
/* compiled from: ToastUtils */
final class C2140c implements Runnable {
    final /* synthetic */ Context val$context;
    final /* synthetic */ int val$duration;
    final /* synthetic */ String val$message;
    final /* synthetic */ byte val$modeImage;

    C2140c(Context context, String str, byte b, int i) {
        this.val$context = context;
        this.val$message = str;
        this.val$modeImage = b;
        this.val$duration = i;
    }

    public void run() {
        if (this.val$context != null && !TextUtils.isEmpty(this.val$message)) {
            if (ToastUtils.centerToast != null) {
                ToastUtils.centerToast.cancel();
            }
            ToastUtils.centerToast = new JDToast(this.val$context.getApplicationContext(), 1);
            ToastUtils.centerToast.setImage(this.val$modeImage);
            ToastUtils.centerToast.setText(this.val$message);
            ToastUtils.centerToast.setDuration(this.val$duration);
            ToastUtils.centerToast.show();
        }
    }
}
