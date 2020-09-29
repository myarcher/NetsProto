package com.nets.applibs.glides

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.RequestBuilder

interface ImImageLoader {
    fun showImage(fragment: Fragment,url:Any,view:ImageView,options: GlidOptions)
    fun showImage(view:ImageView,url:Any,options: GlidOptions)
    fun showImage(context: Context,url:Any,view:ImageView,options: GlidOptions)
    fun cleanMemory(context: Context?)
    fun pause(context: Context?)
    fun resume(context: Context?)
    fun init(context: Context?)
}