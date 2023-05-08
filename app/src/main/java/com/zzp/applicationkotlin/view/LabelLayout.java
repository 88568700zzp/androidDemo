package com.zzp.applicationkotlin.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class LabelLayout extends ViewGroup {

    private int mHSpace;
    private int mVSpace;

    private int mLineCount = 0;

    private List<Integer> mLineHeightList = new ArrayList<>();


    public LabelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LabelLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mHSpace = 10;
        mVSpace = 10;
        mLineCount = 1;
    }

    public void setHWSpace(int hSpace,int vSpace){
        mHSpace = hSpace;
        mVSpace = vSpace;
        requestLayout();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int currentLine = -1;
        int userWidth = 0;
        int userHeight = 0;

        int preLineHeight = 0;

        for(int i = 0;i < getChildCount();i++){
            View child = getChildAt(i);
            if(child.getVisibility() != View.GONE) {
                LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
                int lineHeight = mLineHeightList.get(layoutParams.lineIndex);

                if(currentLine == layoutParams.lineIndex){

                }else{//换行
                    userWidth = 0;
                    if(layoutParams.lineIndex > 0) {
                        userHeight = userHeight + preLineHeight + mVSpace;
                    }else{
                        userHeight = 0;
                    }
                }

                int startX = userWidth;
                int startY = (userHeight) + (lineHeight - child.getMeasuredHeight())/2;
                int endX = startX + child.getMeasuredWidth();
                int endY = startY + child.getMeasuredHeight();

               child.layout(startX,startY, endX,endY);

                userWidth = endX + mHSpace;

                currentLine = layoutParams.lineIndex;
                preLineHeight = lineHeight;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int needWidth = 0;
        int needHeight = 0;
        int userWidth = 0;
        int lineHeight = 0;

        mLineHeightList.clear();

        int lineIndex = 0;

        for(int i = 0;i < getChildCount();i++){
            View child = getChildAt(i);
            measureChild(child,widthMeasureSpec,heightMeasureSpec);
            if(child.getVisibility() != View.GONE) {
                LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();

                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();
                userWidth = userWidth + childWidth;
                if (userWidth <= widthSize) {
                    userWidth = userWidth + mHSpace;
                    if (lineHeight < childHeight) {
                        lineHeight = childHeight;
                    }
                } else {//换行
                    lineIndex++;
                    needHeight = needHeight + lineHeight + mVSpace;
                    mLineHeightList.add(lineHeight);
                    lineHeight = childHeight;
                    userWidth = 0;
                }
                layoutParams.lineIndex = lineIndex;
            }
        }



        if(getChildCount() == 0){
            mLineCount = 0;
            needHeight = getMinimumHeight();
            needWidth = getMinimumWidth();
        }else{
            mLineHeightList.add(lineIndex,lineHeight);
            mLineCount = mLineHeightList.size();

            if(mLineCount == 1){//单行情况
                needHeight = lineHeight;
                needWidth = userWidth - mHSpace;
            }else{//多行情况
                needHeight = needHeight + lineHeight;
                needWidth = widthSize;
            }

        }



        int desireWidth = 0;
        int desireHeight = 0;

        switch (widthMode){
            case MeasureSpec.AT_MOST:
                desireWidth = needWidth;
                break;
            case MeasureSpec.EXACTLY:
                desireWidth = widthSize;
                break;
        }

        switch (heightMode){
            case MeasureSpec.AT_MOST:
                if(needHeight > heightSize){
                    desireHeight = heightSize;
                }else{
                    desireHeight = needHeight;
                }
                break;
            case MeasureSpec.EXACTLY:
                desireHeight = heightSize;
                break;
        }
        setMeasuredDimension(desireWidth,desireHeight);
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    class LayoutParams extends ViewGroup.LayoutParams{

        public int lineIndex;//行索引

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }
    }
}
