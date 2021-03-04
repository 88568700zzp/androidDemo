package com.zzp.applicationkotlin

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import java.net.InetAddress

class MainActivity : AppCompatActivity() {

    val permission = arrayOf("android.permission.ACCESS_WIFI_STATE"
        , "android.permission.CHANGE_WIFI_STATE"
        , "android.permission.ACCESS_COARSE_LOCATION"
        , "android.permission.ACCESS_FINE_LOCATION")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        permission.iterator().forEach {
            Log.d("FirstFragment","${it}  ${ContextCompat.checkSelfPermission(MainActivity@this,it)}")
        }

        requestPermissions(permission,0x12);

        object : Thread() {
            override fun run(){
                val inetAddress: InetAddress = InetAddress.getByName("www.baidu.com")
                val ip: String = inetAddress.getHostAddress()
                Log.d("zzp","ip:${ip}")
            }
        }.start()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}