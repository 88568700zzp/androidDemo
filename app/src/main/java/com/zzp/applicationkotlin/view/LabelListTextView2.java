package com.zzp.applicationkotlin.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.zzp.applicationkotlin.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 标签列表文本
 */
public class LabelListTextView2 extends View {

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

    private int mBgColor;
    private int mTextColor;

    private int mDrawablePadding;

    private Bitmap mArrowBitmap;
    private Bitmap mAddBitmap;

    public LabelListTextView2(Context context) {
        super(context);
        init();
    }

    public LabelListTextView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        mMarginSpace = 3 * 3;
        mTextSize = 11 * 3;

        mRoundRadius = 2 * 3;

        mRightSpace = 16 * 3;

        mHPadding = 4 * 3;
        mVPadding = 1 * 3;

        mDrawablePadding = 2 * 3;

        mBgColor = Color.parseColor("#E6F1FE");
        mTextColor = Color.parseColor("#006EF0");

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextAlign(Paint.Align.CENTER);

        mArrowBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cls_edit_label_arrow);
        mAddBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cls_edit_label_add);

        /*for(int i = 0;i < 3;i++) {
            mLabelList.add("lable" + i);
        }*/
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(mLabelList.size() == 0){
            drawNoneLabel(canvas);
            return;
        }

        mPaint.setTextSize(mTextSize);
        mPaint.getFontMetrics(mFontMetrics);

        float textContentHeight = mFontMetrics.bottom - mFontMetrics.top;
        float rectHeight = textContentHeight + 2 * mVPadding;

        float startX = 0;


        for(int i= 0; i < mLabelList.size();i++){

            String label = mLabelList.get(i);

            float textContentWidth = mPaint.measureText(label);


            if(i < mLabelList.size() - 1) {

                float rectWidth = textContentWidth + 2 * mHPadding;

                String nextLabel = mLabelList.get(i + 1);
                float nextTextContentWidth = mPaint.measureText(nextLabel);
                float nextRectWidth = nextTextContentWidth + 2 * mHPadding;

                if (startX + rectWidth + mMarginSpace + nextRectWidth + mRightSpace > getWidth()) {
                    //超出view的边界了,画label··标签
                    String newLabel = label + "··";
                    float newTextContentWidth = mPaint.measureText(newLabel);
                    float startBitmapX = startX + mHPadding + newTextContentWidth + mDrawablePadding;
                    rectWidth = newTextContentWidth + mDrawablePadding + mArrowBitmap.getWidth() + mHPadding * 2;
                    drawLabelTextWithBitmap(canvas,newLabel,startX + mHPadding + newTextContentWidth/2,startX,rectWidth,rectHeight,startBitmapX,mArrowBitmap);
                    break;
                } else {
                    drawLabelText(canvas, label, startX, rectWidth, rectHeight);
                }

                startX = startX + rectWidth + mMarginSpace;
            }else{//画最后一个
                float startBitmapX = startX + mHPadding + textContentWidth  + mDrawablePadding;
                float rectWidth = textContentWidth  + mDrawablePadding + mArrowBitmap.getWidth() + mHPadding * 2;
                drawLabelTextWithBitmap(canvas,label,startX + mHPadding + textContentWidth/2,startX,rectWidth,rectHeight,startBitmapX,mArrowBitmap);
            }
        }

    }

    private void drawLabelText(Canvas canvas,String txt,float startRectX,float rectWidth,float rectHeight) {
        float centerY = getHeight()/2;

        mPaint.setColor(mBgColor);
        mPaint.setStyle(Paint.Style.FILL);
        mRect.set(startRectX, centerY - rectHeight / 2, startRectX + rectWidth, centerY + rectHeight / 2);
        canvas.drawRoundRect(mRect, mRoundRadius, mRoundRadius, mPaint);

        mPaint.setColor(mTextColor);
        canvas.drawText(txt,startRectX + rectWidth/2, centerY - (mFontMetrics.ascent + mFontMetrics.descent)/2, mPaint);
    }

    private void drawLabelTextWithBitmap(Canvas canvas,String txt,float centerTxtX,float startRectX,float rectWidth,float rectHeight,float startBitmapX,Bitmap bitmap) {
        float centerY = getHeight()/2;

        mPaint.setColor(mBgColor);
        mPaint.setStyle(Paint.Style.FILL);
        mRect.set(startRectX, centerY - rectHeight / 2, startRectX + rectWidth, centerY + rectHeight / 2);
        canvas.drawRoundRect(mRect, mRoundRadius, mRoundRadius, mPaint);

        mPaint.setColor(mTextColor);
        canvas.drawText(txt,centerTxtX, centerY - (mFontMetrics.ascent + mFontMetrics.descent)/2, mPaint);

        mRect.set(startBitmapX,centerY - bitmap.getHeight()/2,startBitmapX + bitmap.getWidth(),centerY + bitmap.getHeight()/2);
        canvas.drawBitmap(bitmap,null,mRect,mPaint);
    }

    private void drawNoneLabel(Canvas canvas){
        mPaint.setTextSize(mTextSize);
        mPaint.getFontMetrics(mFontMetrics);

        float textContentHeight = mFontMetrics.bottom - mFontMetrics.top;
        float rectHeight = textContentHeight + 2 * mVPadding;

        String label = "添加标签";

        float startX = 0;
        float textContentWidth = mPaint.measureText(label);

        float startBitmapX = startX + mHPadding + textContentWidth + mDrawablePadding;
        float rectWidth = textContentWidth + mAddBitmap.getWidth() + mHPadding * 2 + mDrawablePadding;
        drawLabelTextWithBitmap(canvas,label,startX + mHPadding + textContentWidth/2,startX,rectWidth,rectHeight,startBitmapX,mAddBitmap);

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
