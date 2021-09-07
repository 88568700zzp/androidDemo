package com.zzp.applicationkotlin

import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.zzp.applicationkotlin.aidl.IITalk
import com.zzp.applicationkotlin.service.FirstService
import kotlinx.android.synthetic.main.activity_foreground_service.*

/**
 *
 * Created by samzhang on 2021/2/26.
 */
class ForegroundServiceActivity: AppCompatActivity() {

    private var mConnect = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_foreground_service)
        startService.setOnClickListener{
            var intent= Intent()
            Log.e("zzp123", "startFirstService")
            intent.setClassName(packageName, FirstService::class.java.name)
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            }else{
                startService(intent)
            }*/
            bindService(intent, conn, android.content.Context.BIND_AUTO_CREATE)

            var dialog =  AlertDialog.Builder(this).setMessage("zzp").create()
            dialog.show()
            Handler().postDelayed({
                Log.d("zzp", "dialog:${dialog.window?.getAttributes()}")
                Log.d("zzp", "activity:${window?.getAttributes()}")
            }, 1000L)
        }

        /*Handler(Looper.getMainLooper()).postDelayed(object:Runnable{
            @RequiresApi(Build.VERSION_CODES.O)
            override fun run() {
                var intent= Intent()
                Log.e("zzp123","startFirstService")
                intent.setClassName(packageName, FirstService::class.java.name)
                startForegroundService(intent)
            }

        },7000L)*/
    }

    var conn = object:ServiceConnection{
        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d("zzp123", "onServiceDisconnected:${name}")
            mConnect = false
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mConnect = true
            Log.d("zzp123", "onServiceConnected:${name} service:${service}")
            var talk =  IITalk.Stub.asInterface(service)
            talk?.doTalk("doTalk")
        }

        override fun onBindingDied(name: ComponentName?) {
            super.onBindingDied(name)
            Log.d("zzp123", "onBindingDied:${name}")
            mConnect = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(mConnect) {
            unbindService(conn)
        }
    }

    fun createIntent():Intent{
        var intent= Intent()
        intent.setClassName(packageName, MainActivity::class.java.name)
        return intent
    }

}