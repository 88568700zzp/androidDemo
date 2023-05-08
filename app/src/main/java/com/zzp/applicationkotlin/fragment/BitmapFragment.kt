package com.zzp.applicationkotlin.fragment

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapRegionDecoder
import android.graphics.Rect
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.zzp.applicationkotlin.R
import com.zzp.applicationkotlin.viewmodel.BitmapViewModel
import kotlinx.android.synthetic.main.fragment_bitmap.*

/**
 *
 * Created by samzhang on 2021/8/9.
 */
class BitmapFragment :Fragment(){

    private var TAG :String = ""
    get(){
        return "BitmapFragmentTAG${this}"
    }

    private var mDecoder: BitmapRegionDecoder? = null

    var index = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("zzpliveData","fragment:${ViewModelProviders.of(requireActivity()).get(BitmapViewModel::class.java)}")
        var bitmapViewModel = ViewModelProviders.of(requireActivity()).get(BitmapViewModel::class.java)
        bitmapViewModel.getValue().observe(viewLifecycleOwner,object:Observer<String>{
            override fun onChanged(t: String?) {
                Log.e("zzpliveData","receive msg ${t}")
            }
        })

        //lifecycle.addObserver(observer)

        Log.i(TAG,"onViewCreated")

        val displayMetrics = resources.displayMetrics
        Log.d(TAG, "displayMetrics:$displayMetrics densityDpi:${displayMetrics.densityDpi}")

        var bitmap1 = BitmapFactory.decodeResource(resources,R.drawable.bitmap_test)
        Log.d(TAG,"bitmap1 size:${getSize(bitmap1)} width:${bitmap1.width} height:${bitmap1.height} scaleWidth:${bitmap1.getScaledWidth(displayMetrics.densityDpi)} scaleHeight:${bitmap1.getScaledWidth(displayMetrics.densityDpi)}")
        bitmap_1.setImageBitmap(bitmap1)

        var option10 = BitmapFactory.Options()
        option10.inSampleSize = 7
        var inBitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().absolutePath + "/doll_enter_wifi_force.png",option10)


        var option11 = BitmapFactory.Options()
        option11.inJustDecodeBounds = true

        BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().absolutePath + "/doll_enter_wifi_force.png",option11)

        option11.inSampleSize = 4
        option11.inJustDecodeBounds = false
        option11.inMutable = true
        option11.inBitmap = inBitmap

        var fileBitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().absolutePath + "/doll_enter_wifi_force.png",option11)


        var option2 = BitmapFactory.Options()
        option2.inTargetDensity = displayMetrics.densityDpi/2
        option2.inPreferredConfig = Bitmap.Config.ARGB_8888
        option2.inSampleSize = 4
        option2.inMutable = true
        option2.inBitmap = bitmap1
        var bitmap2 = BitmapFactory.decodeResource(resources,R.drawable.bitmap_test,option2)
        Log.d(TAG,"bitmap2 size:${getSize(bitmap2)} width:${bitmap2.width} height:${bitmap2.height} scaleWidth:${bitmap2.getScaledWidth(displayMetrics.densityDpi)} scaleHeight:${bitmap2.getScaledWidth(displayMetrics.densityDpi)}")
        bitmap_2.setImageBitmap(fileBitmap)

        var option3 = BitmapFactory.Options()
        option3.inSampleSize = 2
        option3.inMutable = true
        option3.inBitmap = bitmap1
        var bitmap3 = BitmapFactory.decodeResource(resources,R.drawable.bitmap_test,option3)
        Log.d(TAG,"bitmap3 size:${getSize(bitmap3)} width:${bitmap3.width} height:${bitmap3.height} scaleWidth:${bitmap3.getScaledWidth(displayMetrics.densityDpi)} scaleHeight:${bitmap3.getScaledWidth(displayMetrics.densityDpi)}")
        bitmap_3.setImageBitmap(bitmap3)

        var inputStream4 = resources.assets.open("pic/bitmap_test.jpeg")
        var bitmap4 = BitmapFactory.decodeStream(inputStream4)
        Log.d(TAG,"bitmap4 size:${getSize(bitmap4)} width:${bitmap4.width} height:${bitmap4.height} scaleWidth:${bitmap4.getScaledWidth(displayMetrics.densityDpi)} scaleHeight:${bitmap4.getScaledWidth(displayMetrics.densityDpi)}")
        bitmap_4.setImageBitmap(bitmap4)

        var inputStream5 = resources.assets.open("pic/bitmap_test.jpeg")
        var option5 = BitmapFactory.Options()
        mDecoder = BitmapRegionDecoder.newInstance(inputStream5, false)
        var rect = Rect()
        rect.set(0,0,350,350)
        var bitmap5 = mDecoder?.decodeRegion(rect,option5)!!
        Log.d(TAG,"bitmap5 size:${getSize(bitmap5)} width:${bitmap5.width} height:${bitmap5.height} scaleWidth:${bitmap5.getScaledWidth(displayMetrics.densityDpi)} scaleHeight:${bitmap5.getScaledWidth(displayMetrics.densityDpi)}")
        bitmap_5.setImageBitmap(bitmap5)

        Glide.with(this).asBitmap().load("https://gimg2.baidu.com/image_search/src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fimages%2F20171124%2F06cef2639f294072820abef52e75a1c7.png&refer=http%3A%2F%2F5b0988e595225.cdn.sohucs.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1631848916&t=43d13421a71bb14e81f9e9dac9bdecd3").into(object:SimpleTarget<Bitmap>(){
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
            }

        })

        Glide.with(this).asBitmap().priority(Priority.HIGH).load("https://img1.baidu.com/it/u=2119198610,4219415118&fm=15&fmt=auto&gp=0.jpg").into(object:SimpleTarget<Bitmap>(){
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
            }

        })

        /*GlobalScope.launch(Dispatchers.IO) {
            var connection: HttpURLConnection = URL("http://www.baidu.com").openConnection() as HttpURLConnection
            connection.readTimeout = 5000
            connection.requestMethod = "GET"
            connection.connect()
            Log.d(TAG,"current Thread:" + Thread.currentThread())
        }*/

    }

    private fun printlnThread() {
        Thread.getAllStackTraces().forEach{
            Log.d("threadMap","thread:${it.key.name}")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG,"onCreateView")
        return inflater.inflate(R.layout.fragment_bitmap,null)
    }

    private var observer = object : LifecycleEventObserver {
        override fun onStateChanged(
            source: LifecycleOwner,
            event: Lifecycle.Event
        ) {
            Log.e(TAG,"onStateChanged:${event.name}")
        }
    }


    private fun getSize(bitmap:Bitmap):String{
        return (bitmap.allocationByteCount * 1f/1024/1024).toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG,"onCreate")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i(TAG,"onAttach")
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG,"onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG,"onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG,"onPause")
        printlnThread()
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG,"onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG,"onDestroy")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i(TAG,"onDestroyView")

        lifecycle.removeObserver(observer)

        /*Handler(Looper.getMainLooper()).postDelayed(object:Runnable{
            override fun run() {
                Log.i("zzpliveData","observerCount:${(lifecycle as LifecycleRegistry)?.observerCount}}")

            }
        },3000L)*/
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.i(TAG,"onActivityCreated")
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Log.i(TAG,"onHiddenChanged")
    }

    override fun toString(): String {
        if(index > -1){
            return " index:$index"
        }
        return ""
    }
}