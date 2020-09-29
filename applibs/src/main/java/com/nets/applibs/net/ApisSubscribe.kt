package com.nets.applibs.net

import com.nets.applibs.AppManager
import com.nets.applibs.until.AppUntil.isStrNull

class ApisSubscribe<T> : ApiSubscribe<T>, ApiCallListener {
    private var callListener: ApiRespListener? = null

    constructor()
    constructor(tag: Any, listener: ApiRespListener) : super() {
        this.tag = tag
        this.listener = this
        this.callListener = listener
    }

    override fun start(tag: Any) {
        callListener?.onStart(tag)
    }

    override fun <T> onNext(tag: Any, result: Result<T>) {
        var data = ResponseData(tag, result)
        if (data.success) {
            callListener?.onNext(tag, data)
        } else {
            callListener?.onFail(tag,data)
        }
    }

    override fun onFail(tag: Any, ex: ApiException?) {
        var data = ResponseData(tag, ex)
        callListener?.onFail(tag,data)
    }

    override fun finish(tag: Any) {
        callListener?.onFinish(tag)
    }

}