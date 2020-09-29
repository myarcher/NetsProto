package com.nets.applibs.net

import java.io.Serializable

class ListBean<T> : Serializable {
    var current_page = 1
    var per_page = 20
    var total = 0
    var data: List<T>? = null

    constructor() {}
    constructor(
        pageIndex: Int,
        pageSize: Int,
        dataCount: Int,
        dataList: List<T>?
    ) {
        this.current_page = pageIndex
        this.per_page = pageSize
        this.total = dataCount
        this.data = dataList
    }
}