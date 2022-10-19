package com.zzp.applicationkotlin.service.accessibility

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
import android.view.accessibility.AccessibilityNodeInfo

/**
 *
 * Created by samzhang on 2021/7/20.
 */
class AutoAccessibilityService: AccessibilityService(){

    private val TAG = "AutoAccessibilityService"

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
                AccessibilityEvent.TYPE_VIEW_FOCUSED->{
                    Log.e(TAG,"info action:TYPE_VIEW_FOCUSED text:${it.text} source:${it.source}")

                }
                AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_START->{
                    Log.e(TAG,"info action:TYPE_TOUCH_EXPLORATION_GESTURE_START text:${it.text} source:${it.source}")

                }
                AccessibilityEvent.TYPE_TOUCH_INTERACTION_START->{
                    Log.e(TAG,"info action:TYPE_TOUCH_INTERACTION_START text:${it.text} source:${it.source}")

                }
                AccessibilityEvent.TYPE_VIEW_HOVER_ENTER->{
                    Log.e(TAG,"info action:TYPE_VIEW_HOVER_ENTER text:${it.text} source:${it.source}")

                }
                AccessibilityEvent.TYPE_VIEW_CONTEXT_CLICKED->{
                    Log.e(TAG,"info action:TYPE_VIEW_CONTEXT_CLICKED text:${it.text} source:${it.source}")
                }
                AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED->{
                    Log.e(TAG,"info action:TYPE_WINDOW_STATE_CHANGED className:${it.className}")
                    it.source?.let {
                        logAllView(event.source)
                    }
                }
                AccessibilityEvent.TYPE_VIEW_SCROLLED->{
                    Log.e(TAG,"info action:TYPE_VIEW_SCROLLED text:${it.text} source:${it.source}")

                }
                else->{
                    if(it.eventType == TYPE_WINDOW_CONTENT_CHANGED){
                        if(it.className != "android.widget.ImageView") {
                            Log.e(TAG, "else TYPE_WINDOW_CONTENT_CHANGED $it")
                            it.source?.let {source->
                                logAllView(source)
                            }
                        }else{}
                    }else {
                        Log.e(TAG, "else $it")
                    }
                    /*if(it.eventType != TYPE_WINDOW_CONTENT_CHANGED){

                    }else{
                        if(it.text.isNotEmpty()){
                            var found = false
                            for(text in it.text){
                                if(text.contains("广告")){
                                    found = true
                                }
                            }
                            if(found) {
                                Log.e(TAG, "else 广告 $it")
                            }else{

                            }
                        }else{

                        }
                    }*/
                }
            }
        }
    }

    private fun logAllView(nodeInfo: AccessibilityNodeInfo){
        var childCount =  nodeInfo.childCount
        Log.d(TAG,"logAllView className:${nodeInfo.className} text:${nodeInfo.text} click:${nodeInfo.isClickable} isVisibleToUser:${nodeInfo.isVisibleToUser} childCount:$childCount")
        if(nodeInfo.text == "开宝箱得金币"){
            var checkNode = nodeInfo.parent
            while(checkNode != null && !checkNode.isClickable){
                Log.d(TAG,"checkNode:${checkNode}")
                checkNode = checkNode.parent
            }
            checkNode?.let {
                Log.d(TAG,"checkNode result:${checkNode}")
            }
        }
        for(index in 0 until childCount){
            nodeInfo.getChild(index)?.let {node->
                logAllView(node)
            }

        }
    }
}