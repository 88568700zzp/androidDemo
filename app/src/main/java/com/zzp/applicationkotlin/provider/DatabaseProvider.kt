package com.zzp.applicationkotlin.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import java.util.*
import kotlin.collections.ArrayList

/**
 *
 * Created by samzhang on 2021/7/16.
 */
class DatabaseProvider : ContentProvider() {

    private val TAG = "zzp12DatabaseProvider"

    val AUTOHORITY = "com.zzp.databaseprovider"

    private var userList = ArrayList<Int>()

    private var mMatcher: UriMatcher = UriMatcher(UriMatcher.NO_MATCH)

    init {
        mMatcher.addURI(AUTOHORITY, "user", 1)
    }

    override fun onCreate(): Boolean {
        Log.d(TAG, "DatabaseProvider onCreate")
        return true
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        var table = getTableName(uri)
        Log.d(TAG, "DatabaseProvider insert:${uri.toString()} table:${table}")
        if(table == "user"){
            var value = values?.get("id") as Int
            value?.let {
                userList.add(it)
                Log.d(TAG,"insert value:${it}")
            }
        }
        return uri
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        var table = getTableName(uri)
        Log.d(TAG, "DatabaseProvider query:${uri.toString()} table:${table}")
        if(table == "user"){
            Log.d(TAG,"query user:${Arrays.toString(userList.toArray())}")
        }
        return null
    }



    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        var table = getTableName(uri)
        Log.d(TAG, "DatabaseProvider update:${uri.toString()} table:${table} selection:${selection} selectionArgs:${selectionArgs}")
        var index = selection?.toInt()!!
        userList.takeIf {
            it.size > index
        }.let {
            if(it != null) {
                it[index] = 5
                return 1
            }
        }
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        var table = getTableName(uri)
        Log.d(TAG, "DatabaseProvider delete:${uri.toString()} table:${table} selection:${selection} selectionArgs:${selectionArgs}")
        var index = selection?.toInt()!!
        userList.takeIf {
            it.size > index
        }.let {
            if(it != null) {
                it.removeAt(index)
                return 1
            }
        }
        return 0
    }

    override fun getType(uri: Uri): String? {
        Log.d(TAG, "DatabaseProvider getType uri:${uri}")
        return getTableName(uri)
    }


    /**
     * 根据URI匹配 URI_CODE，从而匹配ContentProvider中相应的表名
     */
    private fun getTableName(uri: Uri): String? {
        var tableName: String? = null
        when (mMatcher.match(uri)) {
            1 ->
                tableName = "user"
            2 ->
                tableName = "job"
        }
        return tableName;
    }

}