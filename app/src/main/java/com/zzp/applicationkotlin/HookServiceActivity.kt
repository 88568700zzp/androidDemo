package com.zzp.applicationkotlin

import android.Manifest
import android.app.ActivityManager
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Build.VERSION_CODES.O
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.zzp.applicationkotlin.service.hook.*
import com.zzp.applicationkotlin.util.TimeMonitor

/**
 *
 * Created by samzhang on 2021/7/16.
 */
class HookServiceActivity : AppCompatActivity(){

    val TAG = "HookServiceActivity"

    var mTimeMonitor = TimeMonitor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mTimeMonitor.start()
        setContentView(R.layout.activity_hook_service)
        mTimeMonitor.record("setContentView")

        //ClipboardHook.hookService(this)
        //TelephonyManagerHook.hookService(this)
        //mTimeMonitor.record("TelephonyManagerHook.hookService")
        //LocationManagerHook.hookService(this)
        //mTimeMonitor.record("LocationManagerHook.hookService")
        //HookSetting.hookService(this)
        ActivityManagerHook.hookService(this)
        ActivityManagerHook2.hookService(this)
        //PackageManagerHook.hookService(this.baseContext)
    }

    fun hook(view: View){
        /*val dialog = Dialog(this@HookServiceActivity)
        dialog.setTitle("Dadfadfa")
        dialog.show()*/
        /*var telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        Log.d(TAG,"deviceId:${telephonyManager.deviceId} imei:${telephonyManager.imei}")*/

        /*Log.d(TAG,"androidId:${Settings.Secure.getString(
            contentResolver, Settings.Secure.ANDROID_ID)}")*/

        /*var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.allProviders.takeIf { it.isNotEmpty() }?.forEach {
            if(it == LocationManager.GPS_PROVIDER) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    Log.d(TAG, "hasPermission provider:${it}")
                    locationManager.requestLocationUpdates(
                        it,
                        5000L,
                        10f
                    ) { location -> Log.d(TAG, "location:${location.toString()}") }
                } else {
                    Log.d(TAG, "not hasPermission")
                }
            }

        }*/
       /* var activityManager = (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
        activityManager.getRunningServices(1)?.forEach{
            Log.d(TAG,"${it}")
        }*/


        //var telphone = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        //Log.d(TAG,"imei:${telphone.imei} deviceid:${telphone.deviceId}")

        /*if(Build.VERSION.SDK_INT >= O) {
            packageManager.canRequestPackageInstalls()
        }*/
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(Manifest.permission.READ_PHONE_STATE),0x123);
        }
    }


}