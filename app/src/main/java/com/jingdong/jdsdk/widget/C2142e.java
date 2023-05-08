package com.jingdong.jdsdk.widget;

import android.content.Context;
import android.text.TextUtils;

/* renamed from: com.jingdong.jdsdk.widget.e */
/* compiled from: ToastUtils */
final class C2142e implements Runnable {
    final /* synthetic */ Context val$context;
    final /* synthetic */ int val$duration;
    final /* synthetic */ String val$message;

    C2142e(Context context, String str, int i) {
        this.val$context = context;
        this.val$message = str;
        this.val$duration = i;
    }

    public void run() {
        if (this.val$context != null && !TextUtils.isEmpty(this.val$message)) {
            if (ToastUtils.centerToastNoIcon != null) {
                ToastUtils.centerToastNoIcon.cancel();
            }
            ToastUtils.centerToastNoIcon = new JDToast(this.val$context.getApplicationContext(), 4);
            ToastUtils.centerToastNoIcon.setText(this.val$message);
            ToastUtils.centerToastNoIcon.setDuration(this.val$duration);
            ToastUtils.centerToastNoIcon.show();
        }
    }
}
