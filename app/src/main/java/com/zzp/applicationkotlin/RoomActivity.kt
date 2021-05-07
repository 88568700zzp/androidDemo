package com.zzp.applicationkotlin

import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.zzp.applicationkotlin.database.DataBaseManager
import com.zzp.applicationkotlin.database.Song
import com.zzp.applicationkotlin.viewmodel.RoomViewModel
import kotlinx.android.synthetic.main.activity_room.*
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




        mViewModelProvider = ViewModelProviders.of(this)

        var roomViewModel = mViewModelProvider?.get(RoomViewModel::class.java)

        roomViewModel?.getValue()?.observe(this, Observer<String> {
            Log.d(TAG,"onChanged ${it}")
        })
        roomViewModel?.setValue("zzp ${getAndroidId()}")

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
        roomViewModel?.setValue("zzp onResume ${getAndroidId()}")
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