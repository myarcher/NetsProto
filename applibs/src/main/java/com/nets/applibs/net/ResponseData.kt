package com.nets.applibs.net

import java.io.Serializable

class ResponseData : Serializable {
    var tag: Any? = null
    var result: Result<*>? = null
    var ex: ApiException? = null
    var isShowStat = false
    var success = false

    constructor()
    constructor(tag: Any, result: Result<*>) {
        this.tag = tag
        this.result = result
        this.success = result.code == 0
    }

    constructor(tag: Any, ex: ApiException?) {
        this.tag = tag
        this.ex = ex
        this.success = false
    }

    fun <T> getData(): T {
        return result!!.data as T
    }

    fun getCode(): Int {
        return try {
            if (success) {
                result?.code ?: 0
            } else {
                ex?.statusCode ?: 0
            }
        } catch (e: Exception) {
           0
        }
    }

    fun getMess(): String {
        return try {
            if (success) {
                result?.msg ?: ""
            } else {
                ex?.statusDesc ?: ""
            }
        } catch (e: Exception) {
            ""
        }

    }

    fun isSuccess(): Boolean {
        return success
    }

    override fun toString(): String {
        return "ResponseData(tag=$tag, result=$result, ex=$ex, isShowStat=$isShowStat, success=$success)"
    }

}