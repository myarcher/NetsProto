package com.nets.applibs.net

import androidx.lifecycle.MutableLiveData

class ApiLiveSubscribe<T> : ApiSubscribe<T>{
    constructor():super()
    constructor(tag:Any,listener: ApiCallListener):super(tag,listener)
    var interrupt: (ResponseData) -> Unit = {}// show loading dialog
    override fun onNext(t: Result<T>?) {
        if (t != null) {
            onDone(ResponseData(tag, t))
        } else {
            onError(ApiException(-3, "数据异常"))
        }
    }

    override fun onError(t: Throwable?) {
        var ex = ApiException.handleException(t)
        onDone(ResponseData(tag, ex))
    }

    private fun onDone(data: ResponseData) {
        interrupt(data)
        return MutableLiveData<ResponseData>().postValue(data)
    }
    fun onInterrupt(interrupt: (ResponseData) -> Unit):ApiSubscribe<T>{
        this.interrupt = interrupt
        return this
    }
}