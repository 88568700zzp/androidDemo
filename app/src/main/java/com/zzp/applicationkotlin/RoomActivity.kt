package com.zzp.applicationkotlin

import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.zzp.applicationkotlin.database.DataBaseManager
import com.zzp.applicationkotlin.database.Song
import com.zzp.applicationkotlin.http.HttpEngine
import com.zzp.applicationkotlin.util.GradientUtil
import com.zzp.applicationkotlin.viewmodel.RoomViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_room.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.util.*

/**
 *
 * Created by samzhang on 2021/4/23.
 */
class RoomActivity:AppCompatActivity(){

    private var TAG = "RoomActivity"

    private var mViewModelProvider:ViewModelProvider ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)
        addSong.setOnClickListener{
            var song = Song()
            song.name = "周杰伦"
            song.releaseYear = 1990
            DataBaseManager.getDataBase(RoomActivity@this).songDao.insertAll(song)
        }
        RoomActivity@this.lifecycle.addObserver(object : LifecycleEventObserver{
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                Log.d(TAG,"event:${event}")
            }

        })
        querySong.setOnClickListener{
            var liveData = DataBaseManager.getDataBase(RoomActivity@this).songDao.loadAll()
            liveData.observe(RoomActivity@this,object: Observer<List<Song>> {
                override fun onChanged(t: List<Song>?) {
                    Log.d(TAG,"onChanged ${Arrays.toString(t?.toTypedArray())}")
                }
            })
        }
        deleteSong.setOnClickListener{
            var song = Song()
            song.id = 1
            DataBaseManager.getDataBase(RoomActivity@this).songDao.delete(song)
        }
        queryNet.setOnClickListener {
            HttpEngine.getInstance().requestGet("http://www.baidu.com", null, object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                }

                override fun onResponse(call: Call, response: Response) {
                    response.body()?.let {
                        Observable.just(it.string()).observeOn(AndroidSchedulers.mainThread()).subscribe {body->
                            var roomViewModel = mViewModelProvider?.get(RoomViewModel::class.java)
                            roomViewModel?.netData?.value = body
                        }
                    }
                }

            })
        }

        //querySong.background = GradientUtil.createGradientDrawable(Color.RED,10f)

        mViewModelProvider = ViewModelProviders.of(this)

        var roomViewModel = mViewModelProvider?.get(RoomViewModel::class.java)

        roomViewModel?.getValue()?.observe(this, Observer<String> {
            Log.d(TAG,"onValueChanged ${it} ${Thread.currentThread().name}")
        })


        roomViewModel?.netData?.observe(this, Observer<String> {
            Log.d(TAG,"onNetChanged ${it} ${Thread.currentThread().name}")
        })

    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG,"onPause")
    }

    override fun onTopResumedActivityChanged(isTopResumedActivity: Boolean) {
        super.onTopResumedActivityChanged(isTopResumedActivity)
        Log.d(TAG,"onTopResumedActivityChanged ${isTopResumedActivity}")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG,"onResume")

        var roomViewModel = mViewModelProvider?.get(RoomViewModel::class.java)
        roomViewModel?.setValue("AndroidID：onResume ${getAndroidId()}")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG,"onStart")
    }

    fun getAndroidId():String{
        val ANDROID_ID = Settings.System.getString(contentResolver, Settings.Secure.ANDROID_ID)
        return ANDROID_ID
    }
}