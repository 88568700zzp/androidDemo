package com.zzp.applicationkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler(Looper.getMainLooper()).postDelayed(runnbale,3000);
    }

    private var runnbale = object :Runnable{
        override fun run() {
            var intent = Intent()
            intent.setClassName(packageName,"com.zzp.applicationkotlin.NewMainActivity")
            startActivity(intent)
        }

    }
}