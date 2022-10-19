package com.zzp.applicationkotlin.service.accessibility;

import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public abstract class BaseFace {

    protected boolean mEnterPage = false;

    void onEnter(){
        mEnterPage = true;
    }

    void onExist(){
        mEnterPage = false;
    }

    public boolean isEnterPage(){
        return mEnterPage;
    }

    void checkPage(AccessibilityEvent event){
        if(isFace(event.getClassName())){
            if(!isEnterPage()){
               onEnter();
            }
        }else{
            if(isEnterPage()){
                onExist();
            }
        }
    }

    protected void logAllView(AccessibilityNodeInfo nodeInfo){
        if(nodeInfo == null){
            return;
        }
        Log.d(getTag(),"logAllView className:" + nodeInfo.getClassName() + " text:" + nodeInfo.getText());
        for(int i = 0;i < nodeInfo.getChildCount();i++){
            logAllView(nodeInfo.getChild(i));
        }
    }

    abstract String getTag();

    abstract boolean isFace(CharSequence className);

    abstract void handleEvent(AccessibilityEvent event);
}
