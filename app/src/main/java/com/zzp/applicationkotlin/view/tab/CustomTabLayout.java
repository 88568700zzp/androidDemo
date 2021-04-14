package com.zzp.applicationkotlin.view.tab;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by samzhang on 2021/4/14.
 */
public class CustomTabLayout extends HorizontalScrollView implements View.OnClickListener{


    private int mSelectIndex = -1;

    private Paint mPaint;

    private float mIndicatorHeight;
    private float mIndicatorWidth;
    private RectF mRectF;

    private TabListener mTabListener;

    private float mTextSize = 15;

    public CustomTabLayout(Context context) {
        this(context,null);
    }

    public CustomTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mIndicatorHeight = 3 * 2.75F;
        mIndicatorWidth = 26 * 2.75F;

        mRectF = new RectF();
    }

    public void setTabListener(TabListener tabListener) {
        this.mTabListener = tabListener;
    }

    private LinearLayout getContainerView(){
        View containView = getChildAt(0);
        if(!(containView instanceof LinearLayout)){
            return null;
        }
        return (LinearLayout)containView;
    }

    public void setTab(List<String> data){
        LinearLayout containView = getContainerView();
        if(containView == null){
            return;
        }
        containView.removeAllViews();

        for(int i = 0;i < data.size();i++) {
            TextView textView = new TextView(getContext());
            textView.setText(data.get(i));
            textView.setTag(i);
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(15);
            textView.setGravity(Gravity.CENTER);
            textView.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

            textView.setPadding((int)(20 * 2.75f),0,(int)(20 * 2.75f),0);
            textView.setOnClickListener(this);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.MATCH_PARENT);
            containView.addView(textView,layoutParams);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    private void resetAllView(){
        LinearLayout containView = getContainerView();
        if(containView == null){
            return;
        }
        for(int i =0 ; i < containView.getChildCount();i++){
            View childView = containView.getChildAt(i);
            if(childView instanceof TextView){
                ((TextView)childView).setTextSize(mTextSize);
            }
        }

    }

    public void setSelectIndex(int selectIndex){
        if(selectIndex == mSelectIndex){
            return;
        }
        resetAllView();
        View selectView = getViewByIndex(selectIndex);
        LinearLayout containView = getContainerView();
        if(selectView != null && containView != null){
            this.mSelectIndex = selectIndex;
            if(mTabListener != null){
                mTabListener.onViewSelect(mSelectIndex,selectView);
            }
            if(containView.getWidth() > getWidth()){
                int scrollX = selectView.getLeft() + selectView.getWidth()/2 - getWidth()/2;
                int maxScrollX = containView.getWidth() - getWidth();
                if(scrollX < 0){
                    scrollX = 0;
                }
                if(scrollX > maxScrollX){
                    scrollX = maxScrollX;
                }
                smoothScrollTo(scrollX,0);
            }
            invalidate();
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        View selectView = getViewByIndex(mSelectIndex);
        if(selectView != null){

            float startX = selectView.getLeft() + (selectView.getWidth() - mIndicatorWidth)/2;

            mPaint.setColor(Color.WHITE);
            mRectF.set(startX,getHeight() - mIndicatorHeight,startX + mIndicatorWidth,getHeight());
            canvas.drawRoundRect(mRectF, mIndicatorHeight /2, mIndicatorHeight /2,mPaint);
        }
    }

    private View getViewByIndex(int index){
        View selectView = findViewWithTag(index);
        return selectView;
    }

    @Override
    public void onClick(View v) {
        if(v.getTag() != null){
            int index = (int) v.getTag();
            setSelectIndex(index);
            if(mTabListener != null){
                mTabListener.onTabSelect(index);
            }
        }
    }

    public interface TabListener{
        void onTabSelect(int index);
        void onViewSelect(int index,View view);
    }
}
