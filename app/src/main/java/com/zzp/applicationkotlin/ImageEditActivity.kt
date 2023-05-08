package com.zzp.applicationkotlin

import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.RectF
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.zzp.applicationkotlin.view.ImageClipLayer
import com.zzp.applicationkotlin.view.ScaleLayoutLayer
import com.zzp.applicationkotlin.view.TextEditLayer
import com.zzp.applicationkotlin.view.doll.dp
import kotlinx.android.synthetic.main.activity_image_edit.*

class ImageEditActivity : AppCompatActivity() , View.OnClickListener{

    private var mRect = RectF()
    private var mMatrix = Matrix()

    private val TAG = "_ImageEditActivity"

    private var mDiff = 0

    private var mCurrentClipInfo = RectF()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_edit)

        root_main.viewTreeObserver.addOnGlobalLayoutListener {
            var rect = Rect()
            root_main.getWindowVisibleDisplayFrame(rect)

            Log.d(TAG,"mRect:${mRect.height()}")

            if(mDiff < rect.height()){
                mDiff = rect.height()
            }

            /*if(mDiff > mRect.height()){
                input_pan.layoutParams?.let {
                    it.height = mDiff - mRect.height()
                    input_pan.requestLayout()
                }
            }*/

        }

        btn_sure.setOnClickListener(this)
        btn_add.setOnClickListener(this)
        btn_change.setOnClickListener(this)
        btn_rotate.setOnClickListener(this)
        btn_cut_finish.setOnClickListener(this)

        input_text_layout.mListener = object :TextEditLayer.IEditListener{
            override fun onDelete(textInfo: TextEditLayer.TextInfo) {
                input_text_layout.removeTextInfo(textInfo)
            }

            override fun onTabEdit(textInfo: TextEditLayer.TextInfo) {
                editText.visibility = View.VISIBLE
                editText.text = SpannableStringBuilder(textInfo.content)
            }
        }

        clip_layer.setClipListener(object :ImageClipLayer.OnClipListener{
            override fun onClipChange(clipRect: RectF,actionUp:Boolean) {
                clipRect.offset(0f,-getMarginTop())
                scaleLayoutLayer.setForceEdge(clipRect.left,clipRect.top,clipRect.right,clipRect.bottom)
            }

            override fun onClipDown() {
            }

            override fun onClipUp() {
            }

        })

        scaleLayoutLayer.setScaleChangeListener(object:
            ScaleLayoutLayer.ChangeListener {
            override fun onScaleChange(scale: Float) {
            }

            override fun onBoundChange(rectF: RectF) {
                rectF.offset(0f,getMarginTop())
                clip_layer.setClipBorder(rectF.left,rectF.top,rectF.right,rectF.bottom)
            }

            override fun onTranslateChange(diffX: Float, diffY: Float) {
            }

            override fun onMotionEvent(event: MotionEvent?) {
            }

        })

        btn_cut.setOnClickListener(this)
        btn_center.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            btn_sure->{
                input_text_layout.setIsCanTouch(true)
                input_text_layout.addText(editText.text.toString(),16f.dp, Color.YELLOW)
                editText.visibility = View.INVISIBLE
            }
            btn_add->{
                editText.setText("", TextView.BufferType.EDITABLE)
                editText.visibility = View.VISIBLE
            }
            btn_change->{
                input_text_layout.updateText(editText.text.append("result").toString(),Color.RED,input_text_layout.mTextInfoList.get(0))
            }
            btn_cut->{
                mRect.set(0f,0f,scaleLayoutLayer.width.toFloat(),scaleLayoutLayer.height.toFloat())
                scaleLayoutLayer.matrix.mapRect(mRect)
                mRect.offset(0f,getMarginTop())
                clip_layer.initClipInfo(mRect.left,mRect.top,mRect.right,mRect.bottom)
                clip_layer.setIsCanTouch(true)
                //scaleLayoutLayer.setClipInfo(RectF(20f,0f,300f,400f))
            }

            btn_center->{
                scaleLayoutLayer.setClipFillView(false)
                /*var clipInfo = clip_layer.getClipBoundInfo()
                var widthScale = scaleLayoutLayer.width/clipInfo.width()
                var heightScale = scaleLayoutLayer.height/clipInfo.height()
                var currentScale = scaleLayoutLayer.scaleX
                var targetScale =  1f
                var left = 0f
                var right = 0f
                var top = 0f
                var bottom = 0f


                if(widthScale <= heightScale){
                    left = 0f
                    right = scaleLayoutLayer.width * 1f
                    top = (scaleLayoutLayer.height - clipInfo.height() * widthScale)/2
                    bottom = (scaleLayoutLayer.height + clipInfo.height() * widthScale)/2
                    targetScale = widthScale * currentScale
                }else{
                    left = (scaleLayoutLayer.width - clipInfo.width() * heightScale)/2
                    right = (scaleLayoutLayer.width + clipInfo.width() * heightScale)/2
                    top = 0f
                    bottom =  scaleLayoutLayer.height * 1f
                    targetScale = heightScale  * currentScale
                }

                var outputRect =  getOutputRectF()
                var targetRect = RectF(left,top,right,bottom)

                scaleLayoutLayer.setPivot(outputRect.centerX(),outputRect.centerY())
                clip_layer.animateToClipRect(left,top,right,bottom,300)
                scaleLayoutLayer.doAnimate2(targetScale,clipInfo,targetRect,outputRect,300,null)*/
            }
            btn_rotate->{
                /*Log.d("zzp123","1 translationX:${scaleLayoutLayer.translationX} translationY:${scaleLayoutLayer.translationY}")
                scaleLayoutLayer.setPivot(scaleLayoutLayer.width/2f,scaleLayoutLayer.height/2f)
                scaleLayoutLayer.rotation = 90f
                scaleLayoutLayer.post {
                    Log.d("zzp123","2 translationX:${scaleLayoutLayer.translationX} translationY:${scaleLayoutLayer.translationY}")
                }*/

                var width = scaleLayoutLayer.width
                var height = scaleLayoutLayerParent.height

                var clipInfo = clip_layer.getClipBoundInfo()
                var targetScale =  Math.min(width/clipInfo.height(),height/clipInfo.width())

                var outputRect = getOutputRectF()


                scaleLayoutLayer.doRotate(outputRect)

                clip_layer.resetClipByCenter(scaleLayoutLayer.width/2f,scaleLayoutLayer.height/2f+ getMarginTop(),clipInfo.height() * targetScale,clipInfo.width()* targetScale)

            }
            btn_cut_finish->{
                scaleLayoutLayer.clearForceEdge()
                scaleLayoutLayer.setClipInfo(getOutputRectF())
                scaleLayoutLayer.setClipFillView(false)
                clip_layer.setIsCanTouch(false)
            }
        }
    }

    fun getMarginTop():Float{
        return (scaleLayoutLayerParent.height - 300f.dp)/2
    }

    private fun getPriovt(a1:Float,a2:Float ,b1:Float,b2:Float):Float{
        if(a1 == b1 && a2 == b2){
            return (b2 - b1)/2
        }else if(a1 == b1){
            return a1
        }else if(a2 == b2){
            return a2
        }
        var a = a1 * b2 - a2 * b1
        var b = a1 + b2 -a2 - b1
        Log.d("zzp123","a1:$a1 a2:$a2 b1:$b1 b2:$b2 a:$a b:$b")
        if(b == 0f){
            return 0f
        }else{
            return a/b
        }
    }

    private fun getOutputRectF(): RectF {
        val scaleViewHeight: Int = scaleLayoutLayer.getHeight()
        val scaleViewWidth: Int = scaleLayoutLayer.getWidth()
        val clipInfo: RectF = clip_layer.getClipBoundInfo()
        clipInfo.offset(0f,-getMarginTop())
        mRect.set(0f, 0f, scaleViewWidth.toFloat(), scaleViewHeight.toFloat())
        scaleLayoutLayer.getMatrix().mapRect(mRect)
        if (clipInfo.bottom > mRect.bottom) {
            clipInfo.bottom = mRect.bottom
        }
        if (clipInfo.top < mRect.top) {
            clipInfo.top = mRect.top
        }
        if (clipInfo.left < mRect.left) {
            clipInfo.left = mRect.left
        }
        if (clipInfo.right > mRect.right) {
            clipInfo.right = mRect.right
        }
        val rotation: Float = scaleLayoutLayer.getRotation()
        val outputRectF = RectF()
        if (rotation % 360 == 0f) {
            val left = (clipInfo.left - mRect.left) * scaleViewWidth / mRect.width()
            val top = (clipInfo.top - mRect.top) * scaleViewHeight / mRect.height()
            val right = (clipInfo.right - mRect.left) * scaleViewWidth / mRect.width()
            val bottom = (clipInfo.bottom - mRect.top) * scaleViewHeight / mRect.height()
            outputRectF.set(left,top,right,bottom)
        } else if (rotation % 360 == 90f) {
            val top = (mRect.right - clipInfo.right) * scaleViewHeight / mRect.width()
            val left = (clipInfo.top - mRect.top) * scaleViewWidth / mRect.height()
            val bottom = (mRect.right - clipInfo.left) * scaleViewHeight / mRect.width()
            val right = (clipInfo.bottom - mRect.top) * scaleViewWidth / mRect.height()
            outputRectF.set(left,top,right,bottom)
        } else if (rotation % 360 == 180f) {
            val top = (mRect.bottom - clipInfo.bottom) * scaleViewHeight / mRect.height()
            val left = (mRect.right - clipInfo.right) * scaleViewWidth / mRect.width()
            val right = (mRect.right - clipInfo.left) * scaleViewWidth / mRect.width()
            val bottom = (mRect.bottom - clipInfo.top) * scaleViewHeight / mRect.height()
            outputRectF.set(left,top,right,bottom)
        } else if (rotation % 360 == 270f) {
            val top = (clipInfo.left - mRect.left) * scaleViewHeight / mRect.width()
            val left = (mRect.bottom - clipInfo.bottom) * scaleViewWidth / mRect.height()
            val bottom = (clipInfo.right - mRect.left) * scaleViewHeight / mRect.width()
            val right = (mRect.bottom - clipInfo.top) * scaleViewWidth / mRect.height()
            outputRectF.set(left,top,right,bottom)
        }
        return outputRectF
    }
}