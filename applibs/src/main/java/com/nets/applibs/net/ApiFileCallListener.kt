package com.nets.applibs.net

import java.io.File

interface ApiFileCallListener {
    fun start(tag:Any)
    fun onNext(tag: Any, data: File)
    fun onProgress(fileSizeDownloaded:Long,totalSize:Long,filePath:String)
    fun onFail(tag: Any, ex: ApiException?)
    fun finish(tag:Any)
}