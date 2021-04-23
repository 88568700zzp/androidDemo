package com.zzp.applicationkotlin.view.doll

import android.content.res.Resources
import android.util.TypedValue

/**
 *
 * Created by samzhang on 2021/4/12.
 */

val Float.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    )

val Int.dp
    get() = this.toFloat().dp.toInt()