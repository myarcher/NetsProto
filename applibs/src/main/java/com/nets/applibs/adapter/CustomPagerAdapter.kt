package com.nets.applibs.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter

abstract class CustomPagerAdapter : FragmentStatePagerAdapter {
    private var count: Int = 0
    constructor(fm: FragmentManager, count: Int) : super(fm) {
        this.count = count
    }

    override fun getItem(position: Int): Fragment {
        val bundle = Bundle()
        bundle.putInt("position", position)
        var fra = getNewFragment(position)
        fra.arguments = bundle
        return fra
    }

    abstract fun getNewFragment(position:Int): Fragment
    override fun getCount(): Int {
        return count
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

}