package com.zzp.applicationkotlin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.android.synthetic.main.activity_share.*
import java.io.*

/**
 *
 * Created by samzhang on 2021/9/7.
 */
class ShareActivity : AppCompatActivity(),View.OnClickListener{

    private val TAG = "ShareActivity_"

    private val takePicturePreviewLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { result ->
        result?.let {
            Log.d(TAG,"width:${it.width} height:${it.height}")
        }
    }

    private val documentLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) { result ->
        result?.let {
            Log.d(TAG,"documentLauncher:${it}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)

        btn_text.setOnClickListener(this)
        btn_img.setOnClickListener(this)
        btn_file.setOnClickListener(this)
        btn_pic.setOnClickListener(this)
        btn_photo.setOnClickListener(this)

        Log.d(TAG,"getRootDirectory:${Environment.getRootDirectory().absolutePath}")
        Log.d(TAG,"getExternalStoragePublicDirectory:${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath}")
        Log.d(TAG,"dataDirectory:${Environment.getDataDirectory().absolutePath}")
        Log.d(TAG,"getStorageDirectory:${Environment.getStorageDirectory().absolutePath}")
        Log.d(TAG,"getExternalStorageDirectory:${Environment.getExternalStorageDirectory().absolutePath}")
        Log.d(TAG,"getDownloadCacheDirectory:${Environment.getDownloadCacheDirectory().absolutePath}")
        Log.d(TAG,"cacheDir:${cacheDir.absolutePath}")
        Log.d(TAG,"externalCacheDir:${externalCacheDir?.absolutePath}")
        Log.d(TAG,"getExternalFilesDir:${getExternalFilesDir(Environment.DIRECTORY_DCIM)?.absolutePath}")
        Log.d(TAG,"getFilesDir:${filesDir.absolutePath}")
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
                        "$packageName.fileprovider",
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
            btn_pic -> {
                /*Intent(Intent.ACTION_PICK).apply {
                    startActivityForResult(this,0x123)
                }*/
                documentLauncher.launch(null)
            }
            btn_photo->{
                takePicturePreviewLauncher.launch(null)
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, returnIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, returnIntent)
        // If the selection didn't work
        if (resultCode != Activity.RESULT_OK) {
            // Exit without doing anything else
            return
        }
        returnIntent?.data?.let {
            Log.d(TAG,"onActivityResult:${it.toString()}")
            contentResolver.openFileDescriptor(it, "r")?.apply {
                var fileReader = FileReader(fileDescriptor)
                fileReader.readLines().forEach {line->
                    Log.d(TAG,"${line}")
                }
            }
            contentResolver.query(it, null, null, null, null)?.let {cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                cursor.moveToFirst()
                Log.d(TAG,"name:${cursor.getString(nameIndex)} size:${cursor.getLong(sizeIndex)}")
            }
        }
    }
}