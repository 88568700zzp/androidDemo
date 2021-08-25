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
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import com.zzp.applicationkotlin.R
import com.zzp.applicationkotlin.view.doll.dp
import kotlin.math.abs

/**
 * 拖动view
 * Created by samzhang on 2021/8/20.
 */
class BallTouchView(context:Context,attr:AttributeSet?) :View(context,attr){

    private var mInit = false
    private var mIsExpand = false//是否展开
    private var mIsDragging = false
    private var mIsAnimating = false
    private var mHandleTouchDown = false//按下的时候是否在布局界面范围内
    private var mLeft = true//左边还是右边
    private var mTouchExpandView = false//按下的时候是否是展开的

    private var mRectWidth = 40f.dp
    private var mRectHeight = 107f.dp
    private var mSideWidth = 91f.dp
    private var mSideHeight = 91f.dp

    private var mTouchSlot = ViewConfiguration.get(context).scaledTouchSlop

    private var mCurrentX = 0f
    private var mCurrentY = 0f

    private var mTouchDownX = 0f
    private var mTouchDownY = 0f

    private var mRect = RectF()
    private var mGiftBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_main_float_gift)
    private var mSideRightGiftBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_main_float_gift_side_right)
    private var mSideLeftGiftBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_main_float_gift_side_left)

    private var mHandler = Handler(Looper.getMainLooper())

    var mCallBack:ICallBack ?= null

    private var mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mValueAnimator:ValueAnimator ?= null

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        var handle = false

        event?.let {
            when(event.actionMasked){
                MotionEvent.ACTION_DOWN->{
                    if(isAccept(event)){
                        mTouchExpandView = mIsExpand
                        mIsExpand = true
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

                        handle = true
                    }
                }
                MotionEvent.ACTION_CANCEL,
                MotionEvent.ACTION_UP->{
                    if(mHandleTouchDown) {
                        if (mIsExpand) {
                            var targetX = if (mCurrentX + mSideWidth / 2 > width / 2) {
                                mLeft = false
                                width - mSideWidth / 2
                            } else {
                                mLeft = true
                                mSideWidth / 2
                            }
                            animateToX(targetX)

                            if (!mIsDragging && mTouchExpandView) {
                                mCallBack?.onExpandClick()
                            }
                        }
                        handle = true
                        mIsDragging = false
                        mHandleTouchDown = false
                    }
                }
            }
        }

        if(handle){
            invalidate()
            return true
        }else {
            return super.onTouchEvent(event)
        }
    }

    private fun adjustX(x:Float):Float{
        return if(x + mSideWidth/2 > width){
            width - mSideWidth/2
        }else {
            x
        }
    }

    private fun adjustY(y:Float):Float{
        return when {
            y - mSideHeight/2 < 0 -> {
                mSideHeight/2
            }
            y + mSideHeight/2 > height -> {
                height - mSideHeight/2
            }
            else -> {
                y
            }
        }
    }

    private fun isAccept(event: MotionEvent):Boolean{
        var unexpand = !mIsExpand && !mIsAnimating && event.x > (mCurrentX - mRectWidth/2) && event.x < (mCurrentX + mRectWidth/2) && event.y > (mCurrentY - mRectHeight/2) && event.y < (mCurrentY + mRectHeight/2)
        var expand = mIsExpand && !mIsAnimating && event.x > (mCurrentX - mSideWidth/2) && event.x < (mCurrentX + mSideWidth/2) && event.y > (mCurrentY - mSideHeight/2) && event.y < (mCurrentY + mSideHeight/2)
        return unexpand or expand
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

    private fun animateToX(targetX:Float){
        cancelAnimate()

        ValueAnimator.ofFloat(mCurrentX,targetX).let { animator->
            mValueAnimator = animator
            mIsAnimating = true
            animator.addUpdateListener {
                mCurrentX = it.animatedValue as Float
                invalidate()
            }
            animator.addListener(object :AnimatorListenerAdapter(){
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    mIsAnimating = false
                    invalidate()
                    mHandler.removeCallbacks(mRunnbale)
                    mHandler.postDelayed(mRunnbale,3000L)
                }
            })
            animator.duration = 300
            animator.start()
        }
    }

    private var mRunnbale = Runnable {
        mIsExpand = false
        mCurrentX = if(mCurrentX < width/2){
            mRectWidth/2
        }else{
            (width - mRectWidth/2)
        }
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if(!mInit){
            mInit = true
            mLeft = true
            mCurrentY = (height/2).toFloat()
            mCurrentX = mRectWidth/2
        }

        if(!mIsExpand){
            mRect.set(mCurrentX - mRectWidth/2,
                (mCurrentY - mRectHeight/2), (mCurrentX + mRectWidth/2),
                (mCurrentY + mRectHeight/2))
            canvas?.drawBitmap(if(mLeft) mSideLeftGiftBitmap else mSideRightGiftBitmap,null,mRect,mPaint)
        }else{
            mRect.set(mCurrentX - mSideWidth/2,
                (mCurrentY - mSideHeight/2), (mCurrentX + mSideWidth/2),
                (mCurrentY + mSideHeight/2))
            canvas?.drawBitmap(mGiftBitmap,null,mRect,mPaint)
        }
    }

    interface ICallBack{
        fun onExpandClick()
    }
}