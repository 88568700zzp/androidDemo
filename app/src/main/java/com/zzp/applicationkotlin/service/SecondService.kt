package com.zzp.applicationkotlin.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import com.zzp.applicationkotlin.R

/**
 *
 * Created by samzhang on 2021/1/29.
 */
class SecondService :Service(){



    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("SecondService","onStartCommand")
        startNotify()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startNotify() {
        val notification: Notification
        val CHANNEL_ONE_ID = "zzp"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val mUri = Settings.System.DEFAULT_NOTIFICATION_URI
            val mChannel =
                NotificationChannel(CHANNEL_ONE_ID, "driver", NotificationManager.IMPORTANCE_LOW)
            mChannel.description = "description"
            mChannel.setSound(mUri, Notification.AUDIO_ATTRIBUTES_DEFAULT)
            mManager.createNotificationChannel(mChannel)
            notification = Notification.Builder(this, CHANNEL_ONE_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("secondService")
                .build()
        } else {
            // 提升应用权限
            notification = Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("zzp")
                .build()
        }
        notification.flags = Notification.FLAG_ONGOING_EVENT
        notification.flags = notification.flags or Notification.FLAG_NO_CLEAR
        notification.flags = notification.flags or Notification.FLAG_FOREGROUND_SERVICE
        startForeground(10000, notification)
    }


}