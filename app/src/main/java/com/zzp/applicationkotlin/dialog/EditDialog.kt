package com.zzp.applicationkotlin.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import android.view.WindowManager

import android.view.Gravity
import android.view.Window


class EditDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var edit = EditText(context)

        setContentView(edit)

        window?.let {
            it.setGravity(Gravity.BOTTOM)
            it.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

            val lp: WindowManager.LayoutParams = it.getAttributes()
            lp.verticalMargin = 0.1f

            it.attributes = lp
        }

    }
}