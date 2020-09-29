package com.nets.applibs.glides

import android.content.Context
import android.os.Looper
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder

class ImageLoader : ImImageLoader {
     fun <T> RequestBuilder<T>.loads(options: GlidOptions): RequestBuilder<T> {
        return apply(options)
    }
    override fun showImage(fragment: Fragment,url:Any,view:ImageView,options: GlidOptions) {
         GlideApp.with(fragment).asBitmap().load(url).loads(options).into(view)
    }

    override fun showImage(view:ImageView,url:Any,options: GlidOptions) {
        GlideApp.with(view).asBitmap().load(url).loads(options).into(view)
    }

    override fun showImage(context: Context,url:Any,view:ImageView,options: GlidOptions) {
        GlideApp.with(context).asBitmap().load(url).loads(options).into(view)
    }



    override fun cleanMemory(context: Context?) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            GlideApp.get(context!!).clearMemory()
        }
    }

    override fun pause(context: Context?) {
        Glide.with(context!!).pauseRequests()
    }

    override fun resume(context: Context?) {
        GlideApp.with(context!!).resumeRequests()
    }

    override fun init(context: Context?) {
        // 暂时不做配置
    }

    companion object {
        @JvmStatic
        var loader: ImageLoader? = null
            get() {
                if (field == null) {
                    synchronized(ImageLoader::class.java) {
                        if (field == null) {
                            field =
                                ImageLoader()
                        }
                    }
                }
                return field
            }
            private set
    }
}