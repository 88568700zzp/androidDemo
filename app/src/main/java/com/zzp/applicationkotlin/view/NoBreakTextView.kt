package com.zzp.applicationkotlin.view

import android.content.Context
import android.graphics.Canvas
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.TextView

/**
 * 不折行textView
 */
class NoBreakTextView : TextView {

    private var mTextWidths:HashMap<Int,Float> = HashMap()

    constructor(context: Context?, attrs: AttributeSet?) :  super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    override fun onDraw(canvas: Canvas?) {

        if(layout == null || layout.lineCount == 1 || TextUtils.isEmpty(text)){
            super.onDraw(canvas)
            return
        }

        if(layout.getLineWidth(0) > width * 3/4){
            super.onDraw(canvas)
            return
        }

        var paint = paint
        val fm = paint.fontMetrics
        var lineIndex = 0
        var totalLineWidth = 0f
        var startLineIndex = 0
        var endLineIndex = 0

        var mStartLineY = paddingTop.toFloat()
        mStartLineY = mStartLineY - fm.ascent

        var textHeight = Math.ceil((fm.descent - fm.ascent).toDouble()).toInt()
        textHeight = (textHeight * layout.spacingMultiplier + layout
            .spacingAdd).toInt()

        for(i in text.indices){
            var txtLength = mTextWidths.get(i)
            if(txtLength == null){
                txtLength = paint.measureText(text,i,i + 1)
                mTextWidths.put(i,txtLength)
            }
            totalLineWidth += txtLength
            if(totalLineWidth > width){
                endLineIndex = i
                var lineContent = text.subSequence(startLineIndex,endLineIndex).toString()
                canvas?.drawText(lineContent,0f,mStartLineY,paint)

                startLineIndex = endLineIndex
                lineIndex++
                totalLineWidth = 0f
                mStartLineY += textHeight
            }

            if(i == text.length - 1){
                endLineIndex = text.length
                var lineContent = text.subSequence(startLineIndex,endLineIndex).toString()
                canvas?.drawText(lineContent,0f,mStartLineY,paint)
            }
        }

    }

    override fun setTextSize(unit: Int, size: Float) {
        mTextWidths?.clear()
        super.setTextSize(unit, size)
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        mTextWidths?.clear()
        super.setText(text, type)
    }
}