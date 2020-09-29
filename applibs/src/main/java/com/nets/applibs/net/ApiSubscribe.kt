package com.nets.applibs.net

import android.util.Log
import com.nets.applibs.until.AppLog
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

open class ApiSubscribe<T> : Subscriber<Result<T>> {
    var sub: Subscription? = null
      var start: () -> Unit = {listener?.start(tag)}// show loading dialog
      var subscribe: (Subscription?) -> Unit = {}//used to add Dispose into CompositeDispose
      var success: (Result<T>) -> Unit = {listener?.onNext(tag,it)}
     var end: () -> Unit = {listener?.finish(tag)}
     var fail: (ApiException?) -> Unit = {listener?.onFail(tag,it)}
     var tag:Any=""
     var listener: ApiCallListener?=null
    constructor()

    constructor(tag:Any,listener: ApiCallListener){
      this.tag=tag
      this.listener=listener
    }
    override fun onSubscribe(sub: Subscription?) {
        subscribe(sub)
        this.sub = sub
        sub?.request(1)
    }

    override fun onNext(t: Result<T>?) {
        Log.i("nets",t?.msg+"onNext")
        if (t != null) {
            success(t!!)
        } else {
            fail(ApiException(-3, "数据异常"))
        }
    }

    override fun onError(t: Throwable?) {
        Log.i("nets",t?.message+"onError")
        var ex = ApiException.handleException(t)
        fail(ex)
    }

    override fun onComplete() {
        end()
    }

    fun cancel():ApiSubscribe<T> {
        sub?.cancel()
        return this
    }

    fun onStart(start: () -> Unit):ApiSubscribe<T>{
        this.start = start
        return this
    }

    fun onSuccess(success: (Result<T>) -> Unit):ApiSubscribe<T> {
        this.success = success
        return this
    }
    fun onFail(fail: (ApiException?) -> Unit):ApiSubscribe<T> {
        this.fail = fail
        return this
    }
    fun onEnd(end: () -> Unit):ApiSubscribe<T> {
        this.end = end
        return this
    }
}