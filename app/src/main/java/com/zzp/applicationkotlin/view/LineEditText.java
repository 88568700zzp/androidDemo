package com.zzp.applicationkotlin.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

public class LineEditText extends EditText {

    private float mLinePaddingLeft;
    private float mLinePaddingRight;

    private Paint mPaint;
    private Paint.FontMetrics mFontMetrics;
    private Rect mRect;


    public LineEditText(Context context) {
        super(context);
        init();
    }

    public LineEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LineEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mLinePaddingLeft = 20;
        mLinePaddingRight = 20;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        mPaint.setColor(Color.parseColor("#FFEBEEF4"));

        mFontMetrics = new Paint.FontMetrics();
        mRect = new Rect();

        setShadowLayer(28,0,0,Color.WHITE);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Layout layout = getLayout();
        if(layout == null || layout.getLineCount() <= 0){
            return;
        }



        float scrollY = getScrollY();
        float extendedPaddingTop = getExtendedPaddingTop();
        float extendedPaddingBottom = getExtendedPaddingBottom();
        float maxScrollY = layout.getHeight() - getHeight();
        float extraSpace = getLineSpacingExtra() /2;
        float lineHeight = getLineHeight();
        //只处理gravity为top的情况,下划线开始起始y坐标
        float startY = extendedPaddingTop;


        float clipLeft = 0;
        float clipTop = (scrollY == 0) ? 0 : extendedPaddingTop + scrollY;
        float clipRight = getRight();
        float clipBottom = getHeight() + scrollY
                - ((scrollY == maxScrollY) ? 0 : extendedPaddingBottom);

        //只处理gravity为top的情况
        canvas.save();
        //canvas.clipRect(clipLeft,clipTop,clipRight,clipBottom);


        while(startY <= clipBottom){
            float end = startY + lineHeight - (getLineSpacingExtra() - extraSpace);

            if(end >= clipTop) {
                canvas.drawLine(mLinePaddingLeft, end, getWidth() - mLinePaddingRight, end, mPaint);
            }

            startY += lineHeight;
        }

        canvas.restore();

        /*float clipLeft = 0;
        float clipTop = (scrollY == 0) ? 0 : extendedPaddingTop + scrollY;
        float clipRight = getRight();
        float clipBottom = getHeight() + scrollY
                - ((scrollY == maxScrollY) ? 0 : extendedPaddingBottom);

        //只处理gravity为top的情况
        canvas.save();
        canvas.clipRect(clipLeft,clipTop,clipRight,clipBottom);
        canvas.translate(0,getExtendedPaddingTop());


        for(int index = 0;index < layout.getLineCount();index++){
            float end = layout.getLineBottom(index) - (getLineSpacingExtra() - extraSpace);
            if(index == layout.getLineCount() - 1){//最后一行
                end = layout.getLineBottom(index) + extraSpace;
            }
            canvas.drawLine(mLinePaddingLeft,end,getWidth() - mLinePaddingRight,end,mPaint);
        }

        canvas.restore();*/
    }

}
