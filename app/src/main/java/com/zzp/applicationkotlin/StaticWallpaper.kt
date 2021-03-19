package com.zzp.applicationkotlin

import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Environment
import android.service.wallpaper.WallpaperService
import android.text.TextUtils
import android.view.SurfaceHolder
import androidx.core.graphics.drawable.toBitmap
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream


/**
 * 动态壁纸
 *
 * @author chenbenbin
 * @date 2021/1/20
 */
class StaticWallpaper : WallpaperService() {

    override fun onCreateEngine(): Engine {
        return StaticEngine()
    }

    companion object {
        /**
         * 是否正在应用中
         */
        var isUsing = false
    }

    inner class StaticEngine : WallpaperService.Engine() {

        override fun onCreate(surfaceHolder: SurfaceHolder?) {
            super.onCreate(surfaceHolder)
            if (!isPreview) {
                isUsing = true
            }
        }

        override fun onDestroy() {
            super.onDestroy()
            if (!isPreview) {
                isUsing = false
            }
        }

        override fun onSurfaceCreated(holder: SurfaceHolder?) {
            super.onSurfaceCreated(holder)
            val wallpaperManager =
                WallpaperManager.getInstance(applicationContext)
            val wallpaperDrawable =
                try{
                    wallpaperManager.drawable
                }
                catch (e:Error){
                null
                }

            val canvas: Canvas = holder!!.lockCanvas()

            var saveBitmap:String ?= null
            wallpaperDrawable?.let { saveBitmap = saveBitmap(wallpaperDrawable.toBitmap(), "/wallpaper.jpg") }

            if (isPreview) {
                var option = BitmapFactory.Options()
                option.inSampleSize = 2
                val bitmap = BitmapFactory.decodeResource(
                    applicationContext.resources,
                    R.drawable.wallpaper_preview,option
                )

                bitmap?.let {
                    var srcRect = Rect(0,0,bitmap.width,bitmap.height)
                    var rect = Rect(0,0,canvas.width,canvas.height)
                    canvas.drawBitmap(
                        bitmap,
                        srcRect,
                        rect,
                        null
                    )
                }
            } else {
               /* var bitmap: Bitmap? = null

                if (!TextUtils.isEmpty(saveBitmap)) {
                    bitmap = BitmapFactory.decodeFile(saveBitmap)
                }
                if (bitmap == null && wallpaperDrawable != null) {
                    bitmap = wallpaperDrawable.toBitmap()
                }

                if(bitmap != null){
                    val clipWidth = (1.0f * bitmap.height * canvas.width / canvas.height).toInt()
                    val src = Rect(0, 0, clipWidth, bitmap.height)
                    val dest = Rect(0, 0, canvas.width, canvas.height)
                    canvas.drawBitmap(bitmap, src, dest, null)
                }else{
                }*/
            }

            try{
                holder.unlockCanvasAndPost(canvas)
            }catch (e:Exception){

            }
        }

        private fun saveBitmap(bitmap: Bitmap, fileName: String?): String? {
            return try {
                val path =
                    applicationContext
                        .getExternalFilesDir(Environment.DIRECTORY_DCIM)?.path + fileName
                if (isUsing) {
                    return path
                }
                val stream: OutputStream = FileOutputStream(path)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                stream.close()
                path
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }
    }
}