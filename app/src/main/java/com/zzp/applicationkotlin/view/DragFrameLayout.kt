package com.zzp.applicationkotlin.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.FrameLayout
import kotlin.math.abs

/**
 *
 * Created by samzhang on 2021/10/21.
 */
class DragFrameLayout(context: Context,attributeSet: AttributeSet?) :FrameLayout(context,attributeSet){

    private var mIsDragging = false
    private var mIsAnimating = false
    private var mHandleTouchDown = false//按下的时候是否在布局界面范围内
    private var mLeft = true//左边还是右边

    private var mRectWidth = 0f
    private var mRectHeight = 0f

    private var mTouchSlot = ViewConfiguration.get(context).scaledTouchSlop

    private var mCurrentX = 0f
    private var mCurrentY = 0f

    private var mTouchDownX = 0f
    private var mTouchDownY = 0f


    private var mHandler = Handler(Looper.getMainLooper())

    private var mValueAnimator: ValueAnimator?= null

    private var mCallback:Callback ?= null

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {

        var handle = false

        event?.let {
            when(event.actionMasked){
                MotionEvent.ACTION_DOWN->{
                    if(isAccept(event)){
                        mHandleTouchDown = true

                        mTouchDownY = it.y
                        mTouchDownX = it.x
                        mIsDragging = false

                        handle = true

                        mHandler.removeCallbacks(mRunnbale)
                    }
                }
            }
        }

        if(handle || mIsDragging || mIsAnimating){
            return true
        }else {
            return super.onInterceptTouchEvent(event)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        var handle = false

        event?.let {
            when(event.actionMasked){
                MotionEvent.ACTION_DOWN->{
                    if(isAccept(event)){
                        mHandleTouchDown = true

                        mTouchDownY = it.y
                        mTouchDownX = it.x
                        mIsDragging = false

                        handle = true

                        mHandler.removeCallbacks(mRunnbale)
                    }
                }
                MotionEvent.ACTION_MOVE->{
                    if(mHandleTouchDown){
                        if(!mIsDragging){
                            if(abs(it.y - mTouchDownY) >= mTouchSlot || abs(it.x - mTouchDownX) >= mTouchSlot){
                                mIsDragging = true
                            }
                        }
                        if(mIsDragging) {
                            mCurrentY = adjustY(it.y)
                            mCurrentX = adjustX(it.x)
                        }
                        var isLeft = mCurrentX > width/2
                        if(mLeft != isLeft){
                            mLeft = isLeft
                            mCallback?.onSideChange(mLeft)
                        }

                        handle = true
                    }
                }
                MotionEvent.ACTION_CANCEL,
                MotionEvent.ACTION_UP->{
                    var targetX = if (mCurrentX  > width / 2) {
                        mLeft = false
                        width - mRectWidth / 2
                    } else {
                        mLeft = true
                        mRectWidth / 2
                    }
                    animateToX(targetX)
                }
            }
        }

        if(handle){
            Log.d("zzp123","onTouch mCurrentX:${mCurrentX} mCurrentY:${mCurrentY}")
            resetChild()
            return true
        }else {
            return super.onTouchEvent(event)
        }
    }

    private fun adjustX(x:Float):Float{
        return if(x + mRectWidth/2 > width){
            width - mRectWidth/2
        }else if(x - mRectWidth/2 < 0){
            mRectWidth/2
        }else {
            x
        }
    }

    private fun adjustY(y:Float):Float{
        return when {
            y - mRectHeight/2 < 0 -> {
                mRectHeight/2
            }
            y + mRectHeight/2 > height -> {
                height - mRectHeight/2
            }
            else -> {
                y
            }
        }
    }

    private fun isAccept(event: MotionEvent):Boolean{
        var unexpand = !mIsAnimating && event.x > (mCurrentX - mRectWidth/2) && event.x < (mCurrentX + mRectWidth/2) && event.y > (mCurrentY - mRectHeight/2) && event.y < (mCurrentY + mRectHeight/2)
        return unexpand
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        cancelAnimate()
    }

    private fun cancelAnimate(){
        mValueAnimator?.let {
            if(it.isRunning){
                it.cancel()
            }
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        if(childCount > 0){
            var child = getChildAt(0)

            mCurrentY = height/2.toFloat()
            mCurrentX = (width - child.measuredWidth/2).toFloat()

            mRectWidth = child.measuredWidth.toFloat()
            mRectHeight = child.measuredHeight.toFloat()
        }
        Log.d("zzp123","onLayout mCurrentX:${mCurrentX} mCurrentY:${mCurrentY}")
        resetChild()
    }

    private fun resetChild(){
        if(childCount > 0) {
            var child = getChildAt(0)
            var startX = (mCurrentX - child.measuredWidth/2).toInt()
            var startY = (mCurrentY - child.measuredHeight/2).toInt()
            Log.d("zzp123","resetChild startX:" + startX + " startY:" + startY)
            child.layout(startX,startY,startX + child.measuredWidth,startY + child.measuredHeight)
        }
    }

    private fun animateToX(targetX:Float){
        cancelAnimate()

        ValueAnimator.ofFloat(mCurrentX,targetX).let { animator->
            mValueAnimator = animator
            mIsAnimating = true
            animator.addUpdateListener {
                mCurrentX = it.animatedValue as Float
                resetChild()
            }
            animator.addListener(object : AnimatorListenerAdapter(){
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    mIsAnimating = false
                    resetChild()
                    mHandler.removeCallbacks(mRunnbale)
                    mHandler.postDelayed(mRunnbale,3000L)
                }
            })
            animator.duration = 300
            animator.start()
        }
    }

    private var mRunnbale = Runnable {
        mCurrentX = if(mCurrentX < width/2){
            mRectWidth/2
        }else{
            (width - mRectWidth/2)
        }
        invalidate()
    }

    interface Callback{
        fun onSideChange(left:Boolean)
    }

}