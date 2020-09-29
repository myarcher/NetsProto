package com.nets.applibs.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.util.Linkify
import android.util.SparseArray
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.*
import androidx.core.content.ContextCompat

/**
 * @author
 * @version 1.0
 * @date 2017/8/21
 */
/**
 * 自定义的viewholder，用于处理列表的item
 */
class BHelper private constructor(
    var mActivity: Context,
    val itemView: View,
    var mPosition: Int
) {
    private val mViews: SparseArray<View?>
    var layoutid = 0

    private constructor(context: Context, layoutId: Int, position: Int) : this(
        context,
        LayoutInflater.from(context).inflate(layoutId, null),
        position
    ) {
    }

    /**
     * 通过viewId获取控件
     *
     * @param viewId
     * @return
     */
    fun <T : View?> getV(viewId: Int): T? {
        var view = mViews[viewId]
        if (view == null) {
            view = itemView.findViewById(viewId)
            mViews.put(viewId, view)
        }
        return view as T?
    }

    /****以下为辅助方法 */
    /**
     * 设置TextView的值
     *
     * @param viewId
     * @param text
     * @return
     */
    fun setText(viewId: Int, text: String?): BHelper {
        val tv = getV<TextView>(viewId)!!
        tv.text = text
        return this
    }

    fun setFlags(viewId: Int): BHelper {
        val tv = getV<TextView>(viewId)!!
        tv.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
        return this
    }

    fun setImageResource(viewId: Int, resId: Int): BHelper {
        val view = getV<ImageView>(viewId)!!
        view.setImageResource(resId)
        return this
    }

    fun setImageBitmap(viewId: Int, bitmap: Bitmap?): BHelper {
        val view = getV<ImageView>(viewId)!!
        view.setImageBitmap(bitmap)
        return this
    }

    fun setImageDrawable(viewId: Int, drawable: Drawable?): BHelper {
        val view = getV<ImageView>(viewId)!!
        view.setImageDrawable(drawable)
        return this
    }

    fun setBackgroundColor(viewId: Int, color: Int): BHelper {
        val view = getV<View>(viewId)!!
        view.setBackgroundColor(color)
        return this
    }

    fun setBackgroundColorId(viewId: Int, color: Int): BHelper {
        val view = getV<View>(viewId)!!
        view.setBackgroundColor(ContextCompat.getColor(mActivity, color))
        return this
    }

    fun setBackgroundRes(viewId: Int, backgroundRes: Int): BHelper {
        val view = getV<View>(viewId)!!
        view.setBackgroundResource(backgroundRes)
        return this
    }

    fun setTextColor(viewId: Int, textColor: Int): BHelper {
        val view = getV<TextView>(viewId)!!
        view.setTextColor(textColor)
        return this
    }

    fun setTextColorId(viewId: Int, textColor: Int): BHelper {
        val view = getV<TextView>(viewId)!!
        view.setTextColor(ContextCompat.getColor(mActivity, textColor))
        return this
    }

    fun setTextColorRes(viewId: Int, textColorRes: Int): BHelper {
        val view = getV<TextView>(viewId)!!
        view.setTextColor(mActivity.resources.getColor(textColorRes))
        return this
    }

    fun setTextSizeRes(viewId: Int, demin: Int): BHelper {
        val size = mActivity.resources.getDimension(demin)
        val view = getV<TextView>(viewId)!!
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
        return this
    }

    fun setTextSize(viewId: Int, size: Float): BHelper {
        val view = getV<TextView>(viewId)!!
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
        return this
    }

    @SuppressLint("NewApi")
    fun setAlpha(viewId: Int, value: Float): BHelper {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getV<View>(viewId)!!.alpha = value
        } else {
            // Pre-honeycomb hack to set Alpha value
            val alpha = AlphaAnimation(value, value)
            alpha.duration = 0
            alpha.fillAfter = true
            getV<View>(viewId)!!.startAnimation(alpha)
        }
        return this
    }

    fun setVisible(viewId: Int, visible: Boolean): BHelper {
        val view = getV<View>(viewId)!!
        view.visibility = if (visible) View.VISIBLE else View.GONE
        return this
    }

    fun setVisible(viewId: Int, visible: Int): BHelper {
        val view = getV<View>(viewId)!!
        view.visibility = visible
        return this
    }

    fun linkify(viewId: Int): BHelper {
        val view = getV<TextView>(viewId)!!
        Linkify.addLinks(view, Linkify.ALL)
        return this
    }

    fun setTypeface(typeface: Typeface?, vararg viewIds: Int): BHelper {
        for (viewId in viewIds) {
            val view = getV<TextView>(viewId)!!
            view.typeface = typeface
            view.paintFlags = view.paintFlags or Paint.SUBPIXEL_TEXT_FLAG
        }
        return this
    }

    fun setProgress(viewId: Int, progress: Int): BHelper {
        val view = getV<ProgressBar>(viewId)!!
        view.progress = progress
        return this
    }

    fun setProgress(viewId: Int, progress: Int, max: Int): BHelper {
        val view = getV<ProgressBar>(viewId)!!
        view.max = max
        view.progress = progress
        return this
    }

    fun setMax(viewId: Int, max: Int): BHelper {
        val view = getV<ProgressBar>(viewId)!!
        view.max = max
        return this
    }

    fun setRating(viewId: Int, rating: Float): BHelper {
        val view = getV<RatingBar>(viewId)!!
        view.rating = rating
        return this
    }

    fun setRating(viewId: Int, rating: Float, max: Int): BHelper {
        val view = getV<RatingBar>(viewId)!!
        view.max = max
        view.rating = rating
        return this
    }

    fun setTag(viewId: Int, tag: Any?): BHelper {
        val view = getV<View>(viewId)!!
        view.tag = tag
        return this
    }

    fun setTag(viewId: Int, key: Int, tag: Any?): BHelper {
        val view = getV<View>(viewId)!!
        view.setTag(key, tag)
        return this
    }

    fun setChecked(viewId: Int, checked: Boolean): BHelper {
        val view = getV<View>(viewId) as Checkable
        view.isChecked = checked
        return this
    }

    /**
     * 关于事件的
     */
    fun setOnClickListener(viewId: Int, listener: View.OnClickListener?): BHelper {
        val view = getV<View>(viewId)!!
        view.setOnClickListener(listener)
        return this
    }

    fun setOnTouchListener(viewId: Int, listener: OnTouchListener?): BHelper {
        val view = getV<View>(viewId)!!
        view.setOnTouchListener(listener)
        return this
    }

    fun setOnLongClickListener(viewId: Int, listener: OnLongClickListener?): BHelper {
        val view = getV<View>(viewId)!!
        view.setOnLongClickListener(listener)
        return this
    }

    companion object {
        operator fun get(
            context: Context,
            convertView: View?,
            parent: ViewGroup?,
            layoutId: Int,
            position: Int
        ): BHelper {
            return if (convertView == null) {
                createViewHolder(context, parent, layoutId, position)
            } else {
                val holder = convertView.tag as BHelper
                holder.mPosition = position
                holder
            }
        }

        fun createViewHolder(
            context: Context,
            itemView: View,
            position: Int
        ): BHelper {
            return BHelper(context, itemView, position)
        }

        fun createViewHolder(
            context: Context,
            parent: ViewGroup?,
            layoutId: Int,
            position: Int
        ): BHelper {
            val itemView =
                LayoutInflater.from(context).inflate(layoutId, parent, false)
            val holder = BHelper(context, itemView, position)
            holder.layoutid = layoutId
            return holder
        }
    }

    init {
        mViews = SparseArray()
        itemView.tag = this
    }
}