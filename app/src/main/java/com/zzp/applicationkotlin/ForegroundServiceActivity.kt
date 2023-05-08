package com.zzp.applicationkotlin

import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.telephony.TelephonyManager
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.zzp.applicationkotlin.aidl.IITalk
import com.zzp.applicationkotlin.service.FirstService
import com.zzp.applicationkotlin.service.SecondService
import kotlinx.android.synthetic.main.activity_foreground_service.*

/**
 *
 * Created by samzhang on 2021/2/26.
 */
class ForegroundServiceActivity: AppCompatActivity() {

    companion object{
        var REQUEST_CODE = 0x123
    }

    private var mConnect = false

    private var mTalk:IITalk ?= null

    private var mHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_foreground_service)
        startService.setOnClickListener{
            var intent = Intent()
            Log.e("zzp123", "startService")
            intent.setClassName(packageName, SecondService::class.java.name)
            intent.putExtra("key",100)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            }else{
                startService(intent)
            }
            //startService(intent)
            /*var dialog =  AlertDialog.Builder(this).setMessage("zzp").create()
            dialog.show()
            Handler().postDelayed({
                Log.d("zzp", "dialog:${dialog.window?.getAttributes()}")
                Log.d("zzp", "activity:${window?.getAttributes()}")
            }, 1000L)*/
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

        bindService.setOnClickListener {
            if(!mConnect) {
                var intent = Intent()
                Log.e("zzp123", "bindService")
                intent.setClassName(packageName, FirstService::class.java.name)
                bindService(intent, conn, Context.BIND_AUTO_CREATE)
            }

            if(mTalk != null){
                mTalk?.doTalk("6666")
            }
        }

        stopService.setOnClickListener {
           with(Intent(this@ForegroundServiceActivity, SecondService::class.java)){
               stopService(this)
           }

        }

        mHandler.postDelayed({
            startService.performClick()
        },3000L)
    }

    var conn = object:ServiceConnection{
        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d("zzp123", "onServiceDisconnected:${name}")
            mConnect = false
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mConnect = true
            Log.d("zzp123", "onServiceConnected:${name} service:${service}")
            mTalk =  IITalk.Stub.asInterface(service)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("zzp1234","onActivityResult:$requestCode")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d("zzp1234","onNewIntent")
    }
}