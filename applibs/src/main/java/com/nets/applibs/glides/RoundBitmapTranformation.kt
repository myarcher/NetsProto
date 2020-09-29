package com.nets.applibs.glides

import android.graphics.*
import androidx.annotation.NonNull
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

/**
 * author： Created by shiming on 2018/4/19 18:11
 * mailbox：lamshiming@sina.com
 */
class RoundBitmapTranformation(private val radius: Int) : BitmapTransformation() {
    override fun transform(
        @NonNull pool: BitmapPool,
        @NonNull toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        return roundCrop(pool, toTransform)
    }

    /**
     * Glide 使用 LruBitmapPool 作为默认的 BitmapPool 。
     * LruBitmapPool 是一个内存中的固定大小的 BitmapPool，
     * 使用 LRU 算法清理。默认大小基于设备的分辨率和密度，
     * 同时也考虑内存类和 isLowRamDevice 的返回值。
     * 具体的计算通过 Glide 的 MemorySizeCalculator 来完成，
     * 与 Glide 的 MemoryCache 的大小检测方法相似。
     * @param pool
     * @param toTransform
     * @return
     */
    private fun roundCrop(pool: BitmapPool, toTransform: Bitmap): Bitmap {
        val bw = toTransform.width
        val bh = toTransform.height


//        int width = toTransform.getWidth(); // 图片的宽
//        int height = toTransform.getHeight(); // 图片的高
//
//        if (width > height) {
//            // 如果图片的宽大于高，则以高为基准，以形状的宽高比重新设置宽度
//            width = (int) (height * (shapeWidth / shapeHeight));
//        } else {
//            // 如果图片的宽小于等于高，则以宽为基准，以形状的宽高比重新设置高度度
//            height = (int) (width * (shapeHeight / shapeWidth));
//        }

        //由于有这个对象，可以这样的获取尺寸，方便对图片的操作，和对垃圾的回收
        val target = pool[bw, bh, Bitmap.Config.ARGB_8888]
        val paint =
            Paint(Paint.ANTI_ALIAS_FLAG)
        paint.isAntiAlias = true
        val f = RectF(0f, 0f, bw.toFloat(), bh.toFloat())
        val canvas = Canvas(target)
        canvas.drawRoundRect(f, radius.toFloat(), radius.toFloat(), paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(toTransform, 0f, 0f, paint)
        return target
    }

    override fun updateDiskCacheKey(@NonNull messageDigest: MessageDigest) {}

}