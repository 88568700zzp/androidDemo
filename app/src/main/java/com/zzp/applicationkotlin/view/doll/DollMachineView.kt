package com.zzp.applicationkotlin.view.doll

import android.content.Context
import android.graphics.*
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Toast
import com.zzp.applicationkotlin.R
import java.util.*
import kotlin.collections.HashSet

/**
 * 娃娃机
 * Created by samzhang on 2021/3/25.
 */
class DollMachineView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val TAG = "DollMachineView"

    private val RACE_SIZE =4//4条赛道

    private var mPauseTime = 0L//暂停时间
    private var mCreateDollTime = 0L//创建娃娃时间

    var isAnimating = false

    private var mDollList = LinkedList<DollView>()
    private var mPaint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var mStartXArray = floatArrayOf(0f,0f,0f,0f,0f)
    private var mEndXArray = floatArrayOf(0f,0f,0f,0f,0f)
    private var mHitXArray = floatArrayOf(0f,0f,0f,0f,0f)

    private var mRandom = Random()
    private var mHashSet = HashSet<Int>(RACE_SIZE)

    private var mDollStartY = 0F //娃娃开始坐标
    private var mDollEndY = 0F //娃娃结束坐标

    private var mStartHitY = 0F //抓娃娃命中区域
    private var mEndHitY = 0F

    private var mDollClampView:DollClampView = DollClampView()

    private var mDollBitmap1: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.enter_wifi_check)
    private var mDollBitmap2: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.enter_wifi_force)
    private var mDollBitmap3: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.enter_wifi_safe)

    private var mBitmapArray = arrayListOf(mDollBitmap1,mDollBitmap2,mDollBitmap3)

    private var mHandler = Handler(Looper.getMainLooper())

    companion object{
        const val DOLL_DURATION = 7000L//娃娃下落时长
        const val DOLL_CLAMP_DURATION = 2500L//娃娃夹子横向时间
        const val CREATE_DOLL_DURATION = 2500L//创建娃娃机间隔时间
        const val DOLL_CLAMP_ANIMATE_DURATION = 600L//娃娃机动画时间

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        drawLine(canvas)
        animateDoll(canvas)

        if(isAnimating){
            invalidate()
        }
    }

    private fun drawLine(canvas: Canvas?){
        mPaint.color = Color.RED

        for(index in 0..RACE_SIZE){
            canvas?.drawLine(mStartXArray[index],mDollStartY,mEndXArray[index],mDollEndY,mPaint)
            //canvas?.drawCircle(mHitXArray[index],mEndHitY,10f,mPaint)
        }
        canvas?.drawLine(0f,height.toFloat(),width.toFloat(),height.toFloat(),mPaint)
        canvas?.drawLine(0f,mStartHitY,width.toFloat(),mStartHitY,mPaint)
        canvas?.drawLine(0f,mEndHitY,width.toFloat(),mEndHitY,mPaint)

    }

    fun beginDollMachine(){
        isAnimating = true

        for(index in 0..3){
            var dollView = getDollView(index)
            dollView.mBitmap = mBitmapArray[mRandom.nextInt(3)]
            mDollList.add(dollView)
        }
        mCreateDollTime = System.currentTimeMillis()
        doCreateDollRunnable(CREATE_DOLL_DURATION)

        postInvalidate()
    }

    private fun doCreateDollRunnable(delayDuration: Long){
        mHandler.removeCallbacks(mCreateDollRunnbale)
        mHandler.postDelayed(mCreateDollRunnbale,delayDuration)
    }

    /**
     *@description 抓取娃娃
    **/
    fun doHitDoll(){
        if(mDollClampView.isClampAnimating || mDollList.size == 0){
            return
        }
        var hitIndex = -1;
        for(index in mHitXArray.indices){
            if(mHitXArray[index] <= mDollClampView.getCurrentX() && mDollClampView.getCurrentX() <= mHitXArray[index + 1]){
                hitIndex = index
                break
            }
        }

        var hitDollView:DollView ?= null
        if(hitIndex in 0 until RACE_SIZE){
            Log.i(TAG,"hitIndex:${hitIndex}")
            for(data in mDollList){
                if(data.race_index == hitIndex && data.hitArea(mStartHitY,mEndHitY)){//抓中娃娃
                    hitDollView = data
                    break
                }
            }
        }
        if(hitDollView == null) {
            mDollClampView.doHitAnimate(false,(mEndHitY - mStartHitY)/2)
        }else{
            //pauseDoll()
            mDollClampView.doHitAnimate(true,hitDollView.mCurrentY - mStartHitY)
        }
    }

    private fun getDollView(index:Int):DollView{
        var startX = mStartXArray[index] + (mStartXArray[index + 1] - mStartXArray[index])/2
        var endX = mEndXArray[index] + (mEndXArray[index + 1] - mEndXArray[index])/2

        return DollView(startX, mDollStartY,endX, mDollEndY,index)
    }

    fun stopDollMachine(){
        isAnimating = false
        mHandler.removeCallbacks(mCreateDollRunnbale)
        mDollList.clear()
    }

    /**
     *@description 暂停播放
    **/
    fun pauseDoll(){
        if(!isAnimating){
            return
        }
        isAnimating = false
        mHandler.removeCallbacks(mCreateDollRunnbale)
        mPauseTime = System.currentTimeMillis()
    }

    /**
     *@description 继续播放
    **/
    fun resumeDoll(){
        if(isAnimating){
            return
        }
        isAnimating = true
        var delayTime = System.currentTimeMillis() - mPauseTime
        doCreateDollRunnable(CREATE_DOLL_DURATION - (System.currentTimeMillis() - (mCreateDollTime + delayTime)))
        for(data in mDollList){
            data.resume(delayTime)
        }
        mDollClampView.resume(delayTime)
        postInvalidate()
    }

    private fun animateDoll(canvas: Canvas?){
        mDollList?.takeIf { it.size > 0 }.let {
            var iterator = mDollList.iterator()
            while (iterator.hasNext()){
                var doll = iterator.next()
                var result = doll.calculate(canvas,mPaint)
                if(!result){
                    iterator.remove()
                }
            }
        }

        mDollClampView.let {
            mPaint.color = Color.BLUE
            it.calculate(canvas,mPaint)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopDollMachine()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        initPosition()

        beginDollMachine()
    }

    private fun initPosition(){
        var startSideX = width/5
        var startSpace = (width - 2 * startSideX)/4

        var endSideX = -width/20
        var endSpace = (width - 2 * endSideX)/4

        //todo 图片高度的一半
        mDollStartY = -20 * 2.75f
        mDollEndY = 20 * 2.75f + height

        mStartHitY = height /2f
        mEndHitY = height /2f + height /7f

        for(i in 0..RACE_SIZE){
            mStartXArray[i] = (startSideX + startSpace * i).toFloat()
            mEndXArray[i] = (endSideX + endSpace * i).toFloat()

            mHitXArray[i] = mStartXArray[i] - mEndHitY * ((mStartXArray[i] - mEndXArray[i])/height)
        }

        //夹子位置
        mDollClampView.init(mHitXArray[0],mHitXArray[RACE_SIZE], mStartHitY - mDollClampView.drawHeight)
    }

    private var mCreateDollRunnbale = object:Runnable {

        override fun run() {
            if(isAnimating){
                var size = mRandom.nextInt(RACE_SIZE) + 1
                mHashSet.clear()
                for(index in 0 until size){
                    var notFind = false
                    while(!notFind){
                        var raceIndex = mRandom.nextInt(RACE_SIZE)
                        notFind = !mHashSet.contains(raceIndex)
                        if(notFind){
                            var dollView = getDollView(raceIndex)
                            dollView.mBitmap = mBitmapArray[mRandom.nextInt(3)]
                            mDollList.add(dollView)
                            mHashSet.add(raceIndex)
                        }
                    }
                }
                mCreateDollTime = System.currentTimeMillis()
                mHandler.postDelayed(Runnable@this,CREATE_DOLL_DURATION)
            }
        }
    }
}