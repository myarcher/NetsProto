package com.nets.applibs.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.nets.applibs.R
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import java.text.SimpleDateFormat
import java.util.*

class RefreshLoadLayout : SmartRefreshLayout, OnRefreshListener, OnLoadMoreListener {
    private var refreshLoad = 0
     var pageIndex = 1
     var pagerCount = 10
     var dataSize = 20
    private var contentLayoutId = 0
    private var layoutListener: RefreshLoadListener? = null

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initViews()
    }

    private fun initViews() {
        LayoutInflater.from(context).inflate(R.layout.layout_base_refload, this)
        setEnableAutoLoadMore(false)
        setEnableNestedScroll(true)
        setEnableLoadMoreWhenContentNotFull(false)
        setEnableLoadMore(true)

    }
    override fun onFinishInflate() {
     super.onFinishInflate()
        initRefreshLoad()
    }
    private fun initRefreshLoad() {
        val delta = Random().nextInt(7 * 24 * 60 * 60 * 1000)
        val mClassicsHeader = refreshHeader as ClassicsHeader
        mClassicsHeader.setLastUpdateTime(Date(System.currentTimeMillis() - delta))
        mClassicsHeader.setTimeFormat(SimpleDateFormat("更新于 MM-dd HH:mm", Locale.CHINA))
        setEnableLoadMoreWhenContentNotFull(false)
        setOnRefreshListener(this)
        setOnLoadMoreListener(this)
    }

    fun setLayoutListener(layoutListener: RefreshLoadListener?) {
        this.layoutListener = layoutListener
        contentLayoutId = layoutListener?.contentId?: 0
        initContentView()
    }

    private fun initContentView() {
        if (contentLayoutId > 0) {
            val oldContent = LayoutInflater.from(context).inflate(contentLayoutId, null)
            setRefreshContent(oldContent)
        }
    }

    fun getContentLayoutId(): Int {
        return contentLayoutId
    }

    fun setContentLayoutId(contentLayoutId: Int) {
        this.contentLayoutId = contentLayoutId
        initContentView()
    }

    fun onRefreshLoad() {
        layoutListener?.onRefreshLoad(pageIndex, pagerCount)
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        refreshLoad = 1
        pageIndex = 1
        refreshLayout.setNoMoreData(false)
        onRefreshLoad()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        refreshLoad = 2
        pageIndex++
        onRefreshLoad()
    }
    fun isNotHasDataSize(): Boolean {
        return dataSize <= pageIndex * pagerCount
    }
    fun setDataIndexSize(pageSize: Int, pIndex: Int, dataSize: Int) {
        pagerCount = pageSize
        pageIndex = pIndex
        this.dataSize = dataSize
        finishRefreshLoaded()
    }

    fun setDataIndexSizeNoMore(pageSize: Int, pIndex: Int, dataSize: Int) {
        pagerCount = pageSize
        pageIndex = pIndex
        this.dataSize = dataSize
        finishRefreshLoad()
    }

    fun finishRefreshLoad() {
        if (refreshLoad == 1) {
            finishRefresh()
            setNoMoreData(false)
        } else if (refreshLoad == 2) {
            if (pageIndex * pagerCount >= dataSize) {
                finishLoadMoreWithNoMoreData()
            } else {
                finishLoadMore()
            }
        }
        refreshLoad = 0
    }

    fun finishRefreshLoaded() {
        if (refreshLoad == 1) {
            finishRefresh()
        } else if (refreshLoad == 2) {
            finishLoadMore()
        }
        refreshLoad = 0
    }


    fun setPageInfo(pageIndex: Int, pagerCount: Int, dataSize: Int): RefreshLoadLayout {
        this.pageIndex = pageIndex
        this.pagerCount = pagerCount
        this.dataSize = dataSize
        return this
    }

    fun setPageIndex(pageIndex: Int): RefreshLoadLayout {
        this.pageIndex = pageIndex
        return this
    }

    fun setPagerCount(pagerCount: Int): RefreshLoadLayout {
        this.pagerCount = pagerCount
        return this
    }

    fun setDataSize(dataSize: Int): RefreshLoadLayout {
        this.dataSize = dataSize
        return this
    }

    fun resetRefreshLoad() {
        pageIndex = 1
        setNoMoreData(false)
        onRefreshLoad()
    }

    fun reSetCanLoadMore() {
        setNoMoreData(false)
    }

}

interface RefreshLoadListener {
    val contentId:Int
    fun onRefreshLoad(pageIndex: Int, pagerCount: Int)
}