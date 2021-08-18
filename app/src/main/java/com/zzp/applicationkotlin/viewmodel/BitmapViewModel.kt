package com.zzp.applicationkotlin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 *
 * Created by samzhang on 2021/8/10.
 */
class BitmapViewModel :ViewModel(){
    var mutableLiveDatas = MutableLiveData<String>()

    fun getValue(): MutableLiveData<String> {
        return mutableLiveDatas
    }

    fun setValue(value: String) {
        mutableLiveDatas.value = value
    }
}