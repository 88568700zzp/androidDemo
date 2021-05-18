package com.zzp.applicationkotlin.view.doll

import android.content.Context
import android.graphics.*
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import com.zzp.applicationkotlin.R
import org.greenrobot.eventbus.EventBus
import java.util.*
import kotlin.collections.HashSet

/**
 * 娃娃机
 * Created by samzhang on 2021/3/25.
 */
class DollMachineView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val TAG = "DollMachineView"

    private val RACE_SIZE = 4//4条赛道

    private val MODE_NORMAL = 0
    private val MODE_FAST = 1
    private val MODE_SLOW = 2

    private var mPauseTime = 0L//暂停时间
    private var mCreateDollTime = 0L//创建娃娃时间

    var isAnimating = false

    private var mDollList = LinkedList<DollView>()
    private var mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var mStartXArray = floatArrayOf(0f, 0f, 0f, 0f, 0f)
    private var mEndXArray = floatArrayOf(0f, 0f, 0f, 0f, 0f)
    private var mEndHitXArray = floatArrayOf(0f, 0f, 0f, 0f, 0f) //保存命中的x坐标值
    private var mStartHitXArray = floatArrayOf(0f, 0f, 0f, 0f, 0f) //

    private var mRandom = Random()
    private var mHashSet = HashSet<Int>(RACE_SIZE)

    private var mDollStartY = 0F //娃娃开始坐标
    private var mDollEndY = 0F //娃娃结束坐标

    private var mStartHitY = 0F //抓娃娃命中区域开始
    private var mEndHitY = 0F //抓娃娃命中区域结束

    private var mDollClampView: DollClampView = DollClampView()

    private var mVoiceBitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_doll_sheep_voice)
    private var mDollBitmap1: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_doll_gift_small)
    private var mDollBitmap2: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_doll_gift_mid)
    private var mDollBitmap3: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_doll_gift_big)
    private var mDollBitmap4: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_doll_gift_bomb)

    private var mBitmapArray = arrayListOf(mDollBitmap1, mDollBitmap2, mDollBitmap3, mDollBitmap4)

    private var mPurpleBgBitmapArray = arrayListOf(BitmapFactory.decodeResource(resources, R.drawable.ic_doll_purple_bg_1)
            , BitmapFactory.decodeResource(resources, R.drawable.ic_doll_purple_bg_2)
            , BitmapFactory.decodeResource(resources, R.drawable.ic_doll_purple_bg_3)
            , BitmapFactory.decodeResource(resources, R.drawable.ic_doll_purple_bg_4))

    private var mYellowBgBitmapArray = arrayListOf(BitmapFactory.decodeResource(resources, R.drawable.ic_doll_yellow_bg_1)
            , BitmapFactory.decodeResource(resources, R.drawable.ic_doll_yellow_bg_2)
            , BitmapFactory.decodeResource(resources, R.drawable.ic_doll_yellow_bg_3)
            , BitmapFactory.decodeResource(resources, R.drawable.ic_doll_yellow_bg_4))

    private var mHandler = Handler(Looper.getMainLooper())

    private var mMode = MODE_NORMAL//0是正常，1变快，2变慢
    private var mPlayingMode = MODE_NORMAL

    companion object {
        var mCurrentDollDuration = 7000L//娃娃下落时长
        var mCurrentCreateDollDuration = 1500L//创建娃娃机间隔时间

        var INIT_DOLL_CURATION = 7000L//娃娃下落时长
        var INIT_CREATE_DOLL_DURATION = 1500L//娃娃下落时长

        var INIT_DOLL_DOLL_CLAMP_DURATION = 2500L//娃娃夹子横向时间
        var DOLL_CLAMP_ANIMATE_DURATION = 600L//娃娃机夹子夹娃娃时间


        val MODE_NORMAL = 0
        val MODE_FAST = 1
        val MODE_SLOW = 2

    }

    fun initData(dollDuration: Long, dollClampDuration: Long) {
        mCurrentDollDuration = dollDuration
        mCurrentCreateDollDuration = mCurrentDollDuration / 3

        INIT_DOLL_CURATION = dollDuration
        INIT_CREATE_DOLL_DURATION = dollDuration / 3
        INIT_DOLL_DOLL_CLAMP_DURATION = dollClampDuration
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        //drawLine(canvas)
        animateDoll(canvas)

        if (isAnimating) {
            invalidate()
        }
    }

    private fun drawLine(canvas: Canvas?) {
        mPaint.color = Color.GREEN

        for (index in 0..RACE_SIZE) {
            canvas?.drawLine(mStartXArray[index], mDollStartY, mEndXArray[index], mDollEndY, mPaint)
            //canvas?.drawCircle(mHitXArray[index],mEndHitY,10f,mPaint)
        }
        canvas?.drawLine(0f, height.toFloat(), width.toFloat(), height.toFloat(), mPaint)
        canvas?.drawLine(mStartHitXArray[0], mStartHitY, mStartHitXArray[RACE_SIZE], mStartHitY, mPaint)
        canvas?.drawLine(0f, mEndHitY, width.toFloat(), mEndHitY, mPaint)

    }

    fun beginDollMachine() {
        isAnimating = true

        for (index in 0..3) {
            var dollView = getDollView(index)
            mDollList.add(dollView)
        }
        mCreateDollTime = System.currentTimeMillis()
        doCreateDollRunnable(mCurrentCreateDollDuration)

        postInvalidate()
    }

    private fun doCreateDollRunnable(delayDuration: Long) {
        mHandler.removeCallbacks(mCreateDollRunnbale)
        mHandler.postDelayed(mCreateDollRunnbale, delayDuration)
    }

    /**
     *@description 抓取娃娃
     **/
    fun doHitDoll() {
        if (mDollClampView.isClampAnimating || mDollClampView.isBombing || mDollList.size == 0) {
            return
        }
        var hitIndex = -1
        for (index in 0 until (mEndHitXArray.size - 1)) {
            /*if(mEndHitXArray[index] <= mDollClampView.getLeftX() && mDollClampView.getLeftX() <= mEndHitXArray[index + 1]){
                hitIndexSet.add(index)
            }*/
            if (mStartHitXArray[index] <= mDollClampView.getCurrentX() && mDollClampView.getCurrentX() <= mStartHitXArray[index + 1]) {
                hitIndex = index
            }
            /*if(mEndHitXArray[index] <= mDollClampView.getRightX() && mDollClampView.getRightX() <= mEndHitXArray[index + 1]){
                hitIndexSet.add(index)
            }*/
        }

        var hitDollView: DollView? = null
        for (data in mDollList.reversed()) {
            if (hitIndex == data.race_index && data.hitArea(mStartHitY, mEndHitY)) {//抓中娃娃
                hitDollView = data
                break
            }
        }

        if (hitDollView == null) {
            mDollClampView.hitFailAnimate(mStartHitY + (mEndHitY - mStartHitY) / 2)
        } else {
            //pauseDoll()
            mDollClampView.doHitAnimate(hitDollView)
        }
    }

    private fun getDollView(index: Int): DollView {
        var startX = mStartXArray[index] + (mStartXArray[index + 1] - mStartXArray[index]) / 2
        var endX = mEndXArray[index] + (mEndXArray[index + 1] - mEndXArray[index]) / 2
        var dollView = DollView(startX, mDollStartY, endX, mDollEndY, index)
        var randomIndex = mRandom.nextInt(4)
        dollView.mBitmap = mBitmapArray[randomIndex]
        dollView.mBgBitmap = if (randomIndex == 3) mPurpleBgBitmapArray[index] else mYellowBgBitmapArray[index]
        dollView.mVoiceBitmap = mVoiceBitmap
        return dollView
    }

    fun stopDollMachine() {
        isAnimating = false
        mHandler.removeCallbacks(mCreateDollRunnbale)
        mDollList.clear()
    }

    /**
     *@description 暂停播放
     **/
    fun pauseDoll() {
        if (!isAnimating) {
            return
        }
        isAnimating = false
        mHandler.removeCallbacks(mCreateDollRunnbale)
        mPauseTime = System.currentTimeMillis()
    }

    /**
     *@description 继续播放
     **/
    fun resumeDoll() {
        if (isAnimating) {
            return
        }
        isAnimating = true
        var delayTime = System.currentTimeMillis() - mPauseTime
        doCreateDollRunnable(mCurrentCreateDollDuration - (System.currentTimeMillis() - (mCreateDollTime + delayTime)))
        for (data in mDollList) {
            data.resume(delayTime)
        }
        mDollClampView.resume(delayTime)
        postInvalidate()
    }

    private fun animateDoll(canvas: Canvas?) {
        mDollList?.takeIf { it.size > 0 }.let {
            var iterator = mDollList.iterator()
            while (iterator.hasNext()) {
                var doll = iterator.next()
                var result = doll.onDraw(canvas, mPaint)
                if (!result) {
                    iterator.remove()
                }
            }
        }

        mDollClampView.let {
            mPaint.color = Color.BLUE
            it.onDraw(canvas, mPaint)
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

    private fun initPosition() {
        var startSideX = width * 0.39F
        var startSpace = (width - 2 * startSideX) / 4

        var endSideX = -width * 0.16F
        var endSpace = (width - 2 * endSideX) / 4

        mDollStartY = height * 0.43f
        mDollEndY = 33F.dp + height

        mStartHitY = height * 0.73f
        mEndHitY = mStartHitY + height * 0.19f

        for (i in 0..RACE_SIZE) {
            mStartXArray[i] = (startSideX + startSpace * i)
            mEndXArray[i] = (endSideX + endSpace * i)

            mStartHitXArray[i] = mStartXArray[i] - (mStartHitY - mDollStartY) * ((mStartXArray[i] - mEndXArray[i]) / (height - mDollStartY))
            mEndHitXArray[i] = mStartXArray[i] - (mEndHitY - mDollStartY) * ((mStartXArray[i] - mEndXArray[i]) / (height - mDollStartY))
        }

        //夹子位置
        mDollClampView.init(context, mStartHitXArray[0], mStartHitXArray[RACE_SIZE], mStartHitY - height * 0.24f)
    }

    private var mCreateDollRunnbale = object : Runnable {

        override fun run() {
            if (isAnimating) {
                var size = mRandom.nextInt(RACE_SIZE) + 1
                mHashSet.clear()
                for (index in 0 until size) {
                    var notFind = false
                    while (!notFind) {
                        var raceIndex = mRandom.nextInt(RACE_SIZE)
                        notFind = !mHashSet.contains(raceIndex)
                        if (notFind) {
                            var dollView = getDollView(raceIndex)

                            mDollList.add(dollView)
                            mHashSet.add(raceIndex)
                        }
                    }
                }
                mCreateDollTime = System.currentTimeMillis()
                mHandler.postDelayed(Runnable@ this, mCurrentCreateDollDuration)
            }
        }
    }

    fun changeClampShape(changeBig: Boolean) {
        mDollClampView?.let {
            if (changeBig) {
                it.bigClamp()
            } else {
                it.smallClamp()
            }
            invalidate()
        }
    }

    private var mNormalModeRunnbale = Runnable {
        var toMode = mPlayingMode
        if (mMode == MODE_FAST && toMode != MODE_FAST) {
            //EventBus.getDefault().post(BuffInvalidEvent(GameDoll.Present.BUFF_TYPE_FAST))
        } else if (mMode == MODE_SLOW && toMode != MODE_SLOW) {
            //EventBus.getDefault().post(BuffInvalidEvent(GameDoll.Present.BUFF_TYPE_SLOW))
        }
        mMode = toMode
        setModeEffect()
    }

    /**
     *@description
     *@param changeFast true加速，false 减速
     *@param effectTime 持续时间
     *@return
     **/
    fun setMode(changeFast: Boolean, effectTime: Long) {
        var delayTime = effectTime
        if (delayTime < 1000) {
            delayTime = 1000
        }
        if (changeFast) {
            when (mMode) {
                MODE_NORMAL -> {
                    mMode = MODE_FAST
                    mHandler.removeCallbacks(mNormalModeRunnbale)
                    mHandler.postDelayed(mNormalModeRunnbale, delayTime)
                }
                MODE_SLOW -> {
                    mMode = MODE_NORMAL
                    //EventBus.getDefault().post(BuffInvalidEvent(GameDoll.Present.BUFF_TYPE_SLOW))
                }
                MODE_FAST -> {
                    mHandler.removeCallbacks(mNormalModeRunnbale)
                    mHandler.postDelayed(mNormalModeRunnbale, delayTime)
                }
            }
        } else {
            when (mMode) {
                MODE_NORMAL -> {
                    mMode = MODE_SLOW
                    mHandler.removeCallbacks(mNormalModeRunnbale)
                    mHandler.postDelayed(mNormalModeRunnbale, delayTime)
                }
                MODE_FAST -> {
                    mMode = MODE_NORMAL
                    //EventBus.getDefault().post(BuffInvalidEvent(GameDoll.Present.BUFF_TYPE_FAST))
                }
                MODE_SLOW -> {
                    mHandler.removeCallbacks(mNormalModeRunnbale)
                    mHandler.postDelayed(mNormalModeRunnbale, delayTime)
                }
            }
        }
        setModeEffect()
    }

    private fun changeDollDuration(newTime: Long) {
        if (mCurrentDollDuration == newTime) {
            return
        }
        mDollList?.takeIf { it.size > 0 }.let {
            var iterator = mDollList.iterator()
            while (iterator.hasNext()) {
                var doll = iterator.next()
                var result = doll.changeDollSpeed(newTime, mCurrentDollDuration)
                if (!result) {
                    iterator.remove()
                }
            }
        }
        mCurrentDollDuration = newTime
        invalidate()
    }

    private fun changeCreateDollDuration(newTime: Long) {
        if (newTime == mCurrentCreateDollDuration) {
            return
        }
        var progress =
                1 - (System.currentTimeMillis() - mCreateDollTime) * 1f / mCurrentCreateDollDuration
        doCreateDollRunnable((newTime * progress).toLong())

        mCurrentCreateDollDuration = newTime
    }

    fun bomp(time: Int) {
        mDollClampView.bomp(time)
    }

    fun setPlayingMode(mode: Int) {
        mPlayingMode = mode
        mMode = mPlayingMode
        setModeEffect()
    }

    private fun setModeEffect() {
        var newDollDuration = 0L
        var newCreateDollDuration = 0L
        when (mMode) {
            MODE_NORMAL -> {
                newDollDuration = INIT_DOLL_CURATION
                newCreateDollDuration = INIT_CREATE_DOLL_DURATION
            }
            MODE_FAST -> {
                newDollDuration = INIT_DOLL_CURATION / 2
                newCreateDollDuration = INIT_CREATE_DOLL_DURATION / 2
            }
            MODE_SLOW -> {
                newDollDuration = INIT_DOLL_CURATION * 2
                newCreateDollDuration = INIT_CREATE_DOLL_DURATION * 2
            }
        }
        changeDollDuration(newDollDuration)
        changeCreateDollDuration(newCreateDollDuration)
    }

    fun reset() {
        mHandler.removeCallbacks(mNormalModeRunnbale)
        mMode = MODE_NORMAL
        mPlayingMode = MODE_NORMAL
        setModeEffect()
    }

}