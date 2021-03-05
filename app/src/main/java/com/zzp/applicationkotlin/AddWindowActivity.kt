package com.zzp.applicationkotlin

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_window.*

/**
 *
 * Created by samzhang on 2021/3/5.
 */
class AddWindowActivity :AppCompatActivity(), View.OnClickListener{

    private var mWindowManager:WindowManager?= null
    private lateinit var mTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_window)

        mWindowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        mTextView = TextView(this)
        mTextView.text = "zzp"
        mTextView.gravity = Gravity.CENTER
        mTextView.setBackgroundColor(Color.BLUE)
        mTextView.setTextColor(Color.RED)
        mTextView.setOnClickListener(){
            Toast.makeText(AddWindowActivity@this,"toast",Toast.LENGTH_LONG).show()
        }


        btn_add.setOnClickListener(AddWindowActivity@this)
        btn_remove.setOnClickListener(AddWindowActivity@this)
    }

    override fun onClick(v: View?) {
        when(v){
            btn_add->{
                if(!checkFloating()){
                    return
                }
                var mLayoutParam = WindowManager.LayoutParams()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mLayoutParam.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
                } else {
                    mLayoutParam.type = WindowManager.LayoutParams.TYPE_PHONE;
                }
                mLayoutParam.width = 500
                mLayoutParam.flags = mLayoutParam.flags or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                mLayoutParam.height = 100
                mLayoutParam.y = 100
                mLayoutParam.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                mWindowManager?.addView(mTextView,mLayoutParam)
            }
            btn_remove->{
                mWindowManager?.removeView(mTextView)
            }
        }
    }

    private fun checkFloating() :Boolean {
        if (!Settings.canDrawOverlays(this)) {
            startActivityForResult( Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), 0);
            return false
        }
        return true
    }
}