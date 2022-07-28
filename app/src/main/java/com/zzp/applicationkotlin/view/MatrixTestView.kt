package com.zzp.applicationkotlin.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.zzp.applicationkotlin.R

class MatrixTestView : View {

    var mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    var mMatrix = Matrix()

    var mRect = RectF()
    var mDstRect = RectF()

    var mBitmap:Bitmap

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        setBackgroundColor(Color.BLUE)
        mBitmap = BitmapFactory.decodeResource(resources, R.drawable.doll_enter_wifi_force)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        mRect.set(0f,0f, mBitmap.width.toFloat(), mBitmap.height.toFloat())

        mMatrix.reset()
        mMatrix.preTranslate(( - mBitmap.width)/2.toFloat(),( - mBitmap.height)/2.toFloat())
        //mMatrix.postRotate(90f)
        mMatrix.postScale(2f,2f)
        mMatrix.postTranslate(width/2.toFloat(), (height/2).toFloat())
        mMatrix.mapRect(mDstRect,mRect)

        canvas?.let {
            it.drawBitmap(mBitmap,null,mDstRect,mPaint)
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

    }
}