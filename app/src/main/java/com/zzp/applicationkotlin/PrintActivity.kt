package com.zzp.applicationkotlin

import android.content.Context
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintAttributes.COLOR_MODE_COLOR
import android.print.PrintDocumentAdapter
import android.print.PrintManager
import android.util.Log
import android.view.View
import androidx.print.PrintHelper

class PrintActivity : AppCompatActivity() {
    private val TAG = "_PrintActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_print)
    }

    fun doPrint(view:View){
        /*var printManager:PrintManager = getSystemService(Context.PRINT_SERVICE) as PrintManager

        val builder = PrintAttributes.Builder()
        builder.setColorMode(COLOR_MODE_COLOR)


        val attr = builder.build()

        printManager.print("666",object:PrintDocumentAdapter(){

            override fun onStart() {
                super.onStart()
                Log.d(TAG,"onStart")
            }

            override fun onFinish() {
                super.onFinish()
                Log.d(TAG,"onFinish")
            }

            override fun onLayout(
                oldAttributes: PrintAttributes?,
                newAttributes: PrintAttributes?,
                cancellationSignal: CancellationSignal?,
                callback: LayoutResultCallback?,
                extras: Bundle?
            ) {
            }

            override fun onWrite(
                pages: Array<out PageRange>?,
                destination: ParcelFileDescriptor?,
                cancellationSignal: CancellationSignal?,
                callback: WriteResultCallback?
            ) {
            }

        },attr)*/

        var helper = PrintHelper(this)
        helper.printBitmap("666",BitmapFactory.decodeResource(resources,R.drawable.ic_clamp_ship))
    }
}