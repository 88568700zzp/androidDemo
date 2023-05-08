package com.zzp.applicationkotlin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 *
 * Created by samzhang on 2021/5/7.
 */
class RoomViewModel: ViewModel() {
    var mutableLiveDatas = MutableLiveData<String>()

    var netData = MutableLiveData<String>()

    fun getValue(): MutableLiveData<String> {
        return mutableLiveDatas
    }

    fun setValue(value: String) {
        mutableLiveDatas.setValue(value)
    }
}