package com.zzp.applicationkotlin.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import com.zzp.applicationkotlin.ForegroundServiceActivity
import com.zzp.applicationkotlin.R

/**
 *
 * Created by samzhang on 2021/1/29.
 */
class SecondService :Service(){

    private val TAG = "SecondService"

    private var index = 0

    private val notificationId = 10000

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG,"onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG,"onStartCommand:$index")
        index++
        if(index == 3){
             stopForeground(STOP_FOREGROUND_REMOVE)
        }else {
            startNotify()
        }
        intent?.takeIf {
            it.hasExtra("key")
        }?.let {
            Log.d(TAG,"key:${it.getIntExtra("key",0)}")
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"onDestroy")
    }

    private fun startNotify() {
        var intent = Intent()
        intent.setClass(this@SecondService,ForegroundServiceActivity::class.java)
        var peddingIntent:PendingIntent =
            PendingIntent.getActivity(this@SecondService,ForegroundServiceActivity.REQUEST_CODE,intent,PendingIntent.FLAG_IMMUTABLE)


        val notification: Notification
        val CHANNEL_ONE_ID = "zzp"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val mUri = Settings.System.DEFAULT_NOTIFICATION_URI
            val mChannel =
                NotificationChannel(CHANNEL_ONE_ID, "driver", NotificationManager.IMPORTANCE_LOW)
            mChannel.description = "通知栏666"
            mChannel.setSound(mUri, Notification.AUDIO_ATTRIBUTES_DEFAULT)
            mManager.createNotificationChannel(mChannel)
            notification = Notification.Builder(this, CHANNEL_ONE_ID)
                .setContentIntent(peddingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("notification has channel")
                .build()
        } else {
            // 提升应用权限
            notification = Notification.Builder(this)
                .setContentIntent(peddingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("notification no channel")
                .build()
        }

        notification.flags = Notification.FLAG_ONGOING_EVENT
        notification.flags = notification.flags or Notification.FLAG_NO_CLEAR
        notification.flags = notification.flags or Notification.FLAG_FOREGROUND_SERVICE
        startForeground(notificationId, notification)

    }



}