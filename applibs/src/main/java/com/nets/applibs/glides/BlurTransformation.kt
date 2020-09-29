package com.nets.applibs.glides

import android.graphics.Bitmap
import androidx.annotation.NonNull
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

/**
 * Created by wuzhao on 2018/1/31 0031.
 */
class BlurTransformation(radius: Int) : BitmapTransformation() {
    private var defaultRadius = 15
    override fun updateDiskCacheKey(@NonNull messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)
    }

    override fun transform(
        @NonNull pool: BitmapPool,
        @NonNull toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap? {
        return BlurUtils.blur(toTransform, defaultRadius)
    }

    override fun equals(o: Any?): Boolean {
        return o is BlurTransformation
    }

    override fun hashCode(): Int {
        return ID.hashCode()
    }

    companion object {
        private val ID = BlurTransformation::class.java.name
        private val ID_BYTES =
            ID.toByteArray(Key.CHARSET)
    }

    init {
        defaultRadius = radius
    }
}