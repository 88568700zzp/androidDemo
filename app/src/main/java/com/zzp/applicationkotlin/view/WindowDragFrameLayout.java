package com.zzp.applicationkotlin.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

/**
 * Created by samzhang on 2021/10/22.
 */
public class WindowDragFrameLayout extends FrameLayout {

    public WindowManager mWindowManager;
    public WindowManager.LayoutParams mLayoutParams;

    private int mStatusHeight;

    private float mTouchDownX;
    private float mTouchDownY;

    private float mTouchSlot;

    private int mScreenWidth;
    private int mScreenHeight;

    private boolean mIsDragging = false;
    private boolean mIsAnimating = false;
    private boolean mRight = true;

    private ValueAnimator mValueAnimator;

    Callback mCallback;

    public WindowDragFrameLayout(Context context) {
        super(context);
    }

    public WindowDragFrameLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WindowDragFrameLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(int screenWidth,int screenHeight){
        mStatusHeight = getStatusBarHeight();

        mTouchSlot = ViewConfiguration.get(getContext()).getScaledTouchSlop();

        mScreenWidth = screenWidth;
        mScreenHeight = screenHeight;
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mTouchDownX = event.getRawX();
                mTouchDownY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if(!mIsDragging && (Math.abs( event.getRawX() - mTouchDownX) >= mTouchSlot || Math.abs( event.getRawY() - mTouchDownY) >= mTouchSlot)){
                    mIsDragging = true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:{
                handleTouchUp();
                break;
            }
        }
        if(mIsDragging || mIsAnimating){
            return true;
        }
        return super.onInterceptTouchEvent(event);
    }

    private void handleTouchUp(){
        if(mIsDragging) {
            mIsDragging = false;
            mIsAnimating = false;

            int width = 500;
            if (mLayoutParams.x + width / 2 > mScreenWidth / 2) {
                animateToX(mScreenWidth - width);
            } else {
                animateToX(0);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                if(!mIsDragging && (Math.abs( event.getRawX() - mTouchDownX) >= mTouchSlot || Math.abs( event.getRawY() - mTouchDownY) >= mTouchSlot)){
                    mIsDragging = true;
                }
                if(mIsDragging) {
                    mLayoutParams.x = (int) event.getRawX() - getWidth() / 2;
                    mLayoutParams.y = (int) event.getRawY() - getHeight() / 2 - mStatusHeight;
                    mWindowManager.updateViewLayout(this, mLayoutParams);

                    boolean isRight = mLayoutParams.x > mScreenWidth/2;
                    if(mRight != isRight){
                        mRight = isRight;
                        if(mCallback != null){
                            mCallback.onSideChange(mRight);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:{
                handleTouchUp();
            }
        }
        return super.onTouchEvent(event);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            result = getResources().getDimensionPixelSize(resId);
        }
        return result;
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancelAnimate();
    }


    private void cancelAnimate(){
        if(mValueAnimator != null && mValueAnimator.isRunning()){
            mValueAnimator.cancel();
        }
    }

    private void animateToX(int targetX){
        cancelAnimate();

        mValueAnimator = ValueAnimator.ofInt(mLayoutParams.x,targetX);
        mIsAnimating = true;
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mLayoutParams.x = (int) animation.getAnimatedValue();
                mWindowManager.updateViewLayout(WindowDragFrameLayout.this, mLayoutParams);
            }
        });
        mValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mIsAnimating = false;

                mLayoutParams.x =  targetX;
                mWindowManager.updateViewLayout(WindowDragFrameLayout.this, mLayoutParams);
            }
        });
        mValueAnimator.setDuration(300);
        mValueAnimator.start();

    }


    public interface Callback{
        void onSideChange(boolean mRight);
    }
}
