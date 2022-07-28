package com.zzp.applicationkotlin.view

import android.content.Context
import android.graphics.*
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import com.zzp.applicationkotlin.R
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt


class TextEditLayer : FrameLayout {

    var mTextInfoList = ArrayList<TextInfo>()

    private var mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var parentScale = 1f// 父View缩放值


    private var mPaddingH = ScreenUtils.dpToPx(16f)
    private var mPaddingV = ScreenUtils.dpToPx(8f)

    private var mEditTextMaxWidth = ScreenUtils.dpToPx(150f)

    private var mBgColor = Color.parseColor("#4D2F353F")
    private var mBgRound = ScreenUtils.dpToPx(4f)

    private var mRect = RectF()

    private var mTouchTextInfo: TextInfo? = null//记录正在操作的文本信息
    private var mCurrentX = 0f
    private var mCurrentY = 0f
    private var mTouchSlot = 0

    private var mCloseBitmap: Bitmap
    private var mDragBitmap: Bitmap

    private var mMatrix = Matrix()

    private var isCanTouch = false

    var mListener: IEditListener? = null

    constructor(context: Context, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
        mTouchSlot = ViewConfiguration.get(context).scaledTouchSlop

        mCloseBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_scan_edit_close_btn)
        mDragBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_scan_edit_drag_btn)
    }

    /**
     * 添加文本
     */
    fun addText(content: String, textSize: Float, @ColorInt textColor: Int) {
        val parentView = parent as View
        val points = floatArrayOf(

            parentView.width / 2f, parentView.height / 2f

        )
        mMatrix.reset()
        if (parentView.matrix.invert(mMatrix)) {
            mMatrix.mapPoints(points)
        }

        val textInfo = TextInfo(
            content,
            points[0],
            points[1],
            textColor,
            textSize,
            parentView.rotation,
            mEditTextMaxWidth
        )

        textInfo.updateScale(parentScale)

        updateAreaInfo(textInfo)

        mTextInfoList.add(textInfo)

        invalidate()

    }

    /**
     * 父View缩放值
     *
     * @param scale float 缩放倍数
     */
    fun setParentScale(scale: Float) {
        parentScale = scale

        mTextInfoList.forEach {
            it.updateScale(parentScale)
        }

        invalidate()
    }

    /**
     * 移除文本
     * @param textInfo 文本信息
     */
    fun removeTextInfo(textInfo: TextInfo) {
        mTextInfoList.remove(textInfo)

        invalidate()
    }


    /**
     * 更新文字信息
     * @param content 文字内容
     * @param textColor 文字颜色
     * @param textInfo 文本信息
     */
    fun updateText(content: String, textColor: Int, textInfo: TextInfo) {
        if (TextUtils.isEmpty(content)) {
            mTextInfoList.remove(textInfo)
            return
        }
        textInfo.textColor = textColor
        textInfo.content = content
        textInfo.makeNewLayout(content, mEditTextMaxWidth)
        updateAreaInfo(textInfo)
        invalidate()
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        canvas?.let {
            for (textInfo in mTextInfoList) {
                drawTxtInfo(textInfo, canvas, 1f, false)
            }
        }
    }

    /**
     * 绘制文字信息
     * @param canvas Canvas 画板
     * @param scale Float 画板相对于数据的缩放值
     */
    fun drawAllTextInfo(canvas: Canvas, scale: Float) {
        for (textInfo in mTextInfoList) {
            drawTxtInfo(textInfo, canvas, scale, true)
        }
    }

    private fun drawTxtInfo(
        textInfo: TextInfo,
        canvas: Canvas,
        scale: Float,
        isExternal: Boolean
    ) {
        if (textInfo.mInEdit && !isExternal) {
            canvas.save()
            mMatrix.reset()
            mMatrix.postRotate(textInfo.getTransformDegree(), textInfo.centerX, textInfo.centerY)
            canvas.concat(mMatrix)

            mPaint.style = Paint.Style.FILL
            mPaint.color = mBgColor
            canvas.drawRoundRect(
                textInfo.left,
                textInfo.top,
                textInfo.right,
                textInfo.bottom,
                mBgRound,
                mBgRound,
                mPaint
            )

            mPaint.alpha = 255

            textInfo.updateDeleteBtnRect(
                mRect,
                mCloseBitmap.width / parentScale,
                mCloseBitmap.height / parentScale
            )
            canvas.drawBitmap(mCloseBitmap, null, mRect, mPaint)

            textInfo.updateDragBtnRect(
                mRect,
                mDragBitmap.width / parentScale,
                mDragBitmap.height / parentScale
            )
            canvas.drawBitmap(mDragBitmap, null, mRect, mPaint)

            canvas.restore()
        }

        canvas.save()

        textInfo.layout.let { layout ->

            mMatrix.reset()
            mMatrix.preTranslate(
                textInfo.centerX - layout.width / 2,
                textInfo.centerY - layout.height / 2
            )
            mMatrix.preScale(textInfo.scale, textInfo.scale, layout.width / 2f, layout.height / 2f)
            mMatrix.postRotate(textInfo.getTransformDegree(), textInfo.centerX, textInfo.centerY)
            mMatrix.postScale(scale, scale)

            canvas.concat(mMatrix)

            layout.draw(canvas)
        }


        canvas.restore()
    }

    /**
     * 更新文本4个边点的坐标
     */
    private fun updateAreaInfo(textInfo: TextInfo) {
        textInfo.updateWidthAndHeight(mPaddingV, mPaddingH)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (!isCanTouch) {
            return false
        }

        var handle = mTouchTextInfo != null

        event?.let { event ->
            when (event.action and event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    if (mTouchTextInfo == null) {
                        for (textInfo in mTextInfoList.reversed()) {//倒序遍历
                            if (textInfo.isInContentArea(event)) {
                                mTouchTextInfo = textInfo
                                if (!textInfo.mInEdit) {
                                    textInfo.mInEdit = true
                                } else {
                                    textInfo.mBeforeIsEdit = true
                                }
                                if (textInfo.mDragEdit) {
                                    textInfo.mTouchDownLength = sqrtAfterPow(
                                        textInfo.width() / 2.0,
                                        textInfo.height() / 2.0
                                    )
                                    textInfo.mTouchDownScale = textInfo.scale
                                }
                                mCurrentX = event.x
                                mCurrentY = event.y
                                handle = true
                                resetOtherTouchInfo()
                                break
                            }
                        }
                        if (mTouchTextInfo == null) {
                            for (textInfo in mTextInfoList) {
                                textInfo.mInEdit = false
                                textInfo.mBeforeIsEdit = false
                            }
                        } else {
                        }
                    } else {
                    }
                    invalidate()
                }
                MotionEvent.ACTION_MOVE -> {
                    mTouchTextInfo?.let { textInfo ->
                        if (textInfo.mDeleteEdit) {
                            handleDelete(event, textInfo)
                        } else if (textInfo.mDragEdit) { // 拖拽缩放
                            handleDrag(event, textInfo)
                        } else if (textInfo.mContentEdit) { // 拖拽移动
                            handleMove(event, textInfo)
                        }
                    }
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    handleTouchUp()
                }
                else -> {

                }
            }
        }


        return handle
    }

    private fun resetOtherTouchInfo(){
        for (textInfo in mTextInfoList){
            if(textInfo != mTouchTextInfo){
                textInfo.mInEdit = false
                textInfo.mBeforeIsEdit = false
                textInfo.mDragEdit = false
                textInfo.mDeleteEdit = false
                textInfo.mContentEdit = false
            }
        }
    }

    private fun handleMove(event: MotionEvent, textInfo: TextInfo) {
        // 控制边界
        if (event.y < 0 || event.x < 0 || event.x > width || event.y > height) {
            return
        }

        val diffX = event.x - mCurrentX
        val diffY = event.y - mCurrentY

        Log.v(javaClass.name, "event.x:${event.x},event.y:${event.y}")
        Log.v(javaClass.name, "diffX:${diffX},diffY:${diffY}")

        if (textInfo.mMoveEdit || abs(diffX) >= mTouchSlot || abs(diffY) >= mTouchSlot) {
            textInfo.mMoveEdit = true

            textInfo.centerX += diffX
            textInfo.centerY += diffY

            mCurrentX = event.x
            mCurrentY = event.y

            updateAreaInfo(textInfo)

            invalidate()
        }
    }

    private fun handleDrag(event: MotionEvent, textInfo: TextInfo) {
        var diffX = event.x - mCurrentX
        var diffY = event.y - mCurrentY

        if (textInfo.mDraging || abs(diffX) >= mTouchSlot || abs(diffY) >= mTouchSlot) {

            textInfo.mDraging = true

            run {
                var xLength = mCurrentX - textInfo.centerX
                var yLength = mCurrentY - textInfo.centerY
                var afterDegree = Math.toDegrees(Math.atan((yLength / xLength).toDouble()))
                var beforeDegree =
                    Math.toDegrees(Math.atan((textInfo.height() / textInfo.width()).toDouble()))
                if (xLength < 0 && yLength > 0) {
                    afterDegree += 180
                }
                if (xLength < 0 && yLength < 0) {
                    afterDegree += 180
                }
                //计算角度
                textInfo.degree = (afterDegree - beforeDegree + textInfo.initDegree).toFloat()

                var afterLength = sqrtAfterPow(abs(xLength).toDouble(), abs(yLength).toDouble())

                //计算缩放倍数
                textInfo.scale =
                    (textInfo.mTouchDownScale * afterLength / textInfo.mTouchDownLength).toFloat()

                mCurrentX = event.x
                mCurrentY = event.y

                updateAreaInfo(textInfo)

                invalidate()
            }

        }
    }

    /**
     * 平方后再开方
     */
    private fun sqrtAfterPow(width: Double, height: Double): Double {
        return (width.pow(2.0) + height.pow(2.0)).pow(1 / 2.0)
    }

    private fun handleDelete(event: MotionEvent, textInfo: TextInfo) {
        if (!textInfo.isInDeleteArea(event)) {//移出删除按钮区域
            textInfo.mDeleteEdit = false
        }
    }

    private fun handleTouchUp() {
        mTouchTextInfo?.let {
            if (it.mDeleteEdit) {
                mListener?.onDelete(it)
            }
            if (it.mBeforeIsEdit && it.mContentEdit && !it.mMoveEdit && !it.mDeleteEdit && !it.mDragEdit) {
                mListener?.onTabEdit(it)
                it.mInEdit = true
                it.mBeforeIsEdit = false
            }

            it.mMoveEdit = false
            it.mDragEdit = false
            it.mDeleteEdit = false
            it.mDraging = false
        }

        mCurrentX = 0f
        mCurrentY = 0f
        mTouchTextInfo = null

        invalidate()
    }

    class TextInfo {
        var content: String
        var centerX: Float = 0f
        var centerY: Float = 0f
        var initDegree: Float = 0f
        var degree: Float = 0f
        var textSize: Float = 16f
        var scale = 1f
        var textColor: Int = Color.WHITE
        var mInEdit = true//编辑模式
        var mBeforeIsEdit = false//记录之前的编辑模式
        var mMoveEdit = false//移动模式
        var mDragEdit = false//拖动模式
        var mDraging = false//处于拖动状态
        var mDeleteEdit = false//删除
        var mContentEdit = false//在操作区域内

        var mTouchDownLength = 0.0
        var mTouchDownScale = 1f

        //绘制文本4个点区域
        var left = 0f
        var right = 0f
        var top = 0f
        var bottom = 0f

        var adjustOrinSize = ScreenUtils.dpToPx(4f)//按钮位置偏移
        var adjustSize = ScreenUtils.dpToPx(4f)
        var btnEffectRadius = ScreenUtils.dpToPx(12f)//按钮区域周边扩充响应

        var matrix = Matrix()
        lateinit var layout: StaticLayout//静态文本layout，里面有做换行的处理，所以使用这个

        constructor(
            content: String,
            centerX: Float,
            centerY: Float,
            textColor: Int,
            textSize: Float,
            initDegree: Float,
            maxWidth: Float

        ) {

            this.content = content
            this.centerX = centerX
            this.centerY = centerY
            this.textSize = textSize
            this.textColor = textColor
            this.initDegree = initDegree

            makeNewLayout(content, maxWidth)
        }

        fun makeNewLayout(content: String, maxWidth: Float) {
            var textPaint = TextPaint(TextPaint.ANTI_ALIAS_FLAG)
            textPaint.color = textColor
            textPaint.style = Paint.Style.FILL
            textPaint.textSize = textSize

            var wantWidth = -1f
            var desireWidth = maxWidth//想要的最大文本宽度

            if (content.contains("\n")) {//存在换行
                var lines = content.split("\n")
                for (line in lines) {//计算每一行的宽度，获取一个最大值
                    var lineWidth = textPaint.measureText(line)
                    if (wantWidth < lineWidth) {
                        wantWidth = lineWidth
                    }
                }
            } else {
                wantWidth = textPaint.measureText(content)
            }
            if (wantWidth > 0 && wantWidth < maxWidth) {
                desireWidth = wantWidth
            }

            layout = StaticLayout(
                content,
                textPaint,
                desireWidth.toInt(),
                Layout.Alignment.ALIGN_NORMAL,
                1.0f,
                0.0f,
                false
            )
        }

        fun updateScale(scale: Float) {//更新父控件的放大倍数，为了让点击按钮不随着缩放倍数变化
            adjustSize = adjustOrinSize / scale
        }

        fun updateWidthAndHeight(paddingV: Float, paddingH: Float) {
            var height = layout.height * scale + 2 * paddingV
            var width = layout.width * scale + 2 * paddingH

            left = centerX - width / 2
            right = centerX + width / 2
            top = centerY - height / 2
            bottom = centerY + height / 2

        }

        /**
         * 获取转换后的角度，需要减去父控件初始化的旋转角度
         */
        fun getTransformDegree(): Float {
            return degree - initDegree
        }

        /**
         * 是否在内容区域内
         */
        fun isInContentArea(event: MotionEvent): Boolean {
            var x = event.x
            var y = event.y
            if (getTransformDegree() != 0f) {
                var transformEvent = MotionEvent.obtain(event)
                matrix.reset()
                matrix.setRotate(-getTransformDegree(), centerX, centerY)
                transformEvent.transform(matrix)

                x = transformEvent.x
                y = transformEvent.y

                transformEvent.recycle()
            }
            var deleteCenterX = left - adjustSize
            var deleteCenterY = top - adjustSize

            if (x >= deleteCenterX - btnEffectRadius && x <= deleteCenterX + btnEffectRadius && y >= deleteCenterY - btnEffectRadius && y <= deleteCenterY + btnEffectRadius) {
                //点到删除按钮区域
                mDeleteEdit = true
                return true
            }

            var dragCenterX = right + adjustSize
            var dragCenterY = bottom + adjustSize

            if (x >= dragCenterX - btnEffectRadius && x <= dragCenterX + btnEffectRadius && y >= dragCenterY - btnEffectRadius && y <= dragCenterY + btnEffectRadius) {
                //点到拖动按钮区域
                mDragEdit = true
                return true
            }

            if (x >= left && x <= right && y >= top && y <= bottom) {
                mContentEdit = true
                return true
            }

            return false
        }

        fun updateDeleteBtnRect(rect: RectF, bitmapWidth: Float, bitmapHeight: Float) {
            var centerX = left - adjustSize
            var centerY = top - adjustSize
            rect.set(
                centerX - bitmapWidth / 2,
                centerY - bitmapHeight / 2,
                centerX + bitmapWidth / 2,
                centerY + bitmapHeight / 2
            )
        }

        fun updateDragBtnRect(rect: RectF, bitmapWidth: Float, bitmapHeight: Float) {
            var centerX = right + adjustSize
            var centerY = bottom + adjustSize
            rect.set(
                centerX - bitmapWidth / 2,
                centerY - bitmapHeight / 2,
                centerX + bitmapWidth / 2,
                centerY + bitmapHeight / 2
            )
        }

        fun isInDeleteArea(event: MotionEvent): Boolean {
            var x = event.x
            var y = event.y
            if (getTransformDegree() != 0f) {
                var transformEvent = MotionEvent.obtain(event)
                matrix.reset()
                matrix.setRotate(-getTransformDegree(), centerX, centerY)
                transformEvent.transform(matrix)

                x = transformEvent.x
                y = transformEvent.y

                transformEvent.recycle()
            }
            var deleteCenterX = left - adjustSize
            var deleteCenterY = top - adjustSize

            if (x >= deleteCenterX - btnEffectRadius && x <= deleteCenterX + btnEffectRadius && y >= deleteCenterY - btnEffectRadius && y <= deleteCenterY + btnEffectRadius) {
                return true
            }
            return false
        }

        fun width(): Float {
            return right - left
        }

        fun height(): Float {
            return bottom - top
        }

    }

    /**
     * 是否响应触摸事件
     *
     * @param isCanTouch true 可以编辑
     */
    fun setIsCanTouch(isCanTouch: Boolean) {
        this.isCanTouch = isCanTouch
        if (!isCanTouch) {
            mTextInfoList.forEach {
                it.mInEdit = false
            }
            invalidate()
        }
    }

    fun setEditListener(listener: IEditListener) {
        mListener = listener
    }

    interface IEditListener {
        fun onDelete(textInfo: TextInfo)//文本删除
        fun onTabEdit(textInfo: TextInfo)//文本编辑
    }
}