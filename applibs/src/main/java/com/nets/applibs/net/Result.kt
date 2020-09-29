package com.nets.applibs.net

import java.io.Serializable

class Result<D> : Serializable {
    var msg: String? = null
    var code = 0
    var data:D? = null
    var isShowStat=false

    constructor() {}
    constructor(msg: String?, code: Int, data: D) {
        this.msg = msg
        this.code = code
        this.data = data
    }

    fun setDatas(data: D) {
        this.data = data
    }

    override fun toString(): String {
        return "Result{" +
                ", msg='" + msg + '\'' +
                ", code=" + code +
                ", data=" + data +
                '}'
    }
}