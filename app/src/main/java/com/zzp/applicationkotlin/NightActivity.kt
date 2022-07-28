package com.zzp.applicationkotlin

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import kotlinx.android.synthetic.main.activity_night.*

class NightActivity : AppCompatActivity() {

    private val TAG = "_NightActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_night)

        Log.d(TAG,"onCreate")

        var isNight = isNightMode()

        Log.d(TAG,"isNightMode:${isNight}")

        if(isNight){
            Log.d(TAG,"isNightMode:${AppCompatDelegate.getDefaultNightMode()}")
        }

        change_activity_mode.setOnClickListener {
            var isNight = isNightMode()
            if (isNight) {
                delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
            } else {
                delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
            }
        }
        change_app_mode.setOnClickListener {
            var nightMode = AppCompatDelegate.getDefaultNightMode()

            if (nightMode != MODE_NIGHT_NO) {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
            } else {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
            }
        }

    }

    /**
     * 判断当前是否深色模式
     *
     * @return 深色模式返回 true，否则返回false
     */
    fun isNightMode(): Boolean {
        return when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        Log.d(TAG,"onConfigurationChanged:${newConfig.isNightModeActive}")


    }
}