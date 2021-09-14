package com.zzp.applicationkotlin.view

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.zzp.applicationkotlin.R
import com.zzp.applicationkotlin.view.doll.dp
import kotlin.math.cos
import kotlin.math.sin

/**
 *
 * Created by samzhang on 2021/7/5.
 */
@SuppressLint("ResourceType")
class TestSpeedView(context: Context, attrs: AttributeSet) :ViewGroup(context, attrs){

    private var mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mBigLineWidth = 10f.dp
    private var mSmallLineWidth = 8f.dp

    private var mCircleX = 0F
    private var mCircleY = 0F
    private var mRadius = 0F
    private var mStrokeWidth = 2f.dp

    private var mMaxDistance = 0f

    private var mUnSelColor = Color.WHITE
    private var mSelColor = Color.parseColor("#FF00FF6B")

    private var mProgress = 100

    private var mRect = RectF()

    init {
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.ZZpTest, 0, 0
        )
        var text = a.getString(R.styleable.ZZpTest_zzp_text)
        Log.d("TestSpeedView","text:${text} ZZpText:${Integer.toHexString(R.styleable.ZZpTest[0])} ZZpColor:${Integer.toHexString(R.styleable.ZZpTest[1])} text:${Integer.toHexString(R.styleable.ZZpTest_zzp_text)} color:${Integer.toHexString(R.styleable.ZZpTest_zzp_customColor)}")
    }

    private var mClockBitmap = BitmapFactory.decodeResource(
        resources,
        R.drawable.ic_speed_test_clock
    )


    override fun dispatchDraw(canvas: Canvas?)  {
        super.dispatchDraw(canvas)

        drawPointView(canvas)

        drawArc(canvas)
    }

    private fun drawPointView(canvas: Canvas?){
        canvas?.save()

        canvas?.rotate(valueToDegrees(), mCircleX, mCircleY)

        var startX = mCircleX - mClockBitmap.width/2
        var startY = mCircleY - mClockBitmap.height + 24f.dp
        mRect.set(startX, startY, (startX + mClockBitmap.width), startY + mClockBitmap.height)

        mPaint.style = Paint.Style.FILL
        canvas?.drawBitmap(mClockBitmap, null, mRect, mPaint)
        canvas?.restore()
    }

    private fun drawArc(canvas: Canvas?){

        mPaint.strokeWidth = mStrokeWidth

        var currentDegree = valueToDegrees() + 90

        var startDegree = -40
        while(startDegree <= 220){
            var startX = caculateX(startDegree.toDouble(), mRadius) + mBigLineWidth
            var startY = caculateY(startDegree.toDouble(), mRadius) + mBigLineWidth

            var endX = caculateX(startDegree.toDouble(), mRadius + mBigLineWidth)
            var endY = caculateY(startDegree.toDouble(), mRadius + mBigLineWidth)

            if(currentDegree >= startDegree){
                mPaint.color = mSelColor
            }else{
                mPaint.color = mUnSelColor
            }

            mPaint.alpha = (((mMaxDistance - startY)/mMaxDistance * (1-0.3f) + 0.3f) * 255).toInt()

            canvas?.drawLine(
                startX.toFloat(),
                startY.toFloat(),
                endX.toFloat(),
                endY.toFloat(),
                mPaint
            )

            startDegree += 3
        }

        mPaint.alpha = 255
    }

    private fun caculateX(degree: Double, radius: Float):Double{
        val b = Math.toRadians(degree)
        return (1 - cos(b)) * radius
    }

    private fun caculateY(degree: Double, radius: Float):Double{
        val b = Math.toRadians(degree)
        return (1 - sin(b)) * radius
    }

    private fun valueToDegrees():Float{
        return -130F + 260f * (mProgress)/100
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        mCircleX = (width/2).toFloat()
        mCircleY = (height/2).toFloat()
        if(width > height){
            mRadius =(height/2).toFloat() -  mBigLineWidth
        }else{
            mRadius =(width/2).toFloat() -  mBigLineWidth
        }
        mMaxDistance = (((1 - sin(Math.toRadians(-40.0))) * mRadius).toFloat())
        for(i in 0 until childCount){
            var child = getChildAt(i)
            if(child.visibility != View.GONE){
                var measureWidth = child.measuredWidth
                var measureHeight = child.measuredHeight
                var startX = (width - measureWidth)/2
                var startY = (height - measureHeight)/2
                child.layout(startX, startY, startX + measureWidth, startY + measureHeight)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        for(i in 0 until childCount){
            var child = getChildAt(i)
            if(child.visibility != View.GONE){
                measureChild(child, widthMeasureSpec, heightMeasureSpec)
            }
        }
    }

    fun startProgress(){
        var animator = ValueAnimator.ofInt(0, 100)
        animator.addUpdateListener {
            mProgress = it.animatedValue as Int
            invalidate()
        }
        animator.duration = 2000
        animator.start()
    }
}