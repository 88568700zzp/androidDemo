package com.zzp.applicationkotlin.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager

object UtilObject {
    fun ImageView.loadUrl(requestManager: RequestManager = Glide.with(context), url:String){
        requestManager.load(url).into(this)
    }

    fun ImageView.logInvoke(index:Int,call:(index:Int)->String){
        println("logInvoke before")
        call.invoke(index)
        println("logInvoke after")
    }
}