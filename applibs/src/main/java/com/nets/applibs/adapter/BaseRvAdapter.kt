package com.nets.applibs.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import com.nets.applibs.adapter.Builder as Builder

open class BaseRvAdapter<T> : RecyclerView.Adapter<ItemView> {
    private var mData: MutableList<T>?
    protected var mContext: Context? = null
    private var mSlideItems: List<Int>?
    private var mIItemBind: IItemBind<T>? = null
    private var mIItemType: IItemType<T>? = null
    protected var layoutId = 0
    private var mRecycleView: RecyclerView? = null

    constructor(context: Context?) {
        mContext = context
        mData = ArrayList()
        mSlideItems = ArrayList()
    }

    constructor(context: Context?, layoutId: Int) : super() {
        mContext = context
        mData = ArrayList()
        this.layoutId = layoutId
        mSlideItems = ArrayList()
    }

    constructor(context: Context?,
        datas: MutableList<T>?,
        layoutId: Int
    ) {
        mContext = context
        mData = datas
        this.layoutId = layoutId
        mSlideItems = ArrayList()
    }

    constructor(build: Builder<T>?, recyclerView: RecyclerView?) {
        mSlideItems = build!!.slideItems
        mIItemBind = build.itemBind
        mIItemType = build.itemType
        mData = build.data
        mRecycleView = recyclerView
        init()
    }

    fun setBuild(
        build: Builder<T>,
        recyclerView: RecyclerView?
    ) {
        mSlideItems = build.slideItems
        mIItemBind = build.itemBind
        mIItemType = build.itemType
        mData = build.data
        mRecycleView = recyclerView
        init()
    }

    private fun init() {
        mRecycleView!!.adapter = this
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemView {
        return if (layoutId > 0) {
            ItemView.Companion.create(
                parent.context,
                parent,
                layoutId
            )
        } else {
            ItemView.Companion.create(
                parent.context,
                parent,
                mSlideItems!![viewType - 1]
            )
        }
    }

    override fun onBindViewHolder(
        holder: ItemView,
        position: Int
    ) {
        val itemData = getItem(position)
        val count = itemCount
        if (mIItemBind != null) {
            mIItemBind?.bind(holder, itemData, position, count)
        } else {
            convert(holder, itemData, position, count)
        }
    }

    override fun onBindViewHolder(
        holder: ItemView,
        position: Int,
        payloads: List<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
    }

    open fun convert(
        holder: ItemView?,
        item: T,
        position: Int,
        itemCount: Int
    ) {
    }

    override fun getItemViewType(position: Int): Int {
        return if (mIItemType == null || mSlideItems!!.size == 1) 1 else mIItemType!!.type(
            mData!![position], position
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    val list: List<T>
        get() {
            if (mData == null) {
                mData = ArrayList()
            }
            return mData!!
        }

    fun setList(mList: MutableList<T>?) {
        if (mList != null) {
            mData = mList
        }
    }

    fun addItem(item: T?) {
        if (item != null) {
            mData!!.add(item)
        }
    }

    fun addItem(position: Int, item: T?) {
        if (item != null) {
            mData!!.add(position, item)
        }
    }

    fun addList(mList: List<T>?) {
        if (mList != null) {
            mData!!.addAll(mList)
        }
    }

    fun clear() {
        if (mData != null) {
            mData?.clear()
        }
    }

    fun setItem(po: Int, item: T) {
        if (mData != null) {
            mData?.set(po, item)
        }
    }

    fun getItem(position: Int): T {
        return list[position]
    }
    fun removeItem(position: Int){
        if (mData != null) {
            mData?.removeAt(position)
        }
    }

}

class Builder<T> {
    var data: MutableList<T>
    var slideItems: MutableList<Int>? = null
    var itemBind: IItemBind<T>? = null
    var itemType: IItemType<T>? = null
    var itemPadding = 0
    private var mBuilder: Builder<T>? = null
    init {
        data = ArrayList<T>()
    }

    fun load(data: MutableList<T>): Builder<T> {
        this.data = data
        return this
    }

    fun item(itemLayoutId: Int): Builder<T> {
        if (slideItems == null) {
            slideItems = ArrayList()
        }
        slideItems!!.add(itemLayoutId)
        return this
    }

    fun padding(itemPadding: Int): Builder<T> {
        this.itemPadding = itemPadding
        return this
    }

    fun bind(itemBind: IItemBind<T>?): Builder<T> {
        this.itemBind = itemBind
        return this
    }

    fun type(itemType: IItemType<T>): Builder<T> {
        this.itemType = itemType
        return this
    }

    fun into(recyclerView: RecyclerView): BaseRvAdapter<T> {
        val adapter: BaseRvAdapter<T> = BaseRvAdapter(mBuilder, recyclerView)
        mBuilder = null
        return adapter
    }


}