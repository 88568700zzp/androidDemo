package com.zzp.applicationkotlin

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.AndroidRuntimeException
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.zzp.applicationkotlin.view.WindowDragFrameLayout
import kotlinx.android.synthetic.main.activity_add_window.*


/**
 *
 * Created by samzhang on 2021/3/5.
 */
class AddWindowActivity :AppCompatActivity(), View.OnClickListener{

    private var mWindowManager:WindowManager?= null
    private lateinit var mView: WindowDragFrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_window)

        mWindowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        mView = WindowDragFrameLayout(this)

        var textView = TextView(this)
        textView.text = "fdsafdas"
        textView.setTextColor(Color.RED)
        mView.addView(textView)

        textView.setOnClickListener(){
            Toast.makeText(AddWindowActivity@ this, "toast", Toast.LENGTH_LONG).show()
        }


        btn_add.setOnClickListener(AddWindowActivity@ this)
        btn_remove.setOnClickListener(AddWindowActivity@ this)
        
      /*  val dialog = AlertDialog.Builder(this).setMessage("zzp").show()

        dialog.window

        Handler(Looper.getMainLooper()).postDelayed(object:Runnable{
            override fun run() {
                finish()
            }

        },2000)

        Handler(Looper.getMainLooper()).postDelayed(object:Runnable{
            override fun run() {
                dialog.dismiss()
            }

        },3000)*/
    }

    override fun onClick(v: View?) {
        when(v){
            btn_add -> {
                if (!checkFloating()) {
                    return
                }
                var mLayoutParam = WindowManager.LayoutParams()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mLayoutParam.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                } else {
                    mLayoutParam.type = WindowManager.LayoutParams.TYPE_PHONE;
                }
                mLayoutParam.width = 500
                mLayoutParam.flags = mLayoutParam.flags or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                mLayoutParam.height = 100
                mLayoutParam.gravity = Gravity.LEFT or Gravity.TOP

                val outMetrics = DisplayMetrics()
                windowManager.defaultDisplay.getMetrics(outMetrics)

                mView.mWindowManager = mWindowManager
                mView.mLayoutParams = mLayoutParam
                mView.init(outMetrics.widthPixels,outMetrics.heightPixels)

                mLayoutParam.x = outMetrics.widthPixels - mLayoutParam.width
                mLayoutParam.y = (outMetrics.heightPixels - mLayoutParam.height)/2

                mWindowManager?.addView(mView, mLayoutParam)
            }
            btn_remove -> {
                mWindowManager?.removeView(mView)
            }
        }
    }

    private fun checkFloating() :Boolean {
        if (!Settings.canDrawOverlays(this)) {
            startActivityForResult(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), 0);
            return false
        }
        return true
    }
}