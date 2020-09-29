package com.nets.applibs.net

interface ApiRespListener {
    fun onStart(url: Any)
    fun onNext(url: Any, data: ResponseData)
    fun onFail(url: Any, data: ResponseData)
    fun onFinish(url: Any)
}