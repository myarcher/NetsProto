package com.nets.applibs.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.EditText
import androidx.appcompat.widget.AppCompatEditText
import com.nets.applibs.R

/**
 * Created by Administrator on 2016/12/10.
 */
open class ClearEditText : AppCompatEditText, OnFocusChangeListener, TextWatcher {
    private var mClearDrawable: Drawable? = null
    private var hasFocus = false
    var w_h = 0

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init()
    }

    private fun init() {
        // getCompoundDrawables() Returns drawables for the left(0), top(1), right(2) and bottom(3)
        mClearDrawable = compoundDrawables[2] // 获取drawableRight
        if (mClearDrawable == null) {
            // 如果为空，即没有设置drawableRight，则使用R.mipmap.close这张图片
            mClearDrawable = resources.getDrawable(R.mipmap.delete)
        }
        //   w_h = (int) dpToPx(getContext(),30);
        w_h = resources.getDimensionPixelOffset(R.dimen.dimen_20px)
        mClearDrawable?.setBounds(0, 0, w_h, w_h)
        onFocusChangeListener = this
        addTextChangedListener(this)
        // 默认隐藏图标
        setDrawableVisible(false)
    }

    /**
     * 我们无法直接给EditText设置点击事件，只能通过按下的位置来模拟clear点击事件
     * 当我们按下的位置在图标包括图标到控件右边的间距范围内均算有效
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            if (compoundDrawables[2] != null) {
                val start = width - totalPaddingRight + paddingRight // 起始位置
                val end = width // 结束位置
                val available = event.x > start && event.x < end
                if (available) {
                    this.setText("")
                }
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onFocusChange(v: View, hasFocus: Boolean) {
        this.hasFocus = hasFocus
        var le=text?.length?:0
        if (hasFocus && le > 0) {
            setDrawableVisible(true) // 有焦点且有文字时显示图标
        } else {
            setDrawableVisible(false)
        }
    }

    override fun onTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        if (hasFocus) {
            setDrawableVisible(s.length > 0)
        }
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun afterTextChanged(s: Editable) {}
    private fun setDrawableVisible(visible: Boolean) {
        val right = if (visible) mClearDrawable else null
        setCompoundDrawables(
            compoundDrawables[0],
            compoundDrawables[1],
            right,
            compoundDrawables[3]
        )
    }
}