package com.zzp.applicationkotlin.view.doll

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF

/**
 * 娃娃view
 * Created by samzhang on 2021/3/25.
 */

class DollView {

    private val HIT_LATE_TIME = 200

    private var mStartX = 0f
    private var mStartY = 0f

    private var mEndX = 0f
    private var mEndY = 0f

    var race_index = -1

    var mCurrentX = 0f
    var mCurrentY = 0f
    var mCurrentScale = 0f

    var gone = false

    private var hidden = false
    private var mHiddenX = 0f
    private var mHiddenY = 0f
    private var mHiddenScale = 0f

    private var mStartScale = 0.2f
    private var mEndScale = 1.6f

    private var mStartTime = 0L
    private var mRectF = RectF()

    var mBitmap:Bitmap ?= null
    var mBgBitmap:Bitmap ?= null
    var mVoiceBitmap:Bitmap ?= null


    constructor(startx: Float, startY: Float, endx: Float, endY: Float,raceIndex:Int) {
        this.mStartX = startx
        this.mStartY = startY
        this.mEndX = endx
        this.mEndY = endY
        this.race_index = raceIndex

        mStartTime = System.currentTimeMillis()
    }


    fun onDraw(canvas: Canvas?, paint: Paint):Boolean{
        var currentTime = System.currentTimeMillis()

        var leftTime = currentTime - mStartTime
        if(leftTime >= DollMachineView.mCurrentDollDuration || leftTime < 0F || gone){
            return false
        }

        var progress = leftTime * 1f /DollMachineView.mCurrentDollDuration
        mCurrentX = mStartX + (mEndX- mStartX) * progress
        mCurrentY = mStartY + (mEndY- mStartY) * progress
        mCurrentScale = mStartScale + (mEndScale- mStartScale) * progress

        paint.alpha = getAlphaByProgress(progress)

        mBgBitmap?.let {
            var width = it.width.toFloat()
            var height = it.height.toFloat()

            width = (width * mCurrentScale)
            height = (height * mCurrentScale)

            var currentY = mCurrentY + 20f.dp * mCurrentScale
            var currentX = mCurrentX
            if(race_index == 0){
                currentX -= 2f.dp * mCurrentScale
            }else if(race_index == 1){
                currentX -= 2f.dp * mCurrentScale
            }else if(race_index == 2){
                currentX += 2f.dp * mCurrentScale
            }else if(race_index == 3){
                currentX += 2f.dp * mCurrentScale
            }

            mRectF.set(currentX - width/2,currentY - height/2,currentX + width/2,currentY + height/2)
            canvas?.drawBitmap(it, null, mRectF, paint)
        }
        mBitmap?.let {
            var width = it.width.toFloat()
            var height = it.height.toFloat()

            width = (width * mCurrentScale)
            height = (height * mCurrentScale)

            mRectF.set(mCurrentX - width/2,mCurrentY - height/2,mCurrentX + width/2,mCurrentY + height/2)
            if(!hidden) {
                canvas?.drawBitmap(it, null, mRectF, paint)
            }
        }
        paint.alpha = 255

        return true
    }

    private fun getAlphaByProgress(progress:Float):Int{
        if(progress <= 0.2f){
            return 50 + (205 * progress/0.2f).toInt()
        }else{
            return 255
        }
    }


    /**
     * 返回是否命中
     */
    fun hitArea(startHitX:Float,endHitY:Float):Boolean{
        mBitmap?.let{
            var rangeHeight = it.height * mCurrentScale/3f
            var startAreaY = mCurrentY - rangeHeight
            var endAreaY = mCurrentY + rangeHeight

            if(startAreaY in startHitX..(endHitY - rangeHeight * 2)){//头部是否在区域内
                return true
            }
            if(endAreaY in startHitX..endHitY){//尾部是否在区域内
                return true
            }

        }
        return false
    }


    fun resume(delayTime:Long){
        mStartTime += delayTime
    }

    fun changeDollSpeed(newTime:Long,preTime:Long):Boolean{
        var currentTime = System.currentTimeMillis()

        var afterTime = currentTime - mStartTime

        return if(afterTime >= preTime || afterTime < 0F){
            false
        }else{
            var progress = (afterTime) * 1f /preTime //已经走过的进度
            var newAfterTime = progress * newTime
            mStartTime -= ((newAfterTime - afterTime)).toLong()
            true
        }
    }

    fun getPositionWhenHit():Array<Float>{
        var currentTime = System.currentTimeMillis() + HIT_LATE_TIME

        var leftTime = currentTime - mStartTime
        if(leftTime >= DollMachineView.mCurrentDollDuration || leftTime < 0F){
            return arrayOf(mCurrentX,mCurrentY)
        }

        var progress = leftTime * 1f /DollMachineView.mCurrentDollDuration
        var y = mStartY + (mEndY- mStartY) * progress
        var x = mStartX + (mEndX- mStartX) * progress
        return arrayOf(x,y)
    }

    fun setHiddenAndSaveInfo(){
        if(hidden){
            return
        }
        hidden = true

        mHiddenX = mCurrentX
        mHiddenY = mCurrentY
        mHiddenScale = mCurrentScale
    }

    fun drawHitDoll(canvas: Canvas?, paint: Paint ,endY:Float){
        if(!hidden){
            return
        }

        var startVoiceY = endY

        mBitmap?.let{
            var width = it.width.toFloat()
            var height = it.height.toFloat()

            width = (width * mHiddenScale)
            height = (height * mHiddenScale)

            startVoiceY = endY - height

            mRectF.set(mHiddenX - width/2,endY - height,mHiddenX + width/2,endY)

            canvas?.drawBitmap(it, null, mRectF, paint)
        }

        mVoiceBitmap?.let {
            var width = it.width.toFloat()
            var height = it.height.toFloat()

            width = (width * mHiddenScale)
            height = (height * mHiddenScale)

            mRectF.set(mHiddenX,startVoiceY,mHiddenX + width,startVoiceY + height)

            canvas?.drawBitmap(it, null, mRectF, paint)
        }
    }

}