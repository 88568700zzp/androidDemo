package com.zzp.applicationkotlin.view

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.zzp.applicationkotlin.util.TimeMonitor

/**
 *
 * Created by samzhang on 2021/7/28.
 */
class MonitorLinearLayout(context:Context,attrs: AttributeSet):LinearLayout(context,attrs){

    private var mMonitor = TimeMonitor()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        mMonitor.start()
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mMonitor.record("MonitorLinearLayout onMeasure")
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        mMonitor.start()
        super.onLayout(changed, l, t, r, b)
        mMonitor.record("MonitorLinearLayout onLayout")
    }
}