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

    private var mStartX = 0f
    private var mStartY = 0f

    private var mEndX = 0f
    private var mEndY = 0f

    var race_index = -1

    var mCurrentX = 0f
    var mCurrentY = 0f

    private var mStartScale = 1f
    private var mEndScale = 1.4f

    private var mStartTime = 0L
    private var mRectF = RectF()

    var mBitmap:Bitmap ?= null

    constructor(startx: Float, startY: Float, endx: Float, endY: Float,raceIndex:Int) {
        this.mStartX = startx
        this.mStartY = startY
        this.mEndX = endx
        this.mEndY = endY
        this.race_index = raceIndex

        mStartTime = System.currentTimeMillis()
    }


    fun calculate(canvas: Canvas?, paint: Paint):Boolean{
        var currentTime = System.currentTimeMillis()

        var leftTime = currentTime - mStartTime
        if(leftTime >= DollMachineView.DOLL_DURATION){
            return false
        }

        mBitmap?.let {

            var progress = leftTime * 1f /DollMachineView.DOLL_DURATION

            mCurrentX = mStartX + (mEndX- mStartX) * progress
            mCurrentY = mStartY + (mEndY- mStartY) * progress

            var width = it.width.toFloat()
            var height = it.height.toFloat()

            var scale = mStartScale + (mEndScale- mStartScale) * progress

            width = (width * scale)
            height = (height * scale)

            mRectF.set(mCurrentX - width/2,mCurrentY - height/2,mCurrentX + width/2,mCurrentY + height/2)
            canvas?.drawBitmap(it,null,mRectF,paint)
        }


        return true
    }

    /**
     * 返回是否命中
     */
    fun hitArea(startHitX:Float,endHitY:Float):Boolean{
        mBitmap?.let{
            var startAreaY = mCurrentY - it.height/2f
            var endAreaY = mCurrentY + it.height/2f

            if(startAreaY in startHitX..endHitY){//头部是否在区域内
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

}