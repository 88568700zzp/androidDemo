package com.zzp.applicationkotlin.service.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.NonNull;

import java.util.List;

public class ToutiaoFace extends BaseFace {

    private final int MSG_BACK = 0x123;
    private final int MSG_CLOSE_VOICE = 0x124;
    private final int MSG_CHECK_CONTENT = 0x125;
    private final int MSG_CHECK_CONTENT_FLAG = 0x126;

    private final String TAG = "ToutiaoFace";

    private AccessibilityService mService;

    private boolean mCheckCloseFlag = false;
    private boolean mCheckMoreFlag = false;

    private Rect mRect = new Rect();

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what == MSG_BACK){
                mService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
            }else if(msg.what == MSG_CLOSE_VOICE){
                closeVoice();
            }else if(msg.what == MSG_CHECK_CONTENT){
                checkContent();
            }else if(msg.what == MSG_CHECK_CONTENT_FLAG){
                mCheckCloseFlag = true;
            }
        }
    };

    private void closeVoice(){
        List<AccessibilityNodeInfo> voiceMode = mService.getRootInActiveWindow().findAccessibilityNodeInfosByText("开启声音");
        if(voiceMode != null){
            for(AccessibilityNodeInfo node:voiceMode){
                Log.d(TAG,"voice node:" + node);
                if(node != null && "com.lynx.tasm.behavior.ui.view.UIView".equals(node.getClassName())){
                    //node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    node.getBoundsInScreen(mRect);
                    Path path = new Path();
                    path.moveTo(mRect.centerX(),mRect.centerY());
                    GestureDescription.Builder builder = new GestureDescription.Builder();
                    builder.addStroke(new GestureDescription.StrokeDescription(path,0, ViewConfiguration.getTapTimeout()));
                    mService.dispatchGesture(builder.build(),null,null);
                    break;
                }
            }
        }
    }

    private void checkContent(){
        if(mCheckCloseFlag){
            List<AccessibilityNodeInfo> closeMode = mService.getRootInActiveWindow().findAccessibilityNodeInfosByText("关闭");
            if(closeMode != null){
                for(AccessibilityNodeInfo node:closeMode){
                    Log.d(TAG,"close node:" + node);
                    if(node != null && node.isClickable() && "com.lynx.tasm.behavior.ui.view.UIView".equals(node.getClassName())){
                        node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        mCheckCloseFlag = false;
                        mCheckMoreFlag = true;
                        break;
                    }
                }
            }
        }
        if(mCheckMoreFlag){
            checkMoreNode(mService.getRootInActiveWindow());
        }

    }

    private void checkMoreNode(AccessibilityNodeInfo nodeInfo){
        if(nodeInfo == null){
            return;
        }
        if(nodeInfo.getChildCount() > 0){
            for(int i = 0;i < nodeInfo.getChildCount();i++){
                checkMoreNode(nodeInfo.getChild(i));
            }
        }else{
            if(nodeInfo.isClickable() && "com.lynx.tasm.behavior.ui.view.UIView".equals(nodeInfo.getClassName()) && nodeInfo.getText() != null && nodeInfo.getText().toString().contains("再看一个获得")){
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                reset();
            }
        }
    }

    public ToutiaoFace(AccessibilityService service){
        mService = service;
    }

    @Override
    String getTag() {
        return TAG;
    }

    @Override
    void onEnter() {
        super.onEnter();
        reset();
    }

    private void reset(){
        //刚进来关闭时间
        mHandler.removeMessages(MSG_CLOSE_VOICE);
        mHandler.sendEmptyMessageDelayed(MSG_CLOSE_VOICE,1000);

        mCheckCloseFlag = false;
        mCheckMoreFlag = false;
        mHandler.removeMessages(MSG_CHECK_CONTENT_FLAG);
        mHandler.sendEmptyMessageDelayed(MSG_CHECK_CONTENT_FLAG,20000);
    }

    @Override
    void onExist() {
        super.onExist();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public boolean isFace(CharSequence className) {
        return "com.ss.android.excitingvideo.ExcitingVideoActivity".equals(className);
    }

    @Override
    void handleEvent(AccessibilityEvent event) {
        switch (event.getEventType()){
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:{
                mHandler.removeMessages(MSG_CHECK_CONTENT);
                mHandler.sendEmptyMessage(MSG_CHECK_CONTENT);
            }
        }
    }
}
