package com.nets.applibs.net

interface ApiCallListener {
    fun start(tag:Any)
    fun <T>onNext(tag: Any, data: Result<T>)
    fun onFail(tag: Any, ex: ApiException?)
    fun finish(tag:Any)
}