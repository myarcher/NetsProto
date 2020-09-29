package com.nets.applibs.glides

import android.widget.ImageView
import com.bumptech.glide.load.engine.DiskCacheStrategy

object SimpleLoader {
    private const val process_webpurl = "?x-oss-process=image/format,webp"
    var isFormatWebp = false

    fun loadBlur(view: ImageView, url: String, radius: Int, drawable: Int) {
        var urls = dealUrl(url)
        ImageLoader.loader?.showImage(view, urls, GlidOptions {
            diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            placeholder(drawable)
            error(drawable)
            skipMemoryCache(false)
            transform(BlurTransformation(radius))
        })
    }

    fun loadRadius(view: ImageView, url: String, radius: Int, drawable: Int) {
        var urls = dealUrl(url)
        ImageLoader.loader?.showImage(view, urls, GlidOptions {
            diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            placeholder(drawable)
            error(drawable)
            skipMemoryCache(false)
            transform(RoundedCornersTransform(radius,0))
        })
    }

    fun loadImage(view:ImageView, url: String?, drawable: Int, w: Int, h: Int) {
        var urls = dealUrl(url)
        ImageLoader.loader?.showImage(view, urls, GlidOptions {
            diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            placeholder(drawable)
            error(drawable)
            skipMemoryCache(false)
            override(w,h)
        })
    }

    fun loadImage(view: ImageView, url: String, drawable: Int) {
        loadImage(view, url, false, drawable)
    }

    fun loadImage(view: ImageView, url: String, isCircle: Boolean, drawable: Int) {
        var urls = dealUrl(url)
        ImageLoader.loader?.showImage(view, urls, GlidOptions {
            diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            placeholder(drawable)
            error(drawable)
            skipMemoryCache(false)
            if(isCircle){
                transform(CircleTransformation())
            }
        })
    }


    fun loadLocalImage(view: ImageView, resId: Int, drawableId: Int) {
        loadLocalImage(view, resId, false, drawableId)
    }

    fun loadLocalImage(view: ImageView, resId: Int, isCircle: Boolean, drawable: Int) {
        ImageLoader.loader?.showImage(view, resId, GlidOptions {
            diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            placeholder(drawable)
            error(drawable)
            skipMemoryCache(false)
            if(isCircle){
                transform(CircleTransformation())
            }
        })
    }



    private fun dealUrl(urls: String?): String {
        var urls = urls
        if (urls == null) {
            urls = ""
        }
        if (isFormatWebp) {
            urls += process_webpurl
        }
        return urls
    }
}