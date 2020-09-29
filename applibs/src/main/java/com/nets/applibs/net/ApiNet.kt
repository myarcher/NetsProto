package com.nets.applibs.net

import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import java.io.File

fun <T> Flowable<Result<T>>.request(subscriber: ApiSubscribe<T>) {
    doOnSubscribe { subscriber.start() }.compose(transformer())
        .subscribe(subscriber)
}

fun Flowable<ResponseBody>.request(subscriber: ApiFileSubscribe) {
    doOnSubscribe { subscriber.start() }.compose(transformer())
        .subscribe(subscriber)
}

fun <T> ApiSubscribe<T>.request(flowable: Flowable<Result<T>>) {
    flowable.doOnSubscribe { this.start() }.compose(transformer()).subscribe(this)
}

fun getUpParams(params: MutableMap<String, String>, filePaths: MutableList<String>): List<MultipartBody.Part> {
    var builder = MultipartBody.Builder()
        .setType(MultipartBody.FORM)
    //在这里添加服务器除了文件之外的其他参数
    for (key in params.keys) {
        builder.addFormDataPart(key, params[key]!!)
    }
    for (index in 0 until filePaths.size) {
        var file = File(filePaths[index])
        var imageBody =
            file.asRequestBody("multipart/form-data".toMediaTypeOrNull()); //添加文件(uploadfile就是你服务器中需要的文件参数)
        builder.addFormDataPart("files[$index]", file.name, imageBody);
    }
    return builder.build().parts
}

private fun <T> transformer(): FlowableTransformer<T, T> {
    var former = FlowableTransformer<T, T> { upstream ->
        upstream.subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    };
    return former
}
