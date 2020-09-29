package com.nets.applibs.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageView
import com.nets.applibs.R
import com.nets.applibs.listener.Indeterminate
import com.nets.applibs.until.BitmapUntil

@SuppressLint("AppCompatCustomView")
class SpinView : ImageView, Indeterminate {
    private var mRotateDegrees = 0f
    private var mFrameTime = 0
    private var mNeedToUpdateView = false
    private var mUpdateViewRunnable: Runnable? = null
    private var defaultRes = R.mipmap.kprogresshud_spinner

    constructor(context: Context) : super(context) {
        init(null, context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs, context)
    }

    private fun init(attrs: AttributeSet?, context: Context) {
        if (attrs != null) {
            val array = context.obtainStyledAttributes(attrs, R.styleable.SpinView)
            defaultRes = array.getResourceId(R.styleable.SpinView_res, defaultRes)
            array.recycle()
        }
        setImageDrawable(BitmapUntil.getBitmap(context, defaultRes))
    }

    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        setRun()
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        setRun()
    }

    private fun setRun() {
        mFrameTime = 1000 / 12
        mUpdateViewRunnable = object : Runnable {
            override fun run() {
                mRotateDegrees += 30f
                mRotateDegrees = if (mRotateDegrees < 360) mRotateDegrees else mRotateDegrees - 360
                invalidate()
                if (mNeedToUpdateView) {
                    postDelayed(this, mFrameTime.toLong())
                }
            }
        }
    }

    override fun setAnimationSpeed(scale: Float) {
        mFrameTime = (1000 / 12 / scale).toInt()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.rotate(mRotateDegrees, width / 2.toFloat(), height / 2.toFloat())
        super.onDraw(canvas)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mNeedToUpdateView = true
        post(mUpdateViewRunnable)
    }

    override fun onDetachedFromWindow() {
        mNeedToUpdateView = false
        super.onDetachedFromWindow()
    }
}