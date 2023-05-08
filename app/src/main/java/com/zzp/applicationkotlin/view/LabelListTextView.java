package com.zzp.applicationkotlin.view;

import static java.lang.Math.max;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 标签列表文本
 */
public class LabelListTextView extends View {

    private List<String> mLabelList = new ArrayList<>();

    private Paint.FontMetrics mFontMetrics = new Paint.FontMetrics();
    private RectF mRect = new RectF();

    private Paint mPaint;
    private float mTextSize;
    private float mMarginSpace;
    private float mRoundRadius;

    private float mRightSpace;//右边剩余空间

    private float mHPadding;//字体水平padding
    private float mVPadding;//字体竖直padding

    public LabelListTextView(Context context) {
        super(context);
        init();
    }

    public LabelListTextView(Context context,  AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        mMarginSpace = 4 * 3;
        mTextSize = 15 * 3;

        mRoundRadius = 2 * 3;

        mRightSpace = 16 * 3;

        mHPadding = 4 * 3;
        mVPadding = 1 * 3;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextAlign(Paint.Align.CENTER);

        for(int i = 0;i < 10;i++) {
            mLabelList.add("lable" + i);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(mLabelList.size() == 0){
            return;
        }

        mPaint.setTextSize(mTextSize);
        mPaint.getFontMetrics(mFontMetrics);

        float textContentHeight = mFontMetrics.bottom - mFontMetrics.top;
        float rectHeight = textContentHeight + 2 * mVPadding;

        float startX = 0;

        for(int i= 0; i < mLabelList.size();i++){

            String label = mLabelList.get(i);

            float textContentWidth = mPaint.measureText(label) ;
            float rectWidth = textContentWidth + 2 * mHPadding;

            if(i == mLabelList.size() - 1) {//最后一个
                if (startX + rectWidth > getWidth()) {//超出view的边界了
                    drawEndMark(canvas,startX,rectHeight);
                    break;
                }
            }else{
                if (startX + rectWidth > getWidth() - mRightSpace) {//超出view的边界了
                    drawEndMark(canvas,startX,rectHeight);
                    break;
                }
            }

            drawLabelText(canvas,label,startX,rectWidth,rectHeight,Color.parseColor("#006EF0"),Color.parseColor("#E6F1FE"));

            startX = startX + rectWidth + mMarginSpace;
        }
    }

    private void drawLabelText(Canvas canvas,String txt,float startX,float rectWidth,float rectHeight,int textColor,int bgRectColor) {
       float centerY = getHeight()/2;

        mPaint.setColor(bgRectColor);
        mPaint.setStyle(Paint.Style.FILL);
        mRect.set(startX, centerY - rectHeight / 2, startX + rectWidth, centerY + rectHeight / 2);
        canvas.drawRoundRect(mRect, mRoundRadius, mRoundRadius, mPaint);

        mPaint.setColor(textColor);
        canvas.drawText(txt,startX + rectWidth / 2, centerY - (mFontMetrics.ascent + mFontMetrics.descent)/2, mPaint);
    }

    private void drawEndMark(Canvas canvas,float startX,float rectHeight){
        String label = "··";

        float rectWidth = mRightSpace;

        drawLabelText(canvas,label,startX,rectWidth,rectHeight,Color.parseColor("#006EF0"),Color.parseColor("#E6F1FE"));

    }

    public void setTextSize(float textSize){
        mTextSize = textSize;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        mPaint.setTextSize(mTextSize);
        mPaint.getFontMetrics(mFontMetrics);
        int contentHeight = (int)(mFontMetrics.bottom - mFontMetrics.top + 2 * mVPadding);

        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);

        switch (heightMode){
            case MeasureSpec.AT_MOST:
                if(contentHeight < heightSize){
                    height = contentHeight;
                }
                break;
        }

        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                height);
    }

}
