package com.zzp.applicationkotlin.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.animation.addListener
import com.zzp.applicationkotlin.view.doll.dp

/**
 * wifi倒计时布局
 * Created by samzhang on 2021/8/27.
 */
class WifiReportCountView(context: Context, attr: AttributeSet?) : View(context,attr){

    private val MSG_REFRESH = 0x123
    private val MSG_TEXT = 0x124

    private var mPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private var mTextColor = Color.parseColor("#FF00F689")

    private var mTextWidth = 0
    private var mTextHeight = 0
    private var mDrawTxtY = 0f
    private var mScrollX = 0f
    private var mMaxScrollX = 0f

    private var mReportTxt = "报告生成中，24小时后记得来看看喔~全面了解自己的网络状况~"
    private var mMode = 0

    private var mValueAnimate:ValueAnimator ?= null

    private var leftTime:Long = 0L

    fun resetLeftTime(time:Long) {

        leftTime = time

        invalidate()

        mHandler.removeMessages(MSG_REFRESH)
        mHandler.sendEmptyMessageDelayed(MSG_REFRESH,1000L)

        mHandler.removeMessages(MSG_TEXT)
        mHandler.sendEmptyMessageDelayed(MSG_TEXT,10000L)
    }


    private var mHandler:Handler = object :Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if(msg.what == MSG_REFRESH){
                mRunnble.run()
            }else{
                mAnimateRunnbale.run()
            }
        }
    }

    private var mRunnble = object :Runnable{
        override fun run() {
            leftTime -= 1000

            invalidate()

            mHandler.removeMessages(MSG_REFRESH)
            mHandler.sendEmptyMessageDelayed(MSG_REFRESH,1000L)
        }
    }

    private var mAnimateRunnbale = object :Runnable{
        override fun run() {

            Log.d("zzp123","do mAnimateRunnbale")

            mMode = 1

            mHandler.removeMessages(MSG_TEXT)

            mValueAnimate = ValueAnimator.ofFloat(0f,-mMaxScrollX)
            mValueAnimate?.addUpdateListener {
                mScrollX = it.animatedValue as Float
                invalidate()
            }
            mValueAnimate?.addListener(object : AnimatorListenerAdapter(){

                override fun onAnimationEnd(animation:Animator){
                    mMode = 0
                    invalidate()

                    mHandler.removeMessages(MSG_TEXT)
                    mHandler.sendEmptyMessageDelayed(MSG_TEXT,10000L)
                }
            })

            mValueAnimate?.duration = 7000
            mValueAnimate?.start()
        }
    }

    init{
        mPaint.textSize = 13f.dp
        mPaint.color = mTextColor
        mPaint.textAlign = Paint.Align.LEFT

        var fontMetricsInt = mPaint.getFontMetricsInt()
        mTextHeight = fontMetricsInt.bottom - fontMetricsInt.top
        mTextWidth = mPaint.measureText("00:00:00").toInt()
        mDrawTxtY = -fontMetricsInt.top.toFloat()

        mScrollX = 0f
        mMaxScrollX = mPaint.measureText(mReportTxt) - mTextWidth
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawText(getText(),getTranslateX(),mDrawTxtY,mPaint)
    }

    private fun getText():String{
        if(mMode == 0) {
            if (leftTime > 0L) {
                return formatDateDifference2(leftTime/1000)
            } else {
                return "00:00:00"
            }
        }else{
            return mReportTxt
        }
    }

    private fun getTranslateX():Float{
        if(mMode == 0) {
            return 0f
        }else{
            return mScrollX
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(MeasureSpec.makeMeasureSpec(mTextWidth,MeasureSpec.EXACTLY),MeasureSpec.makeMeasureSpec(mTextHeight,MeasureSpec.EXACTLY))
    }

    val SECOND = 1
    val MINUTE = SECOND * 60
    val HOURS = MINUTE * 60
    val DAY = HOURS * 24

    /**
     * 格式化时间差
     */
    fun formatDateDifference2(timestamp: Long): String {
        val day = timestamp / DAY
        val hour = (timestamp - DAY * day) / HOURS
        val minute = (timestamp - DAY * day - HOURS * hour) / MINUTE
        val second =
            (timestamp - DAY * day - HOURS * hour - MINUTE * minute) / SECOND


        val hourTxt = if (hour < 10) "0$hour" else hour
        val secondTxt = if (second < 10) "0$second" else second
        val minuteTxt = if (minute < 10) "0$minute" else minute

        return String.format("%s:%s:%s", hourTxt, minuteTxt, secondTxt)
    }
}