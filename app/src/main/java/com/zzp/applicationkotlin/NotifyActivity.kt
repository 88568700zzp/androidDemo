package com.zzp.applicationkotlin

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_notify.*


class NotifyActivity : AppCompatActivity() , View.OnClickListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notify)
        notify.setOnClickListener(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var channelId = "upgrade"
            var channelName = "升级"
            var importance = NotificationManager.IMPORTANCE_HIGH
            createNotificationChannel(channelId, channelName, importance)
        }

        var intentFilter = IntentFilter()
        intentFilter.addAction("zzp")
        intentFilter.addAction("zzy")
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
        registerReceiver(receiver,intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    //创建通知渠道
    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun createNotificationChannel(
        channelId: String,
        channelName: String,
        importance: Int
    ) {
        val channel = NotificationChannel(channelId, channelName, importance)
        val notificationManager = getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    override fun onClick(v: View?) {
        doNotify(100)
    }

    private fun doNotify(id:Int){
        val remoteViews = RemoteViews(packageName, R.layout.layout_notification_function)
        var intent1 = Intent()
        intent1.setAction("zzp")
        intent1.putExtra("zzp",2)
        remoteViews.setOnClickPendingIntent(R.id.notify_speed_check, PendingIntent.getBroadcast(this,0,intent1, PendingIntent.FLAG_UPDATE_CURRENT))

        var intent2 = Intent()
        intent2.setAction("zzp")
        intent2.putExtra("zzp",1)
        remoteViews.setOnClickPendingIntent(R.id.notify_boost, PendingIntent.getBroadcast(this,0,intent2, PendingIntent.FLAG_UPDATE_CURRENT))

        val manager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification: Notification = NotificationCompat.Builder(this, "upgrade")
            .setContentTitle("升级")
            .setContentText("程序员终于下班了。。")
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.doll_enter_wifi_safe)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
            .setAutoCancel(true)
            .setOngoing(false)
            .setCustomContentView(remoteViews)
            .build()
        manager.notify(id, notification)
    }

    var receiver = object:BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("zzp","onReceive ${intent?.action} ${intent?.getIntExtra("zzp",-1)}")
        }

    }
}