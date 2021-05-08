package com.zzp.watchapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        text.setOnClickListener{
            var intent = Intent()
            intent.setClass(MainActivity@this,ListActivity::class.java)
            startActivity(intent)
        }
    }
}