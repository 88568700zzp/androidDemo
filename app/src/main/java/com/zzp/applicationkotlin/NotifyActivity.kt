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
import android.net.*
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

    private val TAG = "NotifyActivity123"

    lateinit var mConnectivityManager:ConnectivityManager
    lateinit var mWifiManager: WifiManager

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

        mWifiManager  = getApplicationContext().getSystemService(Context.WIFI_SERVICE) as WifiManager

        var intentFilter = IntentFilter()
        intentFilter.addAction("zzp")
        intentFilter.addAction("zzy")
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION)
        registerReceiver(receiver,intentFilter)



        mConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        mConnectivityManager.registerNetworkCallback(
            NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .build(),callBack)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
        mConnectivityManager.unregisterNetworkCallback(callBack)
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
            if(intent?.action == WifiManager.NETWORK_STATE_CHANGED_ACTION){
               var extra:NetworkInfo? = intent?.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO)
                Log.d(TAG,"NETWORK_STATE_CHANGED_ACTION:${extra?.toString()}")
                extra?.let {
                    Log.d(TAG,"NETWORK_STATE_CHANGED_ACTION ${mWifiManager.connectionInfo?.ssid} ${it.type} ${it.typeName} ${it.state}")
                }
            }else if(intent?.action == WifiManager.WIFI_STATE_CHANGED_ACTION){
                var state= intent?.getIntExtra(WifiManager.EXTRA_WIFI_STATE,0)
                Log.d(TAG,"WIFI_STATE_CHANGED_ACTION:${state}")
            }else if(intent?.action == ConnectivityManager.CONNECTIVITY_ACTION){
                Log.d(TAG,"CONNECTIVITY_ACTION ${mConnectivityManager.activeNetwork}")
                Log.d(TAG,"CONNECTIVITY_ACTION ${mConnectivityManager.activeNetworkInfo?.typeName}")
            }
        }

    }

    private var callBack = object: ConnectivityManager.NetworkCallback(){
        private val NEWTAG = "NetworkCallback"

        override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
            super.onBlockedStatusChanged(network, blocked)
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            Log.d(NEWTAG,"onCapabilitiesChanged ${network}")
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            Log.d(NEWTAG,"onLost ${network}")
        }

        override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
            super.onLinkPropertiesChanged(network, linkProperties)
            Log.d(NEWTAG,"onLinkPropertiesChanged ${network}")
        }

        override fun onUnavailable() {
            super.onUnavailable()
            Log.d(NEWTAG,"onUnavailable")
        }

        override fun onLosing(network: Network, maxMsToLive: Int) {
            super.onLosing(network, maxMsToLive)
            Log.d(NEWTAG,"onLosing ${network}")
        }

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            Log.d(NEWTAG,"onAvailable ${network}")
        }
    }
}