package com.zzp.applicationkotlin

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

/**
 *
 * Created by samzhang on 2021/7/16.
 */
class ProviderActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_provider)
    }

    fun query(view: View){
        var uri = Uri.parse("content://com.zzp.databaseprovider/user")
        contentResolver.query(uri, null, null, null, null)
    }

    fun insert(view: View){
        var uri = Uri.parse("content://com.zzp.databaseprovider/user")
        var contentValues = ContentValues()
        contentValues.put("id",1)
        contentResolver.insert(uri, contentValues)
    }

    fun delete(view: View){
        var uri = Uri.parse("content://com.zzp.databaseprovider/user")
        contentResolver.delete(uri,"3", arrayOf())
    }

    fun update(view: View){
        var uri = Uri.parse("content://com.zzp.databaseprovider/user")
        var contentValues = ContentValues()
        contentValues.put("id",1)
        contentResolver.update(uri,contentValues,"2",null)
    }

}