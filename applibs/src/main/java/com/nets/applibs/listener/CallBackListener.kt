package com.nets.applibs.listener

interface CallBackListener {
    fun callBack(
        code: Long,
        stat: Long,
        value1: Any?,
        value2: Any?
    )
}