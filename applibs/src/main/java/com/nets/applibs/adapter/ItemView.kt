package com.nets.applibs.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
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
import androidx.recyclerview.widget.RecyclerView

/**
 * created by yhao on 2017/9/8.
 */
class ItemView internal constructor(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    private val mViews: SparseArray<View?>

    val context: Context
        get() = itemView.getContext()

    val resources: Resources
        get() = itemView.getContext().getResources()

    fun <T : View?> getView(viewId: Int): T? {
        var view = mViews[viewId]
        if (view == null) {
            view = itemView.findViewById(viewId)
            mViews.put(viewId, view)
        }
        return view as T?
    }

    fun setText(viewId: Int, text: String?): ItemView {
        val tv = getView<TextView>(viewId)!!
        tv.text = text
        return this
    }

    fun setFlags(viewId: Int): ItemView {
        val tv = getView<TextView>(viewId)!!
        tv.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
        return this
    }

    fun setImageResource(viewId: Int, resId: Int): ItemView {
        val view = getView<ImageView>(viewId)!!
        view.setImageResource(resId)
        return this
    }

    fun setImageBitmap(viewId: Int, bitmap: Bitmap?): ItemView {
        val view = getView<ImageView>(viewId)!!
        view.setImageBitmap(bitmap)
        return this
    }

    fun setImageDrawable(viewId: Int, drawable: Drawable?): ItemView {
        val view = getView<ImageView>(viewId)!!
        view.setImageDrawable(drawable)
        return this
    }

    fun setBackgroundColor(viewId: Int, color: Int): ItemView {
        val view = getView<View>(viewId)!!
        view.setBackgroundColor(color)
        return this
    }

    fun setBackgroundColorId(viewId: Int, color: Int): ItemView {
        val view = getView<View>(viewId)!!
        view.setBackgroundColor(ContextCompat.getColor(view.context, color))
        return this
    }

    fun setBackgroundRes(viewId: Int, backgroundRes: Int): ItemView {
        val view = getView<View>(viewId)!!
        view.setBackgroundResource(backgroundRes)
        return this
    }

    fun setTextColor(viewId: Int, textColor: Int): ItemView {
        val view = getView<TextView>(viewId)!!
        view.setTextColor(textColor)
        return this
    }

    fun setTextColorId(viewId: Int, textColor: Int): ItemView {
        val view = getView<TextView>(viewId)!!
        view.setTextColor(ContextCompat.getColor(view.context, textColor))
        return this
    }

    fun setTextColorRes(viewId: Int, textColorRes: Int): ItemView {
        val view = getView<TextView>(viewId)!!
        view.setTextColor(view.context.resources.getColor(textColorRes))
        return this
    }

    fun setTextSizeRes(viewId: Int, demin: Int): ItemView {
        val view = getView<TextView>(viewId)!!
        val size = view.context.resources.getDimension(demin)
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
        return this
    }

    fun setTextSize(viewId: Int, size: Float): ItemView {
        val view = getView<TextView>(viewId)!!
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
        return this
    }

    @SuppressLint("NewApi")
    fun setAlpha(viewId: Int, value: Float): ItemView {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getView<View>(viewId)!!.alpha = value
        } else {
            // Pre-honeycomb hack to set Alpha value
            val alpha = AlphaAnimation(value, value)
            alpha.duration = 0
            alpha.fillAfter = true
            getView<View>(viewId)!!.startAnimation(alpha)
        }
        return this
    }

    fun setVisible(viewId: Int, visible: Boolean): ItemView {
        val view = getView<View>(viewId)!!
        view.visibility = if (visible) View.VISIBLE else View.GONE
        return this
    }

    fun setVisible(viewId: Int, visible: Int): ItemView {
        val view = getView<View>(viewId)!!
        view.visibility = visible
        return this
    }

    fun linkify(viewId: Int): ItemView {
        val view = getView<TextView>(viewId)!!
        Linkify.addLinks(view, Linkify.ALL)
        return this
    }

    fun setTypeface(typeface: Typeface?, vararg viewIds: Int): ItemView {
        for (viewId in viewIds) {
            val view = getView<TextView>(viewId)!!
            view.typeface = typeface
            view.paintFlags = view.paintFlags or Paint.SUBPIXEL_TEXT_FLAG
        }
        return this
    }

    fun setProgress(viewId: Int, progress: Int): ItemView {
        val view = getView<ProgressBar>(viewId)!!
        view.progress = progress
        return this
    }

    fun setProgress(viewId: Int, progress: Int, max: Int): ItemView {
        val view = getView<ProgressBar>(viewId)!!
        view.max = max
        view.progress = progress
        return this
    }

    fun setMax(viewId: Int, max: Int): ItemView {
        val view = getView<ProgressBar>(viewId)!!
        view.max = max
        return this
    }

    fun setRating(viewId: Int, rating: Float): ItemView {
        val view = getView<RatingBar>(viewId)!!
        view.rating = rating
        return this
    }

    fun setRating(viewId: Int, rating: Float, max: Int): ItemView {
        val view = getView<RatingBar>(viewId)!!
        view.max = max
        view.rating = rating
        return this
    }

    fun setTag(viewId: Int, tag: Any?): ItemView {
        val view = getView<View>(viewId)!!
        view.tag = tag
        return this
    }

    fun setTag(viewId: Int, key: Int, tag: Any?): ItemView {
        val view = getView<View>(viewId)!!
        view.setTag(key, tag)
        return this
    }

    fun setChecked(viewId: Int, checked: Boolean): ItemView {
        val view = getView<View>(viewId) as Checkable
        view.isChecked = checked
        return this
    }

    /**
     * 关于事件的
     */
    fun setOnClickListener(
        viewId: Int,
        listener: View.OnClickListener?
    ): ItemView {
        val view = getView<View>(viewId)!!
        view.setOnClickListener(listener)
        return this
    }

    fun setOnTouchListener(
        viewId: Int,
        listener: OnTouchListener?
    ): ItemView {
        val view = getView<View>(viewId)!!
        view.setOnTouchListener(listener)
        return this
    }

    fun setOnLongClickListener(
        viewId: Int,
        listener: OnLongClickListener?
    ): ItemView {
        val view = getView<View>(viewId)!!
        view.setOnLongClickListener(listener)
        return this
    }

    companion object {
        fun create(
            context: Context,
            parent: ViewGroup,
            itemLayoutId: Int
        ): ItemView {
            val itemView =
                LayoutInflater.from(context).inflate(itemLayoutId, parent, false)
            return ItemView(itemView)
        }
    }

    init {
        mViews = SparseArray()
    }
}