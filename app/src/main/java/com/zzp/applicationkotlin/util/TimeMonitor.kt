package com.zzp.applicationkotlin.util

import android.util.Log

/**
 * 时间监控器
 * Created by samzhang on 2021/7/27.
 */
class TimeMonitor(var TAG:String = "TimeMonitor") {

    private var mTime = 0L

    fun start(msg:String? = null){
        mTime = System.currentTimeMillis()

        msg?.let {
            Log.i(TAG,it)
        }
    }

    fun record(msg:String? = null){
        var currentTime = System.currentTimeMillis()

        if(msg != null){
            Log.i(TAG,"${msg} cost:${currentTime - mTime}")
        }else{
            Log.i(TAG,"cost:${currentTime - mTime}")
        }

        mTime = currentTime
    }

    companion object{
        private var mTimeMonitor:TimeMonitor ?= null

        @JvmStatic
        fun getGlobalInstance():TimeMonitor{
            if(mTimeMonitor == null) {
                synchronized(TimeMonitor.javaClass) {
                    if (mTimeMonitor == null) {
                        mTimeMonitor = TimeMonitor("globalTimeMonitor")
                    }
                }
            }
            return mTimeMonitor!!
        }
    }
}