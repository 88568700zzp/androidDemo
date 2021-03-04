package com.zzp.addemo.util;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by samzhang on 2021/3/2.
 */
public class SystemUtil {

    public static float dipToPx(Context context,float dp){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.density * dp;
    }
}
