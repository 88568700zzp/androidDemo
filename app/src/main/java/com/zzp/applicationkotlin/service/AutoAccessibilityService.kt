package com.zzp.applicationkotlin.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.annotation.SuppressLint
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

/**
 *
 * Created by samzhang on 2021/7/20.
 */
class AutoAccessibilityService: AccessibilityService(){

    private val TAG = "AccessibilityService"

    @Override
    override fun onServiceConnected() {

        Log.d(TAG,"onServiceConnected")

        /*var info = AccessibilityServiceInfo()

        info.eventTypes = AccessibilityEvent.TYPE_VIEW_CLICKED or AccessibilityEvent.TYPE_WINDOWS_CHANGED

        info.packageNames = arrayOf("com.zzp.applicationkotlin")

        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK

        info.notificationTimeout = 100

        this.setServiceInfo(info);*/

    }

    override fun onInterrupt() {
        Log.e(TAG,"onInterrupt")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        //Log.i(TAG,"onAccessibilityEvent event:${event}")
        event?.let {
            when(it.eventType){
                AccessibilityEvent.TYPE_VIEW_CLICKED->{
                    Log.e(TAG,"info action:TYPE_VIEW_CLICKED text:${it.text} source:${it.source}")

                }
                AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED->{
                    Log.e(TAG,"info action:TYPE_WINDOW_STATE_CHANGED className:${it.className}")
                    it.source?.let {
                        logAllView(event.source)
                    }
                }
                else->{

                }
            }
        }
    }

    private fun logAllView(nodeInfo: AccessibilityNodeInfo){
        var childCount =  nodeInfo.childCount
        Log.d(TAG,"logAllView className:${nodeInfo.className} text:${nodeInfo.text} id:${nodeInfo.viewIdResourceName}")
        if(nodeInfo.text == "LAUNCH MAIN" && nodeInfo.isClickable){
            Log.d(TAG,"result:" + nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK))
        }
        for(index in 0 until childCount){
            logAllView(nodeInfo.getChild(index))
        }
    }
}