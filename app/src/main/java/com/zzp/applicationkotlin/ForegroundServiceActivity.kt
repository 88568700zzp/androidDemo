package com.zzp.applicationkotlin

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.zzp.applicationkotlin.service.FirstService
import kotlinx.android.synthetic.main.activity_foreground_service.*

/**
 *
 * Created by samzhang on 2021/2/26.
 */
class ForegroundServiceActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_foreground_service)
        startService.setOnClickListener{
            var intent= Intent()
            Log.e("ForegroundServiceActivity","startFirstService")
            intent.setClassName(packageName, FirstService::class.java.name)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            }else{
                startService(intent)
            }
        }
    }

    fun createIntent():Intent{
        var intent= Intent()
        intent.setClassName(packageName, MainActivity::class.java.name)
        return intent
    }

}