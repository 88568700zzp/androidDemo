package com.zzp.applicationkotlin.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.FrameLayout
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.view.View


class GrayFrameLayout(context: Context, attrs: AttributeSet) : FrameLayout(context,attrs){

    init {
        setLayerType(View.LAYER_TYPE_HARDWARE, getMatrix(1))
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

    }

    private fun getMatrix(type:Int):Paint{
        var paint =  Paint()
        var cm =  ColorMatrix()
        when(type){
            0->{
                cm.setSaturation(1.0f);
            }
            1->{
                cm.setSaturation(0f);
            }
        }
        paint.colorFilter = ColorMatrixColorFilter(cm);
        return paint;
    }

}