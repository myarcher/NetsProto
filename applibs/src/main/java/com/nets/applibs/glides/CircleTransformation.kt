package com.nets.applibs.glides

import android.graphics.Bitmap
import androidx.annotation.NonNull
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import java.security.MessageDigest

/**
 * Created by wuzhao on 2018/1/31 0031.
 */
class CircleTransformation : BitmapTransformation() {
    override fun updateDiskCacheKey(@NonNull messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)
    }

    override fun transform(
        @NonNull pool: BitmapPool,
        @NonNull toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        return TransformationUtils.circleCrop(pool, toTransform, outWidth, outHeight)
    }

    override fun equals(o: Any?): Boolean {
        return o is CircleTransformation
    }

    override fun hashCode(): Int {
        return ID.hashCode()
    }

    companion object {
        private val ID = CircleTransformation::class.java.name
        private val ID_BYTES =
            ID.toByteArray(Key.CHARSET)
    }
}