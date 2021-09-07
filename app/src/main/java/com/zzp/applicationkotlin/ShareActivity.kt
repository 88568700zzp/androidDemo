package com.zzp.applicationkotlin

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_share.*
import java.io.File

/**
 *
 * Created by samzhang on 2021/9/7.
 */
class ShareActivity : AppCompatActivity(),View.OnClickListener{

    private val TAG = "ShareActivity_"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)

        btn_text.setOnClickListener(this)
        btn_img.setOnClickListener(this)
        btn_file.setOnClickListener(this)

        Log.d(TAG,"getRootDirectory:${Environment.getRootDirectory().absolutePath}")
        Log.d(TAG,"dataDirectory:${Environment.getDataDirectory().absolutePath}")
        Log.d(TAG,"getStorageDirectory:${Environment.getStorageDirectory().absolutePath}")
        Log.d(TAG,"getExternalStorageDirectory:${Environment.getExternalStorageDirectory().absolutePath}")
        Log.d(TAG,"getDownloadCacheDirectory:${Environment.getDownloadCacheDirectory().absolutePath}")
        Log.d(TAG,"cacheDir:${cacheDir.absolutePath}")
        Log.d(TAG,"externalCacheDir:${externalCacheDir?.absolutePath}")
    }

    override fun onClick(v: View?) {
        when(v){
            btn_text -> {
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.putExtra(
                    Intent.EXTRA_TEXT,
                    "聚划算发补贴福利了，大牌正品，买贵必陪，小伙伴们快来抢购吧~  https://10sd1.kuaizhan.com/?_s=kKUX9"
                )
                intent.type = "text/plain"
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(Intent.createChooser(intent, "选择分享应用"))
            }
            btn_img -> {
                val shareFile = File(Environment.getExternalStorageDirectory(),"php.jpg")
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    val contentUri: Uri = FileProvider.getUriForFile(
                        this,
                        getPackageName().toString() + ".fileprovider",
                        shareFile
                    )
                    intent.putExtra(Intent.EXTRA_STREAM, contentUri)
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                } else {
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(shareFile))
                }
                intent.type = "image/*"
                startActivity(Intent.createChooser(intent, "选择分享应用"))
            }
            btn_file -> {
                val shareFile = File(Environment.getExternalStorageDirectory(),"php.jpg")
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    val contentUri: Uri = FileProvider.getUriForFile(
                        this,
                        getPackageName().toString() + ".fileprovider",
                        shareFile
                    )
                    intent.putExtra(Intent.EXTRA_STREAM, contentUri)
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                } else {
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(shareFile))
                }
                intent.type = "application/vnd.ms-excel"
                startActivity(Intent.createChooser(intent, "选择分享应用"))
            }
        }

    }
}