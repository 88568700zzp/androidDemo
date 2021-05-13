package com.zzp.applicationkotlin.util

import android.graphics.drawable.GradientDrawable

/**
 * 圆角工具栏
 * Created by samzhang on 2021/5/12.
 */
object GradientUtil {

    fun createGradientDrawable(color:Int,radius:Float,size:IntArray ?= null):GradientDrawable{
        var drawable = GradientDrawable()
        drawable.setColor(color)
        drawable.setCornerRadius(radius)
        size?.takeIf { it.size >= 2}?.let {
            drawable.setSize(size[0],size[1])
        }
        return drawable
    }

    fun createGradientDrawable(color:IntArray,radius:Float,size:IntArray ?= null):GradientDrawable{
        var drawable = GradientDrawable()
        drawable.setColors(color)
        drawable.setCornerRadius(radius)
        size?.takeIf { it.size >= 2}?.let {
            drawable.setSize(size[0],size[1])
        }
        return drawable
    }
}