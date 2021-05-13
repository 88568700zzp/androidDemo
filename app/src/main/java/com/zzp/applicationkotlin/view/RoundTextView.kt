package com.zzp.applicationkotlin.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import com.zzp.applicationkotlin.R


/**
 * 圆角textView
 * Created by samzhang on 2021/5/11.
 */
class RoundTextView(context:Context, attrs: AttributeSet):AppCompatTextView(context,attrs){

    private var mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    @ColorInt
    private var mStrokeColor:Int = Color.WHITE
    private var mHasStroke = false
    private var mStrokeWidth:Float = 10f

    @ColorInt
    private var mFillColor:Int = Color.RED
    private var mRadius:Float = 0f

    private var mRectF = RectF()

    private var mHasPress = true
    private var mPressAlpha = 0.6f
    private var mNormalAlpha =1f

    private var mLinearType = 0
    private var mLinearGradient:LinearGradient ?= null

    @ColorInt
    private var mStartColor = Color.WHITE
    @ColorInt
    private var mEndColor = Color.WHITE

    init {
        context.theme
            .obtainStyledAttributes(attrs, R.styleable.RoundTextView, 0, 0).apply {
                try {
                    mHasStroke = getBoolean(R.styleable.RoundTextView_has_stroke, false)
                    mHasPress = getBoolean(R.styleable.RoundTextView_has_press, true)
                    mStrokeWidth = getDimension(R.styleable.RoundTextView_stroke_width, 3f)
                    mRadius = getDimension(R.styleable.RoundTextView_radius, -1f)
                    mFillColor = getColor(R.styleable.RoundTextView_fill_color,Color.WHITE)
                    mStrokeColor = getColor(R.styleable.RoundTextView_stroke_color,Color.WHITE)
                    mPressAlpha = getFloat(R.styleable.RoundTextView_press_alpha,0.7f)
                    mNormalAlpha = getFloat(R.styleable.RoundTextView_normal_alpha,1f)
                    mLinearType = getInt(R.styleable.RoundTextView_linear_type,0)
                    mStartColor = getColor(R.styleable.RoundTextView_start_color,Color.WHITE)
                    mEndColor = getColor(R.styleable.RoundTextView_end_color,Color.WHITE)
                } finally {
                    recycle()
                }
            }
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()
        if(mHasPress){
            alpha = if(isPressed) mPressAlpha else mNormalAlpha
        }
    }

    override fun onDraw(canvas: Canvas?) {
        var radius = mRadius
        if(radius > height/2 || radius <= 0f){
            radius = height/2f
        }

        mRectF.set(0f,0f,width.toFloat(),height.toFloat())

        if(mLinearType == 0) {
            mPaint.color = mFillColor
            mPaint.style = Paint.Style.FILL
        }else{
            if(mLinearGradient == null){
                if(mLinearType == 1) {
                    mLinearGradient = LinearGradient(
                        0f,
                        0f,
                        width.toFloat(),
                        0f,
                        mStartColor,
                        mEndColor,
                        Shader.TileMode.CLAMP
                    )
                }else{
                    mLinearGradient = LinearGradient(
                        width.toFloat()/2,
                        0f,
                        width.toFloat()/2,
                        height.toFloat(),
                        mStartColor,
                        mEndColor,
                        Shader.TileMode.CLAMP
                    )
                }
            }
            mPaint.shader = mLinearGradient
            mPaint.style = Paint.Style.FILL
        }
        canvas?.drawRoundRect(mRectF,radius,radius,mPaint)

        mPaint.shader = null
        if(mHasStroke){
            mPaint.style = Paint.Style.STROKE
            mPaint.color = mStrokeColor
            mPaint.strokeWidth = mStrokeWidth
            mRectF.inset(mStrokeWidth/2,mStrokeWidth/2)
            canvas?.drawRoundRect(mRectF,radius - mStrokeWidth/2,radius - mStrokeWidth/2,mPaint)
        }

        super.onDraw(canvas)
    }

}