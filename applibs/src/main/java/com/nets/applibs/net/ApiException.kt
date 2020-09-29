package com.nets.applibs.net

import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.Result

class ApiException : Exception {
    var statusCode = 0
    var statusDesc: String? = null

    constructor(statusCode: Int, statusDesc: String?) : super() {
        this.statusCode = statusCode
        this.statusDesc = statusDesc
    }

    companion object {
        fun handleException(e: Throwable?): ApiException? {
            var ex: ApiException? = null
            if (e is SocketTimeoutException) {
                ex = ApiException(-5, "请求数据超时")
            } else if (e is SocketException) {
                ex = ApiException(-4, "请求服务器异常")
            } else if (e is UnknownHostException) {
                ex = ApiException(-6, "网络连接错误")
            }else{
                ex = ApiException(-7, "系统异常，请稍后再试");
            }
            return ex
        }
    }

    override fun toString(): String {
        return "ApiException(statusCode=$statusCode, statusDesc=$statusDesc)"
    }

}