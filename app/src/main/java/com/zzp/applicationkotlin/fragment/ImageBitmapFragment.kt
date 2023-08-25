package com.zzp.applicationkotlin.fragment

import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.zzp.applicationkotlin.R
import com.zzp.applicationkotlin.viewmodel.BitmapViewModel
import kotlinx.android.synthetic.main.fragment_bitmap.*
import kotlinx.android.synthetic.main.fragment_image_bitmap.*

/**
 *
 * Created by samzhang on 2021/8/9.
 */
class ImageBitmapFragment :Fragment(){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var opts = BitmapFactory.Options()
        opts.inTargetDensity = 480 * 1080/720
        opts.inJustDecodeBounds = false

        var bitmap = BitmapFactory.decodeResource(resources,R.drawable.ic_agreement_third_share_list,opts)

        Log.d("zzp12345","width:${bitmap.width} height:${bitmap.height}")

        imageView.layoutParams.height = 1080 * bitmap.height/bitmap.width

        /*var matrix = Matrix()
        matrix.postScale(1080f/bitmap.width,1080f/bitmap.width)

        imageView.imageMatrix = matrix*/

        imageView.scaleType = ImageView.ScaleType.FIT_CENTER

        imageView.setImageBitmap(bitmap)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_image_bitmap,null)
    }

}