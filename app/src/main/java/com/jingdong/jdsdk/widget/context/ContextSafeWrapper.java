package com.jingdong.jdsdk.widget.context;

import android.content.Context;
import android.content.ContextWrapper;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

public class ContextSafeWrapper extends ContextWrapper {
    private static final String TAG = "JDToast";

    private static final class ApplicationContextSafeWrapper extends ContextWrapper {
        public ApplicationContextSafeWrapper(Context context) {
            super(context);
        }

        public Object getSystemService(String str) {
            if ("window".equals(str)) {
                return new WindowManagerSafeWrapper((WindowManager) super.getSystemService(str));
            }
            return super.getSystemService(str);
        }
    }

    private static final class WindowManagerSafeWrapper implements WindowManager {
        private WindowManager base;

        public WindowManagerSafeWrapper(WindowManager windowManager) {
            this.base = windowManager;
        }

        public Display getDefaultDisplay() {
            return this.base.getDefaultDisplay();
        }

        public void removeViewImmediate(View view) {
            try {
                this.base.removeViewImmediate(view);
            } catch (Exception unused) {
            }
        }

        public void addView(View view, LayoutParams layoutParams) {
            addView(view, layoutParams, true);
        }

        private boolean isAddedException(Exception exc) {
            boolean z = false;
            if (exc == null) {
                return false;
            }
            String exc2 = exc.toString();
            String str = "has already been added to the window manager";
            if ((exc instanceof IllegalStateException) && exc2.contains(str)) {
                z = true;
            }
            return z;
        }

        private void tryDealAddedException(View view, LayoutParams layoutParams) {
            removeViewImmediate(view);
            addView(view, layoutParams, false);
        }

        private void addView(View view, LayoutParams layoutParams, boolean z) {
            try {
                this.base.addView(view, layoutParams);
            } catch (Exception e) {
                if (z && isAddedException(e)) {
                    tryDealAddedException(view, layoutParams);
                }
            }
        }

        public void updateViewLayout(View view, LayoutParams layoutParams) {
            this.base.updateViewLayout(view, layoutParams);
        }

        @Override
        public void addView(View view, ViewGroup.LayoutParams params) {

        }

        @Override
        public void updateViewLayout(View view, ViewGroup.LayoutParams params) {

        }

        public void removeView(View view) {
            try {
                this.base.removeView(view);
            } catch (Exception unused) {
            }
        }
    }

    public ContextSafeWrapper(Context context) {
        super(context);
    }

    public Context getApplicationContext() {
        return new ApplicationContextSafeWrapper(super.getApplicationContext());
    }
}
