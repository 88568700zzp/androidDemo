package com.zzp.applicationkotlin.service.accessibility

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityEvent.*
import android.view.accessibility.AccessibilityNodeInfo
import android.view.accessibility.AccessibilityNodeInfo.ACTION_CLICK

class DealAccessibilityService : AccessibilityService {
    private val TAG = "RealAccessibilityService"

    private var mFaceList = ArrayList<BaseFace>()

    constructor(){
        mFaceList.add(ToutiaoFace(this))
    }

    @Override
    override fun onServiceConnected() {
        Log.d(TAG,"onServiceConnected")

    }

    override fun onInterrupt() {
        Log.e(TAG,"onInterrupt")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.let {
            if(it.eventType == TYPE_WINDOW_STATE_CHANGED){
                Log.e(TAG,"TYPE_WINDOW_STATE_CHANGED ${event.className}")
                mFaceList.forEach {face->
                    face.checkPage(event)
                }
            }else{
                mFaceList.forEach {face->
                    if(face.isEnterPage) {
                        face.handleEvent(event)
                    }
                }

                when(it.eventType){
                    AccessibilityEvent.TYPE_VIEW_CLICKED->{
                        Log.e(TAG,"info action:TYPE_VIEW_CLICKED text:${it.text} source:${it.source}")

                    }
                    AccessibilityEvent.TYPE_VIEW_FOCUSED->{
                        Log.e(TAG,"info action:TYPE_VIEW_FOCUSED text:${it.text} source:${it.source}")

                    }
                    AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED->{
                        Log.e(TAG,"info action:TYPE_VIEW_TEXT_CHANGED text:${it.text} source:${it.source}")

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
                    }
                    AccessibilityEvent.TYPE_VIEW_SCROLLED->{
                        Log.e(TAG,"info action:TYPE_VIEW_SCROLLED text:${it.text} source:${it.source}")

                    }
                    AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED->{
                        Log.i(TAG,"info action:TYPE_NOTIFICATION_STATE_CHANGED text:${it.text} source:${it.source}")
                        rootInActiveWindow?.let {
                            logAllView(rootInActiveWindow)
                        }
                    }
                    AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED->{
                        /*if(event.contentChangeTypes == AccessibilityEvent.CONTENT_CHANGE_TYPE_TEXT){
                            Log.e(TAG,"info action:TYPE_WINDOW_CONTENT_CHANGED text:${it.text} source:${it.source}")
                        }else{

                        }*/
                        if(it.contentChangeTypes == CONTENT_CHANGE_TYPE_TEXT || it.contentChangeTypes == CONTENT_CHANGE_TYPE_SUBTREE) {
                            Log.e(TAG, "info action:TYPE_WINDOW_CONTENT_CHANGED CONTENT_CHANGE_TYPE_TEXT $it")

                            //Log.e(TAG,"TYPE_NOTIFICATION_STATE_CHANGED");
                            rootInActiveWindow?.let {
                                logAllView(rootInActiveWindow)
                            }
                        }else{}
                    }
                    else->{


                    }                    }

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG,"onDestroy")
        mFaceList.forEach {face->
            face.onExist()
        }
        mFaceList.clear()
    }

    private fun logAllView(nodeInfo: AccessibilityNodeInfo){
        var childCount =  nodeInfo.childCount
        Log.d(TAG,"logAllView className:${nodeInfo.className} text:${nodeInfo.text} click:${nodeInfo.isClickable} viewIdResourceName:${nodeInfo.viewIdResourceName}")

        for(index in 0 until childCount){
            nodeInfo.getChild(index)?.let {node->
                logAllView(node)
            }

        }
    }
}