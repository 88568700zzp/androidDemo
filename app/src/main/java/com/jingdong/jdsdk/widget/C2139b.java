package com.jingdong.jdsdk.widget;

import android.content.Context;
import android.text.TextUtils;


/* renamed from: com.jingdong.jdsdk.widget.b */
/* compiled from: ToastUtils */
final class C2139b implements Runnable {
    final /* synthetic */ Context val$context;
    final /* synthetic */ int val$duration;
    final /* synthetic */ String val$msg;

    C2139b(Context context, String str, int i) {
        this.val$context = context;
        this.val$msg = str;
        this.val$duration = i;
    }

    public void run() {
        if (this.val$context != null && !TextUtils.isEmpty(this.val$msg)) {
            if (ToastUtils.sToast != null) {
                ToastUtils.sToast.cancel();
            }
            ToastUtils.sToast = new JDToast(this.val$context.getApplicationContext(), 300);
            ToastUtils.sToast.setText(this.val$msg);
            ToastUtils.sToast.setDuration(this.val$duration);
            ToastUtils.sToast.show();
        }
    }
}
