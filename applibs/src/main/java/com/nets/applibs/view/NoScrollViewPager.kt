package com.nets.applibs.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

/**
 * 提供禁止滑动功能的自定义ViewPager
 */
class NoScrollViewPager : ViewPager {
    private var noScroll = false

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
    }

    constructor(context: Context?) : super(context!!) {}

    fun setNoScroll(noScroll: Boolean) {
        this.noScroll = noScroll
    }

    override fun scrollTo(x: Int, y: Int) {
        super.scrollTo(x, y)
    }

    override fun onTouchEvent(arg0: MotionEvent): Boolean {
        return if (noScroll) {
            false
        } else {
            super.onTouchEvent(arg0)
        }
    }

    override fun canScrollHorizontally(direction: Int): Boolean {
        return if (noScroll) {
            false
        } else super.canScrollHorizontally(direction)
    }

    override fun onInterceptTouchEvent(arg0: MotionEvent): Boolean {
        return if (noScroll) {
            false
        } else {
            super.onInterceptTouchEvent(arg0)
        }
    }

    override fun setCurrentItem(item: Int, smoothScroll: Boolean) {
        super.setCurrentItem(item, smoothScroll)
    }

    override fun setCurrentItem(item: Int) {
        super.setCurrentItem(item)
    }
}