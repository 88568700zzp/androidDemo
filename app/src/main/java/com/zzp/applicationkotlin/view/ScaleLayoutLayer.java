package com.zzp.applicationkotlin.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateInterpolator;
import android.widget.RelativeLayout;

/**
 * @Auther: Paak
 * @Date: 2022/7/12
 * @Description: 缩放层级
 */
public class ScaleLayoutLayer extends RelativeLayout implements View.OnTouchListener {
    private boolean isCanTouch = true;
    // 当前触摸点数
    private int pointNum = 0;
    //最大的缩放比例
    private static final float SCALE_MAX = 8.0f;
    private static final float SCALE_MIN = 0.8f;
    private double oldDist = 0;
    private double moveDist = 0;
    private float currentX = 0;
    private float currentY = 0;
    private float mTouchSlot = 0;
    private int mActivePointerId = -1;
    private float[] mInfos = new float[2];

    private float parentLayoutHeight = 0f;//父布局高度

    private boolean mStartDrag = false;
    private boolean mStartScale = false;
    private boolean mIsAnimating = false;
    private float mTouchDownScale = 1f;

    private boolean mForceEdge = false;
    private RectF mForceEdgeRect = new RectF();
    private RectF mVisibleRect = new RectF();
    private RectF mRect = new RectF();

    //裁剪相关信息
    private RectF mClipRect = new RectF();
    private RectF mClipBorder = new RectF();
    private boolean mHasClip = false;
    private float mClipTranslateX = 0f;
    private float mClipTranslateY = 0f;
    private float mClipScale = 1f;

    private Matrix mMatrix = new Matrix();

    private ChangeListener mChangeListener;

    public ScaleLayoutLayer(Context context) {
        this(context, null);
    }

    public ScaleLayoutLayer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaleLayoutLayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnTouchListener(this);
        init();
    }

    private void init() {
        mTouchSlot = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        setIsCanTouch(true);
    }

    public void setIsCanTouch(boolean canTouch) {
        isCanTouch = canTouch;
    }

    public void setScaleChangeListener(ChangeListener changeListener) {
        mChangeListener = changeListener;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                pointNum = 1;
                mStartDrag = false;
                mStartScale = false;

                mActivePointerId = event.getPointerId(0);

                getMotionEvent(event);
                currentX = mInfos[0];
                currentY = mInfos[1];

                mTouchDownScale = getScaleX();
                break;
            case MotionEvent.ACTION_UP:
                notifyBoundChange();
                if (mStartScale) {
                    checkScaleBorder();
                }
                pointNum = 0;
                mStartDrag = false;
                mStartScale = false;
                currentX = 0;
                currentY = 0;
                mActivePointerId = -1;

                break;
            case MotionEvent.ACTION_POINTER_DOWN: {
                pointNum += 1;
                if(pointNum == 2) {
                    //两点按下时的距离
                    oldDist = spacing(event);
                }
            }
            break;
            case MotionEvent.ACTION_POINTER_UP: {
                pointNum -= 1;
                if(pointNum == 1){
                    onSecondaryPointerUp(event);
                    getMotionEvent(event);
                    currentX = mInfos[0];
                    currentY = mInfos[1];
                }
                if(pointNum == 2) {
                    //两点按下时的距离
                    oldDist = spacing(event);
                }
            }
            break;
            default:
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >>
                MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        final int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mActivePointerId = ev.getPointerId(newPointerIndex);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (pointNum > 1) return true;
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mChangeListener != null){
            mChangeListener.onMotionEvent(event);
        }
        if (!isCanTouch || mIsAnimating) {
            return false;
        }
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_MOVE:
                if (pointNum == 2) { //缩放操作
                    mStartScale = true;
                    moveDist = spacing(event);
                    double space = moveDist - oldDist;
                    float scale = (float) (getScaleX() + space / getWidth());
                    if (scale > SCALE_MIN && scale < SCALE_MAX) {
                        setScale(scale);
                    } else if (scale < SCALE_MIN) {
                        setScale(SCALE_MIN);
                    } else if(scale > SCALE_MAX){
                        setScale(SCALE_MAX);
                    }
                } else if (pointNum == 1) {
                    getMotionEvent(event);
                    float diffX =  mInfos[0] - currentX;
                    float diffY = mInfos[1] - currentY;
                    if (Math.abs(diffX) >= mTouchSlot || Math.abs(diffY) >= mTouchSlot) {
                        mStartDrag = true;
                    }
                    if (mStartDrag) {
                        getTransformRect();

                        Log.d("zzp123", "mRect:" + mRect.toString());
                        Log.d("zzp123", "getTranslationX():" + getTranslationX() + " getScaleX():" + getScaleX() + " diffX:" + diffX);


                        float leftEdge = 0;
                        float rightEdge = getWidth();
                        float topEdge = 0;
                        float bottomEdge = getHeight();
                        if (mForceEdge) {
                            leftEdge = mForceEdgeRect.left;
                            rightEdge = mForceEdgeRect.right;
                            topEdge = mForceEdgeRect.top;
                            bottomEdge = mForceEdgeRect.bottom;

                        }else if(mHasClip){
                            leftEdge = mClipBorder.left;
                            rightEdge = mClipBorder.right;
                            topEdge = mClipBorder.top;
                            bottomEdge = mClipBorder.bottom;
                        }

                        /*if (mRect.width() < (rightEdge - leftEdge)) {
                            if (mRect.left + diffX <= leftEdge) {
                                diffX = leftEdge - mRect.left;
                            } else if (diffX + mRect.right >= rightEdge) {
                                diffX = rightEdge - mRect.right;
                            }
                        }*/
                        if(mRect.left > leftEdge || mRect.right < rightEdge){
                            diffX = 0f;
                        } else {
                            if (mRect.left + diffX >= leftEdge) {
                                diffX = leftEdge - mRect.left;
                            } else if (diffX + mRect.right <= rightEdge) {
                                diffX = rightEdge - mRect.right;
                            }
                        }

                        /*if (mRect.height() < (bottomEdge - topEdge)) {
                            if (diffY + mRect.top <= topEdge) {
                                diffY = topEdge - mRect.top;
                            } else if (diffY + mRect.bottom >= bottomEdge) {
                                diffY = bottomEdge - mRect.bottom;
                            }
                        }*/
                        if(mRect.top > topEdge ||  mRect.bottom < bottomEdge){
                            diffY = 0;
                        } else {
                            if (diffY + mRect.top >= topEdge) {
                                diffY = topEdge - mRect.top;
                            } else if (diffY + mRect.bottom <= bottomEdge) {
                                diffY = bottomEdge - mRect.bottom;
                            }
                        }

                        setTranslationX(getTranslationX() + diffX);
                        setTranslationY(getTranslationY() + diffY);

                        if(mChangeListener != null){
                            mChangeListener.onTranslateChange(diffX,diffY);
                        }

                        currentX = mInfos[0];
                        currentY = mInfos[1];

                    }
                }
                break;
            default:
                break;
        }
        return true;
    }

    private void getMotionEvent(MotionEvent event){
        MotionEvent motionEvent = MotionEvent.obtain(event);
        if(!getMatrix().isIdentity()) {
            motionEvent.transform(getMatrix());
        }

        int pointerIndex = event.findPointerIndex(mActivePointerId);

        if(pointerIndex == -1){
            pointerIndex = 0;
            mActivePointerId = event.getPointerId(pointerIndex);
        }

        mInfos[0] = motionEvent.getX(pointerIndex);
        mInfos[1] = motionEvent.getY(pointerIndex);

        motionEvent.recycle();
    }


    private void getTransformRect() {
        if (mHasClip) {
            mRect.set(mClipRect);
        } else {
            mRect.set(0, 0, getWidth(), getHeight());
        }
        getMatrix().mapRect(mRect);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }


    /**
     * 计算两个点的距离
     *
     * @param event 事件
     * @return 返回数值
     */
    private double spacing(MotionEvent event) {
        if (event.getPointerCount() >= 2) {
            float x = event.getX(0) - event.getX(1);
            float y = event.getY(0) - event.getY(1);
            return Math.sqrt(x * x + y * y);
        } else {
            return 0;
        }
    }

    /**
     * 平移画面，当画面的宽或高大于屏幕宽高时，调用此方法进行平移
     *
     * @param x 坐标x
     * @param y 坐标y
     */
    public void setPivot(float x, float y) {
        setPivotX(x);
        setPivotY(y);
    }

    /**
     * 设置放大缩小
     *
     * @param scale 缩放值
     */
    public void setScale(float scale) {
        setScaleX(scale);
        setScaleY(scale);
        if (mChangeListener != null) {
            mChangeListener.onScaleChange(scale);
        }
    }

    /**
     * 初始化比例，也就是原始比例
     */
    public void setInit() {
        setScale(1.0f);
        setTranslationX(0f);
        setTranslationY(0f);
        setPivot(getWidth() / 2f, getHeight() / 2f);
        notifyBoundChange();
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (mHasClip) {
            canvas.save();

            if (mHasClip) {
                canvas.clipRect(mClipRect);
            }

            super.dispatchDraw(canvas);

            canvas.restore();
        } else {
            super.dispatchDraw(canvas);
        }
    }

    public void resetClip() {
        mHasClip = false;
        invalidate();
    }

    public void setClipInfo(RectF clipInfo) {
        mClipRect.set(clipInfo);
        mHasClip = true;

        float height = parentLayoutHeight;
        float width = getWidth();

        float clipWidth = mClipRect.width();
        float clipHeight = mClipRect.height();
        if(getRotation() % 180 != 0){
            clipHeight = mClipRect.width();
            clipWidth = mClipRect.height();
        }
        float left = 0;
        float top = 0;
        float right = 0;
        float bottom = 0;

        float widthScale = width * 1f / clipWidth;
        float heightScale = height * 1f / clipHeight;
        if(widthScale < heightScale){
            mClipScale = widthScale;
            left = 0;
            right = width;
            top = getHeight()/2 - clipHeight * mClipScale/2;
            bottom = getHeight()/2 + clipHeight * mClipScale/2;
        }else{
            mClipScale = heightScale;

            left = getWidth()/2 - clipWidth * mClipScale/2;
            right =getWidth()/2 + clipWidth * mClipScale/2;
            top = getHeight()/2 - parentLayoutHeight/2;
            bottom = getHeight()/2 + parentLayoutHeight/2;
        }
        mClipBorder.set(left,top,right,bottom);

        mMatrix.reset();
        mMatrix.postScale(mClipScale,mClipScale,mClipRect.centerX(),mClipRect.centerY());
        mMatrix.mapRect(mRect,mClipRect);


        mClipTranslateX = mClipBorder.centerX() - mRect.centerX();
        mClipTranslateY = mClipBorder.centerY() - mRect.centerY();

        invalidate();

    }

    public void doRotate(RectF bound){
        float height = parentLayoutHeight;
        float width = getWidth();

        float clipWidth = bound.width();
        float clipHeight = bound.height();
        if(getRotation() % 180 != 0){
            clipHeight = bound.width();
            clipWidth = bound.height();
        }

        float left = 0;
        float top = 0;
        float right = 0;
        float bottom = 0;

        float scale = 1f;
        float translateX = 0f;
        float translateY = 0f;

        float widthScale = width * 1f / clipHeight;
        float heightScale = height * 1f / clipWidth;
        if(widthScale < heightScale){
            scale = widthScale;
            left = 0;
            right = width;
            top = getHeight()/2 - clipHeight * scale/2;
            bottom = getHeight()/2 + clipHeight * scale/2;
        }else{
            scale = heightScale;

            left = getWidth()/2 - clipWidth * scale/2;
            right =getWidth()/2 + clipWidth * scale/2;
            top = getHeight()/2 - parentLayoutHeight/2;
            bottom = getHeight()/2 + parentLayoutHeight/2;
        }
        mMatrix.reset();
        mMatrix.postScale(scale,scale,bound.centerX(),bound.centerY());
        mMatrix.mapRect(mRect,bound);


        translateX = left + (right - left)/2 - mRect.centerX();
        translateY = top + (bottom - top)/2 - mRect.centerY();

        setPivot(bound.centerX(), bound.centerY());
        setScale(scale);
        setTranslationX(translateX);
        setTranslationY(translateY);
        setRotation(getRotation() + 90f);

        notifyBoundChange();
    }

    public RectF getClipInfo(){
        if(mHasClip){
            return mClipRect;
        }else{
            return null;
        }
    }

    /**
     * 裁剪区域填空view
     */
    public void setClipFillView(boolean animate) {
        if (mHasClip && !mClipRect.isEmpty()) {
            setPivot(mClipRect.centerX(), mClipRect.centerY());
            if(animate) {
                doAnimate(mClipTranslateX, mClipTranslateY, mClipScale);
            }else{
                setScale(mClipScale);
                setTranslationX(mClipTranslateX);
                setTranslationY(mClipTranslateY);
            }
        }
    }

    /**
     * 根据裁剪区域 设置滑动边界
     */
    public void setForceEdge(float left, float top, float right, float bottom) {
        mForceEdge = true;
        mForceEdgeRect.set(left, top, right, bottom);
    }

    public void clearForceEdge() {
        mForceEdge = false;
        mForceEdgeRect.setEmpty();
    }

    private void checkScaleBorder() {//缩放之后的处理
        getTransformRect();
        if (mForceEdge) {//裁剪强制边界
            checkForceBorder();
        }else if(mHasClip){
            if (mRect.left > mClipBorder.left || mRect.top > mClipBorder.top || mRect.bottom < mClipBorder.bottom || mRect.right < mClipBorder.right) {
                doAnimate(mClipTranslateX,mClipTranslateY,mClipScale);
            }
        } else {
            mVisibleRect.set(0, 0, getWidth(), getHeight());
            boolean sameDirection = (mRect.width() >= getWidth() && mRect.height() >= getHeight()) || (mRect.width() < getWidth() && mRect.height() < getHeight());
            if(sameDirection){
                if (mRect.left > mVisibleRect.left || mRect.top > mVisibleRect.top || mRect.bottom < mVisibleRect.bottom || mRect.right < mVisibleRect.right) {//小于4个边角触发
                    if (mHasClip) {
                        setPivot(mClipRect.centerX(), mClipRect.centerY());
                        doAnimate(mClipTranslateX,mClipTranslateY,mClipScale);
                    } else {
                        setPivot(getWidth() / 2, getHeight() / 2);
                        doAnimate(0f,0f,1f);
                    }
                }
            }else{
                float translateByX = 0f;
                float translateByY = 0f;
                if(mRect.width() < getWidth()){
                    if(mRect.right > getWidth() || mRect.left < 0){
                        translateByX = getWidth()/2 - mRect.centerX();
                    }
                }else{
                    if(mRect.right < getWidth() || mRect.left > 0){
                        translateByX = getWidth()/2 - mRect.centerX();
                    }
                }
                if(mRect.height() < getHeight()){
                    if(mRect.bottom > getHeight() || mRect.top < 0){
                        translateByY = getHeight()/2 - mRect.centerY();
                    }
                }else{
                    if(mRect.bottom < getHeight() || mRect.top > 0){
                        translateByY = getHeight()/2 - mRect.centerY();
                    }
                }
                if(translateByX != 0 || translateByY != 0){
                    doAnimate(getTranslationX() + translateByX,getTranslationY() + translateByY,getScaleX());
                }
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        parentLayoutHeight = ((View)getParent()).getHeight();
    }

    private void checkForceBorder(){
        float scaleTargetX = getScaleX();
        float scaleTargetY = getScaleX();

        boolean effect = false;

        if (mRect.left > mForceEdgeRect.left || mRect.right < mForceEdgeRect.right) {
            if(mRect.width() < mForceEdgeRect.width()){
                scaleTargetX = mForceEdgeRect.width() * 1f/mRect.width() * getScaleX();
            }
            effect = true;
        }

        if(mRect.top > mForceEdgeRect.top || mRect.bottom < mForceEdgeRect.bottom){
            if(mRect.height() < mForceEdgeRect.height()){
                scaleTargetY = mForceEdgeRect.height() * 1f/mRect.height() * getScaleY();
            }
            effect = true;
        }

        if(effect) {
            if (scaleTargetY != getScaleY() || scaleTargetX != getScaleX()) {//涉及放大缩小
                RectF rect = new RectF(0, 0, getWidth(), getHeight());
                float maxScale = Math.max(scaleTargetY,scaleTargetX);
                mMatrix.reset();
                mMatrix.postScale(maxScale,maxScale,getPivotX(),getPivotY());
                mMatrix.mapRect(rect);
                float scaleTranslateX = mForceEdgeRect.centerX() - rect.centerX();
                float scaleTranslateY = mForceEdgeRect.centerY() - rect.centerY();
                if(mForceEdgeRect.left < mRect.left){
                    scaleTranslateX = scaleTranslateX + (rect.width() -mForceEdgeRect.width())/2;
                }else if(mForceEdgeRect.right > mRect.right){
                    scaleTranslateX = scaleTranslateX - (rect.width() -mForceEdgeRect.width())/2;
                }
                if(mForceEdgeRect.top < mRect.top){
                    scaleTranslateY = scaleTranslateY + (rect.height() -mForceEdgeRect.height())/2;
                }else if(mForceEdgeRect.bottom > mRect.bottom){
                    scaleTranslateY = scaleTranslateY - (rect.height() -mForceEdgeRect.height())/2;
                }
                doAnimate(scaleTranslateX, scaleTranslateY, maxScale);
            } else {//单纯平移
                float translateByX = 0f;
                float translateByY = 0f;
                if (mRect.top > mForceEdgeRect.top || mRect.bottom < mForceEdgeRect.bottom) {
                    if (mRect.height() >= mForceEdgeRect.height()) {
                        if (mRect.top > mForceEdgeRect.top) {
                            translateByY = mForceEdgeRect.top - mRect.top;
                        } else if (mRect.bottom < mForceEdgeRect.bottom) {
                            translateByY = mForceEdgeRect.bottom - mRect.bottom;
                        }
                    }
                }
                if (mRect.left > mForceEdgeRect.left || mRect.right < mForceEdgeRect.right) {
                    if (mRect.width() >= mForceEdgeRect.width()) {
                        if (mRect.left > mForceEdgeRect.left) {
                            translateByX = mForceEdgeRect.left - mRect.left;
                        } else if (mRect.right < mForceEdgeRect.right) {
                            translateByX = mForceEdgeRect.right - mRect.right;
                        }
                    }
                }
                doAnimate(getTranslationX() + translateByX, getTranslationY() + translateByY, getScaleX());
            }

        }

    }

    /**
     * 做动画平移，缩放
     * @param translateX
     * @param translateY
     * @param scale
     */
    public void doAnimate(float translateX,float translateY,float scale){
        doAnimate(translateX,translateY,scale,150);
    }

    /**
     * 做动画平移，缩放
     * @param translateX
     * @param translateY
     * @param scale
     */
    public void doAnimate(float translateX,float translateY,float scale,long time){
        float startTranlateX = getTranslationX();
        float startTranlateY = getTranslationY();
        float startScale = getScaleX();

        float translateByX = translateX - startTranlateX;
        float translateByY = translateY - startTranlateY;
        float scaleBy = scale - startScale;

        ValueAnimator valueAnimator = ValueAnimator.ofInt(0,100);
        valueAnimator.setDuration(time);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int progress = (int) animation.getAnimatedValue();
                if(translateByX != 0) {
                    setTranslationX(translateByX * progress / 100 + startTranlateX);
                }
                if(translateByY != 0) {
                    setTranslationY(translateByY * progress / 100 + startTranlateY);
                }
                if(scaleBy != 0) {
                    setScale(scaleBy * progress / 100 + startScale);
                }
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mIsAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mIsAnimating = false;
                if(translateByX != 0) {
                    setTranslationX(translateByX + startTranlateX);
                }
                if(translateByY != 0) {
                    setTranslationY(translateByY + startTranlateY);
                }
                if(scaleBy != 0) {
                    setScale(scaleBy  + startScale);
                }
                notifyBoundChange();
            }
        });
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.start();
    }

    /**
     * 做动画平移，缩放
     * @param scale
     */
    public void doAnimate2(float scale,RectF startRect,RectF targetRect,RectF bound,long time,Animator.AnimatorListener listener){

        setPivot(bound.centerX(),bound.centerY());

        float startScale = getScaleX();

        float scaleBy = scale - startScale;

        RectF tempRect = new RectF();

        ValueAnimator valueAnimator = ValueAnimator.ofInt(0,100);
        valueAnimator.setDuration(time);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress = (int) animation.getAnimatedValue()/100f;
                float currentScale = scaleBy * progress + startScale;

                setScale(currentScale);

                getMatrix().mapRect(mRect,bound);

                tempRect.set((targetRect.left - startRect.left) * progress + startRect.left,
                        (targetRect.top - startRect.top) * progress + startRect.top,
                        (targetRect.right - startRect.right) * progress + startRect.right,
                        (targetRect.bottom - startRect.bottom) * progress + startRect.bottom
                );
                setTranslationX(getTranslationX() + tempRect.centerX() - mRect.centerX());
                setTranslationY(getTranslationY() + tempRect.centerY() - mRect.centerY());
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mIsAnimating = true;

                if(listener != null){
                    listener.onAnimationStart(animation);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mIsAnimating = false;

                setScale(scale);

                getMatrix().mapRect(mRect,bound);

                setTranslationX(getTranslationX() + targetRect.centerX() - mRect.centerX());
                setTranslationY(getTranslationY() + targetRect.centerY() - mRect.centerY());

                notifyBoundChange();

                if(listener != null){
                    listener.onAnimationEnd(animation);
                }
            }
        });
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.start();
    }

    /**
     * 通知内容区域发生变化
     */
    public void notifyBoundChange() {
        getTransformRect();
        if (mChangeListener != null) {
            mChangeListener.onBoundChange(mRect);
        }
    }


    public interface ChangeListener {
        void onScaleChange(float scale);

        void onBoundChange(RectF rectF);

        void onTranslateChange(float diffX,float diffY);

        void onMotionEvent(MotionEvent event);
    }
}
