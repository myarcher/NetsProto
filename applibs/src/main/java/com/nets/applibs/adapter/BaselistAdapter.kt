package com.nets.applibs.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.nets.applibs.adapter.BHelper
import java.util.*

/**
 * @author admin
 */
open class BaselistAdapter<T> : BaseAdapter {
    protected var mContext: Context
    protected var mDatas: MutableList<T>?
    protected var layoutId: Int

    constructor(context: Context, layoutId: Int) : super() {
        mContext = context
        mDatas = ArrayList()
        this.layoutId = layoutId
    }

    constructor(
        context: Context,
        datas: MutableList<T>?,
        layoutId: Int
    ) {
        mContext = context
        mDatas = datas
        this.layoutId = layoutId
    }

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var viewHolder: BHelper? = null
        if (convertView == null) {
            viewHolder = BHelper.Companion.createViewHolder(mContext, parent, layoutId, position)
        } else {
            viewHolder = convertView.tag as BHelper
            viewHolder!!.mPosition = position
        }
        convert(viewHolder, getItem(position), position, count)
        return viewHolder.itemView
    }

     open fun convert(holder: BHelper?, item: T, position: Int, itemCount: Int) {
    }

    val list: MutableList<T>
        get() {
            if (mDatas == null) {
                mDatas = ArrayList()
            }
            return mDatas as MutableList<T>
        }

    fun setList(mList: MutableList<T>?) {
        if (mList != null) {
            mDatas = mList
        }
    }

    fun addItem(item: T?) {
        if (item != null) {
            mDatas!!.add(item)
        }
    }

    fun addList(mList: List<T>?) {
        if (mList != null) {
            mDatas!!.addAll(mList)
            notifyDataSetChanged()
        }
    }

    fun clear() {
        if (mDatas != null) {
            mDatas!!.clear()
        }
    }

    fun setItem(po: Int, item: T) {
        if (mDatas != null) {
            mDatas!![po] = item
        }
    }

    override fun getCount(): Int {
        return mDatas!!.size
    }

    override fun getItem(position: Int): T {
        return mDatas!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}