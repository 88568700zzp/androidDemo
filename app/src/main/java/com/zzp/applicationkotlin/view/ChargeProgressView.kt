package com.zzp.applicationkotlin.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.zzp.applicationkotlin.view.doll.dp

/**
 * 电池进度
 * Created by samzhang on 2021/9/22.
 */
class ChargeProgressView(context: Context,attr:AttributeSet?) : View(context,attr){

    private var mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var mStrokeWidth = 15f.dp

    private var mGreenShader:Shader ?= null
    private var mGreenBg = Color.parseColor("#FFE0F6E8")

    private var mRedShader:Shader ?= null
    private var mRedBg = Color.parseColor("#FFFFF1E6")

    private var mRectF = RectF()

    private var mProgress = 0

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        mRectF.set(mStrokeWidth/2,mStrokeWidth/2,width.toFloat() - mStrokeWidth/2,height.toFloat() - mStrokeWidth/2)

        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = mStrokeWidth
        mPaint.strokeCap = Paint.Cap.ROUND

        if(mProgress <= 50) {
            mPaint.color = mRedBg
            canvas?.drawCircle((width/2).toFloat(), (height/2).toFloat(), (width/2).toFloat()- mStrokeWidth/2,mPaint)
            mPaint.shader = mRedShader
        }else{
            mPaint.color = mGreenBg
            canvas?.drawCircle((width/2).toFloat(), (height/2).toFloat(), (width/2).toFloat() - mStrokeWidth/2,mPaint)
            mPaint.shader = mGreenShader
        }
        canvas?.drawArc(mRectF,-90f,getAngle(),false,mPaint)
        mPaint.shader = null
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        mGreenShader = LinearGradient(0f,0f,0f,height.toFloat(), Color.parseColor("#FFCCEA00"),Color.parseColor("#FF00CB4C"),
            Shader.TileMode.CLAMP)

        mRedShader = LinearGradient(0f,0f,0f,height.toFloat(), Color.parseColor("#FFFF6A00"),Color.parseColor("#FFFFA300"),
            Shader.TileMode.CLAMP)
    }

    private fun getAngle():Float{
        return mProgress * 360f/100
    }

    fun toProgress(progress:Int){
        var valueAnimate = ValueAnimator.ofInt(0,progress)
        valueAnimate.addUpdateListener {
            mProgress = it.animatedValue as Int
            postInvalidate()
        }

        valueAnimate.duration = 800L
        valueAnimate.start()
    }
}