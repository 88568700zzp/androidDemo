package com.jingdong.jdsdk.widget;

import android.content.Context;
import android.text.TextUtils;

/* renamed from: com.jingdong.jdsdk.widget.d */
/* compiled from: ToastUtils */
final class C2141d implements Runnable {
    final /* synthetic */ Context val$context;
    final /* synthetic */ int val$drawableId;
    final /* synthetic */ int val$duration;
    final /* synthetic */ String val$message;

    C2141d(Context context, String str, int i, int i2) {
        this.val$context = context;
        this.val$message = str;
        this.val$drawableId = i;
        this.val$duration = i2;
    }

    public void run() {
        if (this.val$context != null && !TextUtils.isEmpty(this.val$message)) {
            if (ToastUtils.centerToast != null) {
                ToastUtils.centerToast.cancel();
            }
            ToastUtils.centerToast = new JDToast(this.val$context.getApplicationContext(), 1);
            ToastUtils.centerToast.setImageResource(this.val$drawableId);
            ToastUtils.centerToast.setText(this.val$message);
            ToastUtils.centerToast.setDuration(this.val$duration);
            ToastUtils.centerToast.show();
        }
    }
}
