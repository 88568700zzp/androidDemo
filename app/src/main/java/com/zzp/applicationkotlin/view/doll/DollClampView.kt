package com.zzp.applicationkotlin.view.doll

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.view.animation.AccelerateDecelerateInterpolator
import com.zzp.applicationkotlin.R
import com.zzp.applicationkotlin.event.HitAnimateEvent
import com.zzp.applicationkotlin.view.doll.DollMachineView.Companion.DOLL_CLAMP_ANIMATE_DURATION
import org.greenrobot.eventbus.EventBus


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

    private var mStartHitX = 0F
    private var mEndHitX = 0F

    private var mBompX = -1F
    private var mBompStatus = 0//0隐藏，1显示
    var isBombing = false//爆炸动画中

    private var mShapeLevel = 1f//形状大小

    private var mGo = true//正向或者反向,true是从左往右

    private var mStartTime = 0L
    private var mCurrentX = 0f
    private var mOffsetY = 0f

    private var mRectF = RectF()
    private var mInterpolator = AccelerateDecelerateInterpolator()
    private var mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    var totalDrawWidth = 0
    var totalDrawHeight = 0

    var hitClampProgress = 0F

    var rectWidth = 3F.dp//细线宽度

    lateinit var mContext:Context

    lateinit var mShipBitmap: Bitmap
    lateinit var mClampBitmap: Bitmap

    var mHitDollView:DollView ?= null

    fun init(context:Context,startX:Float,endX:Float,startY:Float){
        mStartX = startX
        mEndX= endX
        mStartY = startY
        mStartTime = System.currentTimeMillis()

        mContext = context

        mShipBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ic_clamp_ship)
        mClampBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ic_clamp_00)
        totalDrawWidth = mShipBitmap.width
        totalDrawHeight = mShipBitmap.height + mShipBitmap.height
    }

    fun onDraw(canvas: Canvas?, paint: Paint){

        mCurrentX = caculateCurrentX()

        var clampBitmap = mClampBitmap

        if(mOffsetY > 0 && isClampAnimating) {
            getBitmapByHitClampProgress()?.let {
                clampBitmap = it
            }
        }

        var shipBeginY = mStartY

        var clampWidth = clampBitmap.width
        var clampHeight = clampBitmap.height
        var clampBeginY = shipBeginY + mShipBitmap.height + mOffsetY


        var drawClamp = true
        if(isBombing && mBompStatus == 0){
            drawClamp = false
        }

        if(drawClamp) {
            mRectF.set(
                mCurrentX - clampWidth / 2,
                clampBeginY,
                mCurrentX + clampWidth / 2,
                clampBeginY + clampHeight
            )
            canvas?.drawBitmap(clampBitmap, null, mRectF, mPaint)
        }

        mHitDollView?.let {
            if (hitClampProgress >= 0.5f) {
                it.setHiddenAndSaveInfo()
                it.drawHitDoll(canvas, paint, clampBeginY + clampHeight)
            }

        }

        if(mOffsetY > 0) {
            var rectBeginY = shipBeginY + mShipBitmap.height
            mPaint.color = Color.WHITE
            mRectF.set(mCurrentX - rectWidth / 2, rectBeginY, mCurrentX + rectWidth / 2, rectBeginY + mOffsetY)
            canvas?.drawRect(mRectF, mPaint)
        }

        if(drawClamp) {
            //画飞船
            mRectF.set(
                mCurrentX - mShipBitmap.width / 2,
                shipBeginY,
                mCurrentX + mShipBitmap.width / 2,
                shipBeginY + mShipBitmap.height
            )
            canvas?.drawBitmap(mShipBitmap, null, mRectF, mPaint)
        }
    }

    private  fun updateStartTimeOnAnimateEnd(pauseX:Float){
        if(mGo){
            var progress  =(pauseX - mStartX) * 1f/(mEndX - mStartX)
            mStartTime = System.currentTimeMillis() -  (progress* DollMachineView.INIT_DOLL_DOLL_CLAMP_DURATION).toLong()
        }else{
            var progress  =(mEndX - pauseX) * 1f/(mEndX - mStartX)
            mStartTime = System.currentTimeMillis() - (progress * DollMachineView.INIT_DOLL_DOLL_CLAMP_DURATION).toLong()
        }
    }

    private fun caculateCurrentX():Float{
        var currentX = 0F

        if(isBombing){
            currentX = mBompX
        }else if(mHitDollView != null){
            getHitXClampProgress().takeIf { it > 0 }?.let{
                currentX = it
            }
        }else {
            if(mEndHitX > 0){
                updateStartTimeOnAnimateEnd(mEndHitX)
                mEndHitX = 0F
            }
            if(!isBombing && mBompX > 0){
                updateStartTimeOnAnimateEnd(mBompX)
                mBompX = 0F
            }
            var currentTime = System.currentTimeMillis()
            var leftTime = currentTime - mStartTime

            if (leftTime >= DollMachineView.INIT_DOLL_DOLL_CLAMP_DURATION) {
                currentX = if (mGo) {
                    mEndX
                } else {
                    mStartX
                }
                mGo = !mGo
                mStartTime = System.currentTimeMillis()
            } else {
                var progress = leftTime * 1f / DollMachineView.INIT_DOLL_DOLL_CLAMP_DURATION
                //progress = mInterpolator.getInterpolation(progress)
                currentX = if (mGo) {
                    mStartX + (mEndX - mStartX) * progress
                } else {
                    mEndX - (mEndX - mStartX) * progress
                }
            }
        }
        return currentX
    }

    fun getCurrentX():Float{
        return mCurrentX
    }

    fun getLeftX():Float{
        return mCurrentX - totalDrawWidth * mShapeLevel
    }

    fun getRightX():Float{
        return mCurrentX + totalDrawWidth * mShapeLevel
    }

    fun bomp(time:Int){
        mBompX = mCurrentX
        var animator = ValueAnimator.ofInt(0,time)
        animator.addUpdateListener {
           var value = it.animatedValue as Int
            mBompStatus = (value/300)%2
        }
        animator.addListener(object : android.animation.AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                isBombing = false
                mBompStatus = 0
            }

            override fun onAnimationStart(animation: Animator?, isReverse: Boolean) {
                isBombing = true
                mBompStatus = 1
            }
        })
        animator.duration = time.toLong()
        animator.start()
    }

    /**
     *@description 执行击中动画
     *@param hit 是否命中
     *@param hitY 命中的y中标
     *@return
    **/
    fun doHitAnimate(hitDollView:DollView){
        mHitDollView = hitDollView

        var position = hitDollView.getPositionWhenHit()
        var hitY = position[1]

        mEndHitX = position[0]
        mStartHitX = mCurrentX

        var finalOffsetY = hitY - (mStartY + totalDrawHeight)

        var animator = ValueAnimator.ofFloat(0F,finalOffsetY,0F)
        animator.addUpdateListener { it ->
            mOffsetY = it.animatedValue as Float
            hitClampProgress = it.animatedFraction

        }
        animator.addListener(object : android.animation.AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                isClampAnimating = false
                mOffsetY = 0F
                hitClampProgress = 1F
                mHitDollView?.let {
                    it.gone = true//动画结束后，隐藏掉
                    mHitDollView = null
                }
                EventBus.getDefault().post(HitAnimateEvent())
            }

            override fun onAnimationStart(animation: Animator?, isReverse: Boolean) {
                isClampAnimating = true
                mOffsetY = 0F
                hitClampProgress = 0F
            }
        })
        animator.duration = DOLL_CLAMP_ANIMATE_DURATION
        animator.start()
    }

    private fun getHitXClampProgress():Float{
        hitClampProgress.let {
            var progress = if(it > 0.3f) 1f else (it/0.3f)
            return mStartHitX + (mEndHitX - mStartHitX) * progress
        }
        return 0F
    }

    private fun getBitmapByHitClampProgress():Bitmap{
        hitClampProgress.let {
            return when (it) {
                in 0F..0.05F -> {
                    BitmapFactory.decodeResource(mContext.resources, R.drawable.ic_clamp_01)
                }
                in 0.05F..0.1F -> {
                    BitmapFactory.decodeResource(mContext.resources, R.drawable.ic_clamp_02)
                }
                in 0.1F..0.15F -> {
                    BitmapFactory.decodeResource(mContext.resources, R.drawable.ic_clamp_03)
                }
                in 0.15F..0.2F -> {
                    BitmapFactory.decodeResource(mContext.resources, R.drawable.ic_clamp_04)
                }
                in 0.2F..0.25F -> {
                    BitmapFactory.decodeResource(mContext.resources, R.drawable.ic_clamp_05)
                }
                in 0.25F..0.3F -> {
                    BitmapFactory.decodeResource(mContext.resources, R.drawable.ic_clamp_06)
                }
                in 0.3F..0.35F -> {
                    BitmapFactory.decodeResource(mContext.resources, R.drawable.ic_clamp_07)
                }
                in 0.35F..0.4F -> {
                    BitmapFactory.decodeResource(mContext.resources, R.drawable.ic_clamp_07)
                }
                in 0.4F..0.45F -> {
                    BitmapFactory.decodeResource(mContext.resources, R.drawable.ic_clamp_08)
                }
                in 0.45F..0.5F -> {
                    BitmapFactory.decodeResource(mContext.resources, R.drawable.ic_clamp_09)
                }
                in 0.5F..0.55F -> {
                    BitmapFactory.decodeResource(mContext.resources, R.drawable.ic_clamp_10)
                }
                in 0.55F..0.6F -> {
                    BitmapFactory.decodeResource(mContext.resources, R.drawable.ic_clamp_11)
                }
                in 0.6F..0.65F -> {
                    BitmapFactory.decodeResource(mContext.resources, R.drawable.ic_clamp_12)
                }
                in 0.65F..0.7F -> {
                    BitmapFactory.decodeResource(mContext.resources, R.drawable.ic_clamp_13)
                }
                in 0.7F..0.75F -> {
                    BitmapFactory.decodeResource(mContext.resources, R.drawable.ic_clamp_14)
                }
                in 0.75F..0.8F -> {
                    BitmapFactory.decodeResource(mContext.resources, R.drawable.ic_clamp_15)
                }
                in 0.85F..0.9F -> {
                    BitmapFactory.decodeResource(mContext.resources, R.drawable.ic_clamp_16)
                }
                in 0.9F..0.95F -> {
                    BitmapFactory.decodeResource(mContext.resources, R.drawable.ic_clamp_17)
                }
                else -> {
                    BitmapFactory.decodeResource(mContext.resources, R.drawable.ic_clamp_18)
                }
            }
        }
        return mClampBitmap
    }

    /**
     *@description 击中失败
     *@param hitY 命中的y中标
     *@return
     **/
    fun hitFailAnimate(hitY:Float){
        var finalOffsetY = hitY - (mStartY + totalDrawHeight)

        var animator = ValueAnimator.ofFloat(0F,finalOffsetY,0F)
        animator.addUpdateListener {
            mOffsetY = it.animatedValue as Float
        }
        animator.addListener(object : android.animation.AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                isClampAnimating = false
                mOffsetY = 0F
            }
            override fun onAnimationStart(animation: Animator?, isReverse: Boolean) {
                isClampAnimating = true
                mOffsetY = 0F
            }
        })
        animator.duration = DOLL_CLAMP_ANIMATE_DURATION
        animator.start()
    }


    fun resume(delayTime:Long){
        mStartTime += delayTime
    }

    /**
     *@description 夹子变大
     *@param
     *@return
    **/
    fun bigClamp(){
        mShapeLevel *= 2
    }

    /**
     *@description 夹子变小
     *@param
     *@return
     **/
    fun smallClamp(){
        mShapeLevel /= 2f
    }
}