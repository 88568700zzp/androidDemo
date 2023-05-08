package com.zzp.applicationkotlin.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

class PdfWaterView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var mWaterViewDelegate: WaterViewDelegate = WaterViewDelegate()


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mWaterViewDelegate.draw(canvas, width, height)
    }

    class WaterViewDelegate{

        private var paint: TextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)

        private var alpha: Int = 100
        private var color: Int  = Color.BLUE
        private var textSize: Float  = ScreenUtils.dpToPx(27f)
        private var degree = -30f //倾斜角度
        private var text = "严禁fdaf对传"

        private var matrix = Matrix()

        private var mContentWidth = 0f
        private var mContentHeight = 0f
        private var mCenterAdjust = 0f

        private var mHSpace = ScreenUtils.dpToPx(30f) //水平间距
        private var mVSpace = ScreenUtils.dpToPx(20f) //竖直间距

        init {
            paint.textAlign = Paint.Align.LEFT
            paint.color = color
            paint.textSize = textSize
            paint.alpha = alpha

            val fontMetrics = paint.fontMetrics

            mContentWidth = paint.measureText(text)
            mContentHeight = fontMetrics.bottom - fontMetrics.top
            mCenterAdjust = -fontMetrics.top
        }

        fun draw(canvas: Canvas, width: Int, height: Int) {

            canvas.save()

            var cDegree = abs(degree.toDouble())

            var desireWidth = (width/cos(Math.toRadians(cDegree)) + height/sin(Math.toRadians(cDegree))).toFloat()
            var desireHeight= (height/cos(Math.toRadians(cDegree)) + width/sin(Math.toRadians(cDegree))).toFloat()


            var top = height/2 - desireHeight/2
            var left = width/2 - desireWidth/2
            var bottom = height/2 + desireHeight/2
            var right = width/2 + desireWidth/2

            //Log.d("zzp1234","desireWitdh:$desireWidth desireHeight:$desireHeight width:$width height:$height")
            //Log.d("zzp1234","top:$top left:$left bottom:$bottom right:$right")


            matrix.reset()
            matrix.preTranslate(-width/2f,-height/2f)
            matrix.postRotate(degree)
            matrix.postTranslate(width/2f,height/2f)

            canvas.concat(matrix)

            var flag = true
            var currentX = left
            var currentY = top

            while (currentY < bottom) {
                currentX = if (flag) {
                    left
                } else {
                    left + mContentWidth * 2 / 3
                }
                flag = !flag
                while (currentX < right) {
                    canvas.drawText(text, currentX, currentY + mCenterAdjust, paint)
                    currentX += mContentWidth + mHSpace
                }
                currentY += mContentHeight + mVSpace
            }

            canvas.restore()
        }


    }

}