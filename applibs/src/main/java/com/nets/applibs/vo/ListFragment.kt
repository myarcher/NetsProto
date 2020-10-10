package com.nets.applibs.vo

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.nets.applibs.R
import com.nets.applibs.adapter.BaseRvAdapter
import com.nets.applibs.adapter.Builder
import com.nets.applibs.adapter.IItemBind
import kotlinx.android.synthetic.main.baselist.*


abstract class ListFragment<T> : CustomFragment() {
    var mAdapter: BaseRvAdapter<T>? = null
    var manager: LinearLayoutManager? = null

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        manager = LinearLayoutManager(context)
        baselist_rv!!.layoutManager = manager
        mAdapter = initAdapter()
    }

    override fun setCustomset() {
        refreshLoad?.setEnableRefresh(true)
        refreshLoad?.setEnableLoadMore(true)
    }

    override fun isNeedRefload(): Boolean {
        return true
    }
     open fun getItemBind(): IItemBind<T>?{
         return null
     }
    open fun initAdapter(): BaseRvAdapter<T> {
        var itemBind=getItemBind()
        var itemId=getItemLayout()
        var build=  Builder<T>()
                .item(itemId)                          // 添加次序为 2
                .bind(itemBind)
        return BaseRvAdapter<T>(build,baselist_rv)
    }

    override val contentId: Int = R.layout.baselist

    open fun getItemLayout(): Int {
        return 0
    }




    override fun setPageInfo(data: Any) {
        super.setPageInfo(data)
        setAdapterList(1, data)
    }

    override fun setDataIndexSize(pageSize: Int, pIndex: Int, dataSize: Int, data: Any?) {
        super.setDataIndexSize(pageSize, pIndex, dataSize, data)
        setAdapterList(pIndex, data)
    }
    open  fun setList(count: Int?, dataList: MutableList<T>?) {
        var pageIndex= refreshLoad?.pageIndex ?:1
        var pagerCount=refreshLoad?.pagerCount?:10
        refreshLoad?.setDataIndexSizeNoMore(pagerCount,pageIndex,count?:0)
        setAdapterList(pageIndex,dataList)
    }

    open fun setAdapterList(pIndex: Int, data: Any?) {
        if (pIndex == 1) {
            mAdapter?.clear()
        }
        mAdapter?.addList(data as MutableList<T>?)
        mAdapter?.notifyDataSetChanged()
        isHashData = mAdapter!!.itemCount > 0
        showIsEmpty()
    }



}