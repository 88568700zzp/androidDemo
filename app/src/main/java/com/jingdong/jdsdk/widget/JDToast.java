package com.jingdong.jdsdk.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build.VERSION;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jingdong.jdsdk.widget.context.ContextSafeWrapper;

public class JDToast extends Toast {
    public static final byte MODE_BOTTOM = 2;
    public static final byte MODE_BOTTOM_Y = 3;
    public static final byte MODE_CENTER = 1;
    public static final byte MODE_CENTER_NO_ICON = 4;
    public static final byte MODE_CUSTOM_CENTER = 5;
    public static final byte MODE_IMAGE_ERROR = 3;
    public static final byte MODE_IMAGE_EXCLAMATORY = 1;
    public static final byte MODE_IMAGE_TICK = 2;
    private static final String TAG = "JDToast";
    private FrameLayout centerRootLayout;
    private int currentMode;

    /* renamed from: iv */
    public ImageView f701iv;

    /* renamed from: tv */
    public TextView f702tv;

    public JDToast(Context context, byte b) {
        super(context);
        this.currentMode = b;
        switch (this.currentMode) {
            case 1:
                initCenterView(context);
                return;
            case 2:
                initBottomView(context);
                return;
            case 4:
                initCenterNoIconView(context);
                return;
            case 5:
                initCustomCenter(context);
                return;
            default:
                return;
        }
    }

    public JDToast(Context context, int i) {
        super(context);
        initBottomView(context, i);
    }

    private void initBottomView(Context context) {

    }

    private void initBottomView(Context context, int i) {

    }

    private void initCenterView(Context context) {

    }

    private void initCenterNoIconView(Context context) {

    }

    private void initCustomCenter(Context context) {

    }

    public void setCustomViewByCenter(View view) {
        FrameLayout frameLayout = this.centerRootLayout;
        if (frameLayout != null) {
            frameLayout.addView(view);
        }
    }

    public void setText(CharSequence charSequence) {
        TextView textView = this.f702tv;
        if (textView != null) {
            textView.setTypeface(Typeface.MONOSPACE);
            this.f702tv.setText(charSequence);
        }
    }

    public void setImage(byte b) {
        ImageView imageView = this.f701iv;
        if (imageView != null) {

        }
    }

    public void setImageResource(int i) {
        try {
            this.f701iv.setBackgroundResource(i);
        } catch (Exception unused) {
        }
    }

    private LayoutInflater getLayoutInflater(Context context) {
        if (VERSION.SDK_INT >= 24) {
            return LayoutInflater.from(context).cloneInContext(new ContextSafeWrapper(context));
        }
        return LayoutInflater.from(context);
    }
}
