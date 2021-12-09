package com.zzp.applicationkotlin.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import androidx.core.view.NestedScrollingChildHelper
import androidx.core.view.ViewCompat

/**
 *
 * Created by samzhang on 2021/11/23.
 */
class ZzpNestView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val TAG = "ZzpNestView"

    private var mChildHelper: NestedScrollingChildHelper = NestedScrollingChildHelper(this)

    private var mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mRect = Rect()

    private var mTouchSlot = ViewConfiguration().scaledTouchSlop
    private var mLastTouchY = 0f
    private var mIsDragging = false

    private var mConsumeArray = IntArray(2)

    init{
        isNestedScrollingEnabled = true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        mRect.set(0, 0, width, height / 2)

        mPaint.color = Color.RED
        canvas?.drawRect(mRect, mPaint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            when (it.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_DOWN -> {
                    mLastTouchY = event.y
                    (parent as ViewGroup).requestDisallowInterceptTouchEvent(true)
                    startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL)
                }
                MotionEvent.ACTION_MOVE -> {
                    var diffY = Math.abs(mLastTouchY - event.y)
                    if (diffY >= mTouchSlot) {
                        var distanceY = (mLastTouchY - event.y).toInt()
                        if(dispatchNestedPreScroll(0, distanceY, mConsumeArray, null)){
                            distanceY -= mConsumeArray[1]
                        }

                        if (scrollY + distanceY > 0) {
                            var newDistanceY = 0 - scrollY
                            scrollBy(0, newDistanceY)
                            mChildHelper.dispatchNestedScroll(0, 0, 0, distanceY - newDistanceY, null, ViewCompat.TYPE_TOUCH, mConsumeArray)
                            Log.d(TAG, "mConsumeArray x：${mConsumeArray[0]} y：${mConsumeArray[1]}")
                            mLastTouchY = event.y + mConsumeArray[1]
                        } else if (scrollY + distanceY < -height / 2) {
                            var newDistanceY = -height / 2 - scrollY
                            scrollBy(0, newDistanceY)
                            mChildHelper.dispatchNestedScroll(0, 0, 0, distanceY - newDistanceY, null, ViewCompat.TYPE_TOUCH, mConsumeArray)
                            Log.d(TAG, "mConsumeArray x：${mConsumeArray[0]} y：${mConsumeArray[1]}")
                            mLastTouchY = event.y + mConsumeArray[1]
                        } else {
                            scrollBy(0, distanceY)
                            Log.d(TAG, "mConsumeArray x：${0} y：${0}")

                            mLastTouchY = event.y
                        }

                    } else {

                    }
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    mIsDragging = false
                    stopNestedScroll()
                }
                else -> {

                }
            }
        }

        return true
    }

    override fun scrollBy(x: Int, y: Int) {
        super.scrollBy(x, y)
        Log.d(TAG, "scrollBy y:${y} scrollY:${scrollY}")
    }

    override fun setNestedScrollingEnabled(enabled: Boolean) {
        mChildHelper.isNestedScrollingEnabled = enabled
    }

    override fun isNestedScrollingEnabled(): Boolean {
        return mChildHelper.isNestedScrollingEnabled
    }

    override fun startNestedScroll(axes: Int): Boolean {
        return mChildHelper.startNestedScroll(axes)
    }

    override fun stopNestedScroll() {
        mChildHelper.stopNestedScroll()
    }


    // NestedScrollingChild3

    // NestedScrollingChild3
    fun dispatchNestedScroll(dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int,
                             dyUnconsumed: Int, offsetInWindow: IntArray?, type: Int, consumed: IntArray) {
        mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed,
                offsetInWindow, type, consumed)
    }


    override fun dispatchNestedPreScroll(dx: Int, dy: Int, consumed: IntArray?, offsetInWindow: IntArray?): Boolean {
        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow)
    }

    override fun hasNestedScrollingParent(): Boolean {
        return mChildHelper.hasNestedScrollingParent()
    }
}