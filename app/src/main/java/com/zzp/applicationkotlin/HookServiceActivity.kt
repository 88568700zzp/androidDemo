package com.zzp.applicationkotlin

import android.Manifest
import android.app.ActivityManager
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.zzp.applicationkotlin.service.hook.ActivityManagerHook
import com.zzp.applicationkotlin.service.hook.HookSetting
import com.zzp.applicationkotlin.service.hook.LocationManagerHook
import com.zzp.applicationkotlin.service.hook.TelephonyManagerHook
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
        var activityManager = (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
        activityManager.runningAppProcesses?.forEach{
            Log.d(TAG,"${it}")
        }
    }


}