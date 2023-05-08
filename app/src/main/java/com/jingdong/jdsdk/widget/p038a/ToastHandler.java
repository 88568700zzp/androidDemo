package com.jingdong.jdsdk.widget.p038a;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/* renamed from: com.jingdong.jdsdk.widget.a.h */
final class ToastHandler extends Handler {
    private volatile Queue<CharSequence> bBF = new ArrayBlockingQueue(3);
    private volatile boolean isShow;
    private final Toast mToast;

    ToastHandler(Toast toast) {
        super(Looper.getMainLooper());
        this.mToast = toast;
    }

    /* access modifiers changed from: 0000 */
    /* renamed from: m */
    public void mo12024m(CharSequence charSequence) {
        if ((this.bBF.isEmpty() || !this.bBF.contains(charSequence)) && !this.bBF.offer(charSequence)) {
            this.bBF.poll();
            this.bBF.offer(charSequence);
        }
    }

    /* access modifiers changed from: 0000 */
    public void show() {
        if (!this.isShow) {
            this.isShow = true;
            sendEmptyMessageDelayed(1, 300);
        }
    }

    public void handleMessage(Message message) {
        switch (message.what) {
            case 1:
                CharSequence charSequence = (CharSequence) this.bBF.peek();
                if (charSequence != null) {
                    this.mToast.setText(charSequence);
                    this.mToast.show();
                    sendEmptyMessageDelayed(2, (long) (m919n(charSequence) + 300));
                    return;
                }
                this.isShow = false;
                return;
            case 2:
                this.bBF.poll();
                if (!this.bBF.isEmpty()) {
                    sendEmptyMessage(1);
                    return;
                } else {
                    this.isShow = false;
                    return;
                }
            case 3:
                this.isShow = false;
                this.bBF.clear();
                this.mToast.cancel();
                return;
            default:
                return;
        }
    }

    /* renamed from: n */
    private static int m919n(CharSequence charSequence) {
        return charSequence.length() > 20 ? 3500 : 2000;
    }
}
