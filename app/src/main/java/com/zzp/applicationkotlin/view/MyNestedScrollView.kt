package com.zzp.applicationkotlin.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.widget.NestedScrollView

/**
 *
 * Created by samzhang on 2021/11/23.
 */
class MyNestedScrollView(context: Context, attrs: AttributeSet?) : NestedScrollView(context, attrs) {

    private val TAG = "MyNestedScrollView"

    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        Log.d(TAG,"onStartNestedScroll1")
        return super.onStartNestedScroll(child, target, axes, type)
    }

    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
        Log.d(TAG,"onStartNestedScroll2")
        return super.onStartNestedScroll(child, target, nestedScrollAxes)
    }

    override fun onStopNestedScroll(target: View, type: Int) {
        Log.d(TAG,"onStopNestedScroll1")
        super.onStopNestedScroll(target, type)
    }

    override fun onStopNestedScroll(target: View) {
        Log.d(TAG,"onStopNestedScroll2")
        super.onStopNestedScroll(target)
    }

    override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int, consumed: IntArray) {
        Log.d(TAG,"onNestedScroll1 dyUnconsumed：${dyUnconsumed} dyConsumed:${dyConsumed}")
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed)
    }

    override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {
        Log.d(TAG,"onNestedScroll2")
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)
    }

    override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int) {
        Log.d(TAG,"onNestedScroll3")
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed)
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        Log.d(TAG,"onNestedPreScroll1 x:" + consumed[0] + " y:" + consumed[1])
        super.onNestedPreScroll(target, dx, dy, consumed, type)
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        Log.d(TAG,"onNestedPreScroll2 x:" + consumed[0] + " y:" + consumed[1])
        super.onNestedPreScroll(target, dx, dy, consumed)
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {
        Log.d(TAG,"onNestedScrollAccepted1")
        super.onNestedScrollAccepted(child, target, axes, type)
    }

    override fun onNestedScrollAccepted(child: View, target: View, nestedScrollAxes: Int) {
        Log.d(TAG,"onNestedScrollAccepted2")
        super.onNestedScrollAccepted(child, target, nestedScrollAxes)
    }

    override fun onNestedFling(target: View, velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
        Log.d(TAG,"onNestedFling")
        return super.onNestedFling(target, velocityX, velocityY, consumed)
    }

    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        Log.d(TAG,"onNestedPreFling")
        return super.onNestedPreFling(target, velocityX, velocityY)
    }
}