package com.zzp.applicationkotlin.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.animation.AccelerateInterpolator
import kotlin.math.abs

class ImageClipLayer : View {

    private val MIN_WIDTH = ScreenUtils.dpToPx(100f)//裁剪区域最小宽度
    private val LINE_LENGTH = ScreenUtils.dpToPx(50f)

    private var mActivePointerId = -1

    private var mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var mLineColor = Color.WHITE
    private var mShadowColor = Color.parseColor("#b2000000")

    private var mSideWidth = ScreenUtils.dpToPx(4f)
    private var mBorderSideWidth = ScreenUtils.dpToPx(2f)
    private var mLineWidth = ScreenUtils.dpToPx(0.5f)

    private var mBorderClipLeft = 0f
    private var mBorderClipRight = 0f

    private var mBorderClipBottom = 0f
    private var mBorderClipTop = 0f

    private var mClipLeft = 0f
    private var mClipRight = 0f

    private var mClipBottom = 0f
    private var mClipTop = 0f

    private var mHitCurrentX = 0f
    private var mHitCurrentY = 0f

    private var mTouchSlot = 0f

    private var mHLineNum = 2
    private var mVLineNum = 2

    private var mStartDrag = false
    private var mClipChange = false
    private var mClipAreaWidth = ScreenUtils.dpToPx(50f)
    private var mClipOutAreaWidth = ScreenUtils.dpToPx(20f)

    private var mEffectSide = 0//1左上角，2右上角，3左下角，4右下角 5左线，6右线，7上线，8下线
    private var mDrawShadow = true
    private var isCanTouch = false
    private var mShowView = false

    private var mRect = RectF()
    private var mPath = Path()

    private var mInit = false

    private var mIsAnimating = false

    private var mListener:OnClipListener? = null

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
        mTouchSlot = ViewConfiguration.get(context).scaledTouchSlop.toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (mShowView) {
            canvas?.let {
                drawShadow(it)
                drawLines(it)
            }
        }
    }


    private fun drawLines(canvas: Canvas) {
        mPaint.color = mLineColor
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = mLineWidth
        mPaint.strokeCap = Paint.Cap.SQUARE

        for (i in 1..mHLineNum) {
            var startX = (mClipRight - mClipLeft) / (mHLineNum + 1) * i + mClipLeft
            canvas.drawLine(startX, mClipTop + mBorderSideWidth, startX, mClipBottom - mBorderSideWidth, mPaint)
        }

        for (i in 1..mVLineNum) {
            var startY = (mClipBottom - mClipTop) / (mVLineNum + 1) * i + mClipTop
            canvas.drawLine(mClipLeft + mBorderSideWidth, startY, mClipRight - mBorderSideWidth, startY, mPaint)
        }

        mPaint.strokeWidth = mSideWidth
        mPaint.strokeCap = Paint.Cap.ROUND

        var left = mClipLeft + mSideWidth / 2
        var top = mClipTop + mSideWidth / 2
        var right = mClipRight - mSideWidth / 2
        var bottom = mClipBottom - mSideWidth / 2

        mPath.reset()
        mPath.moveTo(left, top + LINE_LENGTH)
        mPath.lineTo(left, top)
        mPath.lineTo(left + LINE_LENGTH, top)
        canvas.drawPath(mPath, mPaint)

        mPath.reset()
        mPath.moveTo(right, top + LINE_LENGTH)
        mPath.lineTo(right, top)
        mPath.lineTo(right - LINE_LENGTH, top)
        canvas.drawPath(mPath, mPaint)

        mPath.reset()
        mPath.moveTo(left, bottom - LINE_LENGTH)
        mPath.lineTo(left, bottom)
        mPath.lineTo(left + LINE_LENGTH, bottom)
        canvas.drawPath(mPath, mPaint)

        mPath.reset()
        mPath.moveTo(right, bottom - LINE_LENGTH)
        mPath.lineTo(right, bottom)
        mPath.lineTo(right - LINE_LENGTH, bottom)
        canvas.drawPath(mPath, mPaint)

        mPaint.strokeWidth = mBorderSideWidth
        mPaint.strokeCap = Paint.Cap.SQUARE

        mPath.reset()
        mPath.moveTo(left + LINE_LENGTH, top)
        mPath.lineTo(right - LINE_LENGTH, top)
        canvas.drawPath(mPath, mPaint)

        mPath.reset()
        mPath.moveTo(right, top + LINE_LENGTH)
        mPath.lineTo(right, bottom - LINE_LENGTH)
        canvas.drawPath(mPath, mPaint)

        mPath.reset()
        mPath.moveTo(left + LINE_LENGTH, bottom)
        mPath.lineTo(right - LINE_LENGTH, bottom)
        canvas.drawPath(mPath, mPaint)

        mPath.reset()
        mPath.moveTo(left, top + LINE_LENGTH)
        mPath.lineTo(left, bottom - LINE_LENGTH)
        canvas.drawPath(mPath, mPaint)
    }

    private fun drawShadow(canvas: Canvas) {
        /*if(!mDrawShadow){
            return
        }*/
        mPaint.color = mShadowColor
        mPaint.style = Paint.Style.FILL

        canvas.save()

        mRect.set(mClipLeft, mClipTop, mClipRight, mClipBottom)
        mRect.inset(mBorderSideWidth/2,mBorderSideWidth/2)

        canvas.clipRect(mRect,Region.Op.DIFFERENCE)

        canvas.drawRect(0f,0f,width.toFloat(),height.toFloat(),mPaint)

        canvas.restore()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (!isCanTouch || mIsAnimating) {
            return false
        }
        event?.let {
            when (it.action and it.actionMasked) {
                MotionEvent.ACTION_DOWN -> {

                    mListener?.onClipDown()
                    if (!mStartDrag && isInClipArea(event.x, event.y)) {
                        mActivePointerId = event.getPointerId(0)

                        mStartDrag = true

                        mHitCurrentX = event.x
                        mHitCurrentY = event.y

                        mDrawShadow = false
                    }else{
                        mDrawShadow = true
                    }
                    invalidate()
                }
                MotionEvent.ACTION_MOVE -> {
                    if (mStartDrag) {
                        var pointerIndex: Int = event.findPointerIndex(mActivePointerId)

                        if (pointerIndex == -1) {
                            pointerIndex = 0
                            mActivePointerId = event.getPointerId(pointerIndex)
                        }
                        var diffX = event.getX(pointerIndex) - mHitCurrentX
                        var diffY = event.getY(pointerIndex) - mHitCurrentY


                        if (abs(diffX) >= mTouchSlot || abs(diffY) >= mTouchSlot) {
                            adjustClipInfo(diffX, diffY)
                            mHitCurrentX = event.getX(pointerIndex)
                            mHitCurrentY = event.getY(pointerIndex)
                            invalidate()
                        }
                    }
                }
                MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                    mDrawShadow = true
                    mListener?.onClipUp()
                    if(mClipChange){
                        mListener?.onClipChange(getClipBoundInfo(),true)
                    }
                    reset()
                    invalidate()
                }

            }
        }
        return mStartDrag
    }

    private fun adjustClipInfo(diffX: Float, diffY: Float) {
        mClipChange = true
        if (mEffectSide == 1) {
            mClipLeft += diffX
            mClipTop += diffY
            if (mClipRight - mClipLeft < MIN_WIDTH) {
                mClipLeft = mClipRight - MIN_WIDTH
            }
            if (mClipBottom - mClipTop < MIN_WIDTH) {
                mClipTop = mClipBottom - MIN_WIDTH
            }
        } else if (mEffectSide == 2) {
            mClipRight += diffX
            mClipTop += diffY
            if (mClipRight - mClipLeft < MIN_WIDTH) {
                mClipRight = mClipLeft + MIN_WIDTH
            }
            if (mClipBottom - mClipTop < MIN_WIDTH) {
                mClipTop = mClipBottom - MIN_WIDTH
            }
        } else if (mEffectSide == 3) {
            mClipLeft += diffX
            mClipBottom += diffY
            if (mClipRight - mClipLeft < MIN_WIDTH) {
                mClipLeft = mClipRight - MIN_WIDTH
            }
            if (mClipBottom - mClipTop < MIN_WIDTH) {
                mClipBottom = mClipTop + MIN_WIDTH
            }
        } else if (mEffectSide == 4) {
            mClipRight += diffX
            mClipBottom += diffY
            if (mClipRight - mClipLeft < MIN_WIDTH) {
                mClipRight = mClipLeft + MIN_WIDTH
            }
            if (mClipBottom - mClipTop < MIN_WIDTH) {
                mClipBottom = mClipTop + MIN_WIDTH
            }
        }else if (mEffectSide == 5) {
            mClipLeft += diffX
            if (mClipRight - mClipLeft < MIN_WIDTH) {
                mClipLeft = mClipRight - MIN_WIDTH
            }
        }else if (mEffectSide == 6) {
            mClipRight += diffX
            if (mClipRight - mClipLeft < MIN_WIDTH) {
                mClipRight = mClipLeft + MIN_WIDTH
            }
        }else if (mEffectSide == 7) {
            mClipTop += diffY
            if (mClipBottom - mClipTop < MIN_WIDTH) {
                mClipTop = mClipBottom - MIN_WIDTH
            }
        }else if (mEffectSide == 8) {
            mClipBottom += diffY
            if (mClipBottom - mClipTop < MIN_WIDTH) {
                mClipBottom = mClipTop + MIN_WIDTH
            }
        }

        checkClipBorder()
    }

    //检测裁剪滑动边界
    private fun checkClipBorder(){
        if (mClipLeft < mBorderClipLeft) {
            mClipLeft = mBorderClipLeft
        }
        if (mClipTop < mBorderClipTop) {
            mClipTop = mBorderClipTop
        }
        if (mClipRight > mBorderClipRight) {
            mClipRight = mBorderClipRight
        }
        if (mClipBottom > mBorderClipBottom) {
            mClipBottom = mBorderClipBottom
        }
    }

    private fun reset() {
        mClipChange = false
        mStartDrag = false
        mEffectSide = 0
        mActivePointerId = -1
    }

    //按下是否在裁剪区域内
    private fun isInClipArea(x: Float, y: Float): Boolean {
        if (x > mClipLeft - mClipOutAreaWidth && x < mClipLeft + mClipAreaWidth && y > mClipTop - mClipOutAreaWidth && y < mClipTop + mClipAreaWidth) {
            mEffectSide = 1
            return true
        }

        if (x > mClipRight - mClipAreaWidth && x < mClipRight + mClipOutAreaWidth && y > mClipTop - mClipOutAreaWidth && y < mClipTop + mClipAreaWidth) {
            mEffectSide = 2
            return true
        }

        if (x > mClipLeft - mClipOutAreaWidth && x < mClipLeft + mClipAreaWidth && y > mClipBottom - mClipAreaWidth && y < mClipBottom + mClipOutAreaWidth) {
            mEffectSide = 3
            return true
        }

        if (x > mClipRight - mClipAreaWidth && x < mClipRight + mClipOutAreaWidth && y > mClipBottom - mClipAreaWidth && y < mClipBottom + mClipOutAreaWidth) {
            mEffectSide = 4
            return true
        }

        if (x > mClipLeft - mClipOutAreaWidth && x < mClipLeft + mClipOutAreaWidth && y > mClipTop + mClipAreaWidth && y < mClipBottom - mClipAreaWidth) {
            mEffectSide = 5
            return true
        }

        if (x > mClipRight - mClipOutAreaWidth && x < mClipRight + mClipOutAreaWidth && y > mClipTop + mClipAreaWidth && y < mClipBottom - mClipAreaWidth) {
            mEffectSide = 6
            return true
        }

        if (x > mClipLeft + mClipAreaWidth && x < mClipRight - mClipAreaWidth && y > mClipTop - mClipOutAreaWidth && y < mClipTop + mClipOutAreaWidth) {
            mEffectSide = 7
            return true
        }

        if (x > mClipLeft + mClipAreaWidth && x < mClipRight - mClipAreaWidth && y > mClipBottom - mClipOutAreaWidth && y < mClipBottom +                                                   mClipOutAreaWidth) {
            mEffectSide = 8
            return true
        }

        return false
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if(!mInit){
            mInit = true

            mBorderClipLeft = 0f
            mBorderClipRight = width.toFloat()
            mBorderClipBottom = height.toFloat()
            mBorderClipTop = 0f

            mClipLeft = mBorderClipLeft
            mClipRight = mBorderClipRight
            mClipBottom = mBorderClipBottom
            mClipTop = mBorderClipTop
        }

    }

    /**
     * 是否响应触摸事件
     *
     * @param isCanTouch true 可以编辑
     */
    fun setIsCanTouch(isCanTouch: Boolean) {
        this.isCanTouch = isCanTouch
        this.mShowView = isCanTouch

        invalidate()
    }


    fun initClipInfo(clipInfo:RectF){
        initClipInfo(clipInfo.left,clipInfo.top,clipInfo.right,clipInfo.bottom)
    }

    fun initClipInfo(clipLeft:Float, clipTop:Float, clipRight:Float, clipBottom:Float){

        mClipTop = clipTop
        mClipLeft = clipLeft

        mClipBottom = clipBottom
        mClipRight = clipRight

        invalidate()

        mListener?.onClipChange(getClipBoundInfo(),false)
    }

    /**
     * 设置滑动区域边界
     */
    fun setClipBorder(clipLeft:Float, clipTop:Float, clipRight:Float, clipBottom:Float){
        mBorderClipLeft = clipLeft
        mBorderClipTop = clipTop
        mBorderClipRight = clipRight
        mBorderClipBottom = clipBottom

        if(mBorderClipLeft < 0){
            mBorderClipLeft = 0f
        }

        if(mBorderClipRight > height){
            mBorderClipRight = height.toFloat()
        }

        if(mBorderClipTop < 0){
            mBorderClipTop = 0f
        }

        if(mBorderClipRight > width){
            mBorderClipRight = width.toFloat()
        }
    }

    /**
     * 根据裁剪中点，裁剪宽高 更新裁剪区域
     */
    fun resetClipByCenter(centerX:Float, centerY:Float, width:Float, height:Float){

        mClipTop = centerY - height/2
        mClipLeft = centerX - width/2

        mClipBottom = centerY + height/2
        mClipRight = centerX + width/2

        invalidate()

        mListener?.onClipChange(getClipBoundInfo(),false)
    }

    /**
     * 获取裁剪区域信息
     */
    fun getClipBoundInfo():RectF{
        return RectF(mClipLeft,mClipTop,mClipRight,mClipBottom)
    }

    fun setClipListener(listener: OnClipListener){
        mListener = listener
    }

    fun animateToClipRect(rect:RectF,time:Long = 300){
        animateToClipRect(rect.left,rect.top,rect.right,rect.bottom,time)
    }

    fun animateToClipRect(targetClipLeft:Float, targetClipTop:Float, targetClipRight:Float, targetClipBottom:Float,time:Long = 300){
        var startClipLeft = mClipLeft
        var startClipTop = mClipTop

        var startClipRight = mClipRight
        var startClipBottom = mClipBottom

        mIsAnimating = true

        var valueAnimator = ValueAnimator.ofInt(0,100)

        valueAnimator.addUpdateListener {
            var progress = (it.animatedValue as Int)/100f
            initClipInfo((targetClipLeft - startClipLeft) * progress + startClipLeft,
                (targetClipTop - startClipTop) * progress + startClipTop,
                (targetClipRight - startClipRight) * progress + startClipRight,
                (targetClipBottom - startClipBottom) * progress + startClipBottom)
        }

        valueAnimator.addListener(object: AnimatorListenerAdapter(){
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                mIsAnimating = false
                initClipInfo(targetClipLeft,targetClipTop,targetClipRight,targetClipBottom)
            }
        })

        valueAnimator.setInterpolator(AccelerateInterpolator())
        valueAnimator.duration = time
        valueAnimator.start()

    }

    interface OnClipListener{
        //裁剪区域发生回调
        fun onClipChange(clipRect:RectF,actionUp:Boolean)

        fun onClipDown()

        fun onClipUp()
    }

}