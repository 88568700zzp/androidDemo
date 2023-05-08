package com.zzp.applicationkotlin

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.zzp.applicationkotlin.application.AppApplication
import com.zzp.applicationkotlin.fragment.BitmapFragment
import com.zzp.applicationkotlin.viewmodel.BitmapViewModel


/**
 *
 * Created by samzhang on 2021/8/5.
 */
class BitmapActivity : AppCompatActivity() {

    private val TAG = "BitmapActivityTag_"

    private lateinit var mFragmentTransaction: FragmentTransaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG,"onCreate：${savedInstanceState == null}")

        setContentView(R.layout.activity_bitmap)

        /*mFragmentTransaction = supportFragmentManager.beginTransaction()
        mFragmentTransaction.add(R.id.bitmap_content,BitmapFragment())
        mFragmentTransaction.commitAllowingStateLoss()*/

        mFragmentTransaction = supportFragmentManager.beginTransaction()
        mFragmentTransaction.add(R.id.bitmap_content,BitmapFragment())
        mFragmentTransaction.commitAllowingStateLoss()

        Log.d("zzpliveData","BitmapViewModel class1:${BitmapViewModel::class.java} class2:${BitmapViewModel::javaClass}}")

        Log.d("zzpliveData","activity:${ViewModelProviders.of(this).get(BitmapViewModel::class.java)}")
    }

    fun remove(v: View?){
        supportFragmentManager.findFragmentById(R.id.bitmap_content)?.let {
            mFragmentTransaction = supportFragmentManager.beginTransaction()
            mFragmentTransaction.remove(it)
            mFragmentTransaction.commitAllowingStateLoss()
        }
    }

    fun add(v: View?){
        mFragmentTransaction = supportFragmentManager.beginTransaction()
        mFragmentTransaction.add(R.id.bitmap_content,BitmapFragment())
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }

    fun replace(v: View?){
        mFragmentTransaction = supportFragmentManager.beginTransaction()
        mFragmentTransaction.replace(R.id.bitmap_content,BitmapFragment())
        mFragmentTransaction.commitAllowingStateLoss()
    }

    fun back(v: View?){
        Log.i("BitmapFragmentTAG","popBackStack")
        supportFragmentManager.popBackStack()
    }

    fun hide(v: View?){
        supportFragmentManager.findFragmentById(R.id.bitmap_content)?.let {
            mFragmentTransaction = supportFragmentManager.beginTransaction()
            mFragmentTransaction.hide(it)
            mFragmentTransaction.commit()
        }
    }

    fun show(v: View?){
        supportFragmentManager.findFragmentById(R.id.bitmap_content)?.let {
            mFragmentTransaction = supportFragmentManager.beginTransaction()
            mFragmentTransaction.show(it)
            mFragmentTransaction.commit()
        }
    }

    fun attach(v: View?){
        supportFragmentManager.findFragmentById(R.id.bitmap_content)?.let {
            mFragmentTransaction = supportFragmentManager.beginTransaction()
            mFragmentTransaction.attach(it)
            mFragmentTransaction.commit()
        }
    }

    fun detach(v: View?){
        supportFragmentManager.findFragmentById(R.id.bitmap_content)?.let {
            mFragmentTransaction = supportFragmentManager.beginTransaction()
            mFragmentTransaction.detach(it)
            mFragmentTransaction.commit()
        }
    }

    fun comunicate(v: View?){
        ViewModelProviders.of(this).get(BitmapViewModel::class.java).setValue("send message")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG,"onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG,"onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG,"onStop")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG,"onStart")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG,"onSaveInstanceState")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d(TAG,"onRestoreInstanceState")
    }

}