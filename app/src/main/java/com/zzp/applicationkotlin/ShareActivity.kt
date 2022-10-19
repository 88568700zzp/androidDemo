package com.zzp.applicationkotlin

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_share.*
import java.io.*

/**
 *
 * Created by samzhang on 2021/9/7.
 */
class ShareActivity : AppCompatActivity(),View.OnClickListener{

    private val TAG = "ShareActivity_"

    private var REQUEST_CODE = 0x1234

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
        query_file.setOnClickListener(this)

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
        Log.d(TAG,"getDataDir:${dataDir.absolutePath}")
        //filelist(dataDir)
        filelist(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS))
    }

    private fun filelist(file:File){
        if(!file.exists() || file == null){
            return
        }
        if(file.isDirectory){
            if(file.list() != null && file.list().isNotEmpty()) {
                file.listFiles().forEach {
                    filelist(it)
                }
            }
        }else{
            Log.d(TAG,"path:${file.absolutePath}")
        }
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

            query_file ->{
                /*contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null)?.let {
                    while(it.moveToNext()){
                        var ID = it.getLong(it.getColumnIndex(MediaStore.Images.Media._ID))
                        var DISPLAY_NAME = it.getString(it.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME))
                        var VOLUME_NAME = it.getString(it.getColumnIndex(MediaStore.Images.Media.VOLUME_NAME))
                        Log.d(TAG,"ID:${ID} DISPLAY_NAME:${DISPLAY_NAME} VOLUME_NAME:${VOLUME_NAME}")

                        *//*MediaStore.Images.Media.getBitmap(
                            contentResolver, MediaStore.Images.Media.getContentUri(VOLUME_NAME)
                        ).let {
                            Log.d(TAG,"width:${it.width} height:${it.height}")
                        }*//*
                    }
                }*/
                val intent = Intent().apply {
                    action = Intent.ACTION_GET_CONTENT


                    type = "*/*"

                    addCategory(Intent.CATEGORY_OPENABLE)
                }
                try {
                    startActivityForResult(intent, REQUEST_CODE)
                } catch (exception: ActivityNotFoundException) {
                    finish()
                } catch (exception: Exception) {
                    finish()
                }

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
                val mimeIndex = cursor.getColumnIndex("mime_type")
                cursor.columnNames.forEach {
                    Log.d(TAG,"columnName:$it");
                }
                cursor.moveToFirst()
                Log.d(TAG,"name:${cursor.getString(nameIndex)} size:${cursor.getLong(sizeIndex)} mimeIndex:${cursor.getString(mimeIndex)}}")
            }
        }
    }
}