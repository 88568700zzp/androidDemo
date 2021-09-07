package com.zzp.applicationkotlin.view

import android.graphics.Canvas
import android.graphics.Paint
import android.text.TextUtils
import android.text.style.ReplacementSpan
import com.zzp.applicationkotlin.view.doll.dp

/**
 *
 * Created by samzhang on 2021/9/1.
 */
class TimeDownSpan(var color:Int) : ReplacementSpan() {

    private var mSize = 0

    override fun getSize(
        paint: Paint,
        text: CharSequence?,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?
    ): Int {
        return if(TextUtils.isEmpty(text)){
            0
        }else {
            if(mSize == 0){
                mSize = (paint.measureText(text.toString().substring(start,end)) + 4f.dp).toInt()
            }
            mSize
        }
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence?,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        if(!TextUtils.isEmpty(text)){
            paint.color = color
            var str = text.toString().substring(start,end)
            var adjust = (mSize - paint.measureText(str))/2
            if(adjust < 0){
                adjust = 0f
            }
            canvas.drawText(str,x + adjust,y.toFloat(),paint)
        }

    }
}