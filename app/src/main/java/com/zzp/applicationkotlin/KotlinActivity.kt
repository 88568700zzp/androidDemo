package com.zzp.applicationkotlin

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_kotlin.*
import kotlinx.coroutines.*

/**
 *
 * Created by samzhang on 2021/3/30.
 */
class KotlinActivity :AppCompatActivity(){

    private val TAG = "KotlinActivity_"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)
        Log.e(TAG, "onCreate")
        kotlin1.setOnClickListener {
            val job = GlobalScope.launch (Dispatchers.Main){
                Log.i(TAG, "协程执行结束 -- 线程id：${Thread.currentThread().name}")
            }
            Log.e(TAG, "主线程执行2 isCompleted:${job.isCompleted}")
        }
        kotlin2.setOnClickListener {
            val job = GlobalScope.launch (Dispatchers.IO){
                Log.i(TAG, "协程执行结束 -- 线程id：${Thread.currentThread().name}")
                withContext(Dispatchers.Main){
                    Log.i(TAG, "协程切换线程 -- 线程id：${Thread.currentThread().name}")
                }
            }
            Log.e(TAG, "主线程执行2 isActive:${job.isActive}")
        }
        block.setOnClickListener {
            runBlocking {
                repeat(8) {
                    delay(1000)
                    Log.e(TAG, "协程执行$it 线程id：${Thread.currentThread().name}")

                }
            }
        }
        async.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                Log.e(TAG,"begin thread:${Thread.currentThread().name}")
                val result1 = GlobalScope.async {
                    getResult1()
                }
                val result2 = GlobalScope.async {
                    getResult2()
                }
                val result = result1.await() + result2.await()
                Log.e(TAG,"result = $result thread:${Thread.currentThread().name}")
            }
        }

    }

    private suspend fun getResult1(): Int {
        Log.e(TAG,"getResult1 thread:${Thread.currentThread().name}")
        delay(3000)
        Log.i(TAG,"getResult1 thread:${Thread.currentThread().name}")
        return 1
    }

    private suspend fun getResult2(): Int {
        Log.e(TAG,"getResult2 thread:${Thread.currentThread().name}")
        delay(4000)
        Log.i(TAG,"getResult2 thread:${Thread.currentThread().name}")
        return 2
    }
}