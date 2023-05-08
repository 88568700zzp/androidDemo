package com.jingdong.jdsdk.widget.p038a;

import android.app.Application;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/* renamed from: com.jingdong.jdsdk.widget.a.a */
public class BaseToast extends Toast {
    private TextView mMessageView;

    BaseToast(Application application) {
        super(application);
    }

    public void setView(View view) {
        super.setView(view);
        this.mMessageView = m911N(view);
    }

    public void setText(CharSequence charSequence) {
        this.mMessageView.setText(charSequence);
    }

    /* renamed from: N */
    private static TextView m911N(View view) {
        if (view instanceof TextView) {
            return (TextView) view;
        }
        if (view.findViewById(16908299) instanceof TextView) {
            return (TextView) view.findViewById(16908299);
        }
        if (view instanceof ViewGroup) {
            TextView f = m912f((ViewGroup) view);
            if (f != null) {
                return f;
            }
        }
        throw new IllegalArgumentException("The layout must contain a TextView");
    }

    /* renamed from: f */
    private static TextView m912f(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View childAt = viewGroup.getChildAt(i);
            if (childAt instanceof TextView) {
                return (TextView) childAt;
            }
            if (childAt instanceof ViewGroup) {
                TextView f = m912f((ViewGroup) childAt);
                if (f != null) {
                    return f;
                }
            }
        }
        return null;
    }
}
