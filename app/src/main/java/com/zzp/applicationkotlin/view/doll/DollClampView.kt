package com.zzp.applicationkotlin.view.doll

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.animation.AccelerateDecelerateInterpolator
import com.zzp.applicationkotlin.view.doll.DollMachineView.Companion.DOLL_CLAMP_ANIMATE_DURATION

/**
 * 夹子view
 * Created by samzhang on 2021/3/26.
 */
class DollClampView {
    var isClampAnimating = false//夹子动画是否在进行

    private var mStartX = 0f
    private var mEndX = 0f
    private var mStartY = 0f
    private var mEndY = 0f

    private var mGo = true//正向或者反向

    private var mStartTime = 0L
    private var mCurrentX = 0f
    private var mOffsetY = 0f

    private var mRectF = RectF()
    private var mInterpolator = AccelerateDecelerateInterpolator()

    var drawWidth = 30
    var drawHeight = 200


    fun init(startX:Float,endX:Float,startY:Float){
        mStartX = startX
        mEndX= endX
        mStartY = startY
        mStartTime = System.currentTimeMillis()
    }

    fun calculate(canvas: Canvas?, paint: Paint){
        var currentTime = System.currentTimeMillis()

        var leftTime = currentTime - mStartTime
        if(leftTime >= DollMachineView.DOLL_CLAMP_DURATION){
            mCurrentX = if(mGo){
                mEndX
            }else{
                mStartX
            }
            mGo = !mGo
            mStartTime = System.currentTimeMillis()
        }else{
            var progress = leftTime * 1f /DollMachineView.DOLL_CLAMP_DURATION
            progress = mInterpolator.getInterpolation(progress)

            mCurrentX = if(mGo){
                mStartX + (mEndX- mStartX) * progress
            }else{
                mEndX - (mEndX- mStartX) * progress
            }
        }

        mRectF.set(mCurrentX - drawWidth/2,mStartY + mOffsetY,mCurrentX + drawWidth/2,mStartY + mOffsetY + drawHeight)

        canvas?.drawRect(mRectF,paint)
    }

    fun getCurrentX():Float{
        return mCurrentX
    }

    /**
     *@description 执行击中动画
     *@param hit 是否命中
     *@param hitY 命中的y中标
     *@return
    **/
    fun doHitAnimate(hit:Boolean,hitY:Float){
        isClampAnimating = true

        if(!hit){
           var animator = ValueAnimator.ofFloat(0F,hitY,0F)
            animator.addUpdateListener {
                mOffsetY = it.animatedValue as Float
            }
            animator.addListener(object : android.animation.AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    isClampAnimating = false
                }
            })
            animator.duration = 600
            animator.start()
        }else{
            var animator = ValueAnimator.ofFloat(0F,hitY,0F)
            animator.addUpdateListener {
                mOffsetY = it.animatedValue as Float
            }
            animator.addListener(object : android.animation.AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    isClampAnimating = false
                }
            })
            animator.duration = DOLL_CLAMP_ANIMATE_DURATION
            animator.start()
        }
    }


    fun resume(delayTime:Long){
        mStartTime += delayTime
    }
}