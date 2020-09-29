package com.nets.applibs.net

import android.util.Log
import com.nets.applibs.until.SpUtils
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import okio.IOException


class HttpParamInterceptor : Interceptor {

    constructor()

    override fun intercept(chain: Interceptor.Chain): Response {
        val oldRequest = chain.request()
        var newRequestBuild: Request.Builder? = oldRequest.newBuilder()
        val method: String = oldRequest.method
        var token = SpUtils.decodeString("token", "")

        if ("POST" == method) {
            val oldBody: RequestBody? = oldRequest.body
            if(oldBody is MultipartBody){
                val oldPartList = oldBody.parts
                val builder = MultipartBody.Builder()
                builder.setType(MultipartBody.FORM)
                for (part in oldPartList) {
                    builder.addPart(part);
                }
                builder.addFormDataPart("token",token)
                newRequestBuild = oldRequest.newBuilder()
                newRequestBuild.post(builder.build())
                Log.e("HttpParamInterceptor", "MultipartBody," + oldRequest.url)
            }else{
                val formBodyBuilder = FormBody.Builder()
                formBodyBuilder.add("token", token)
                newRequestBuild = oldRequest.newBuilder()
                val formBody: RequestBody = formBodyBuilder.build()
              var  postBodyString = bodyToString(oldRequest.body)
                postBodyString += (if (postBodyString!!.isNotEmpty()) "&" else "") + bodyToString(formBody)
                newRequestBuild.post(postBodyString.toRequestBody("application/x-www-form-urlencoded;charset=UTF-8".toMediaTypeOrNull()))
            }

        } else {

            // 添加新的参数
            val commonParamsUrlBuilder: HttpUrl.Builder = oldRequest.url
                .newBuilder()
                .scheme(oldRequest.url.scheme)
                .host(oldRequest.url.host)
                .addQueryParameter("token", token)
            newRequestBuild = oldRequest.newBuilder()
                .method(oldRequest.method, oldRequest.body)
                .url(commonParamsUrlBuilder.build())
        }
        val newRequest: Request = newRequestBuild!!.addHeader("Accept", "application/json")
            .addHeader("Accept-Language", "zh")
            .addHeader("token", token)
            .build()
        val startTime = System.currentTimeMillis()
        val response = chain.proceed(newRequest)
        val endTime = System.currentTimeMillis()
        val duration = endTime - startTime
        val mediaType = response.body!!.contentType()
        val content = response.body!!.string()
        val httpStatus = response.code
        val logSB = StringBuilder()
        logSB.append("-------start:$method|")
        logSB.append("$newRequest\n|")
        logSB.append("httpCode=$httpStatus;Response:$content\n|")
        logSB.append("----------End:" + duration + "毫秒----------")
      //  Log.i(TAG, logSB.toString())
        return response.newBuilder()
            .body(content.toResponseBody(mediaType))
            .build()
    }

    private fun bodyToString(request: RequestBody?): String? {
        return try {
            val buffer = Buffer()
            if (request != null) request.writeTo(buffer) else return ""
            buffer.readUtf8()
        } catch (e: IOException) {
            "did not work"
        }
    }
}

/*
if (oldBody is FormBody) {
                val formBodyBuilder = FormBody.Builder()
                formBodyBuilder.add("token", token)


                newRequestBuild = oldRequest.newBuilder()
                val formBody: RequestBody = formBodyBuilder.build()
                postBodyString = bodyToString(oldRequest.body)
                postBodyString += (if (postBodyString!!.isNotEmpty()) "&" else "") + bodyToString(formBody)
                newRequestBuild.post(postBodyString.toRequestBody("application/x-www-form-urlencoded;charset=UTF-8".toMediaTypeOrNull()))
            }
            /*else if (oldBody is MultipartBody) {

                val oldPartList =
                    oldBody.parts
                val builder = MultipartBody.Builder()
                builder.setType(MultipartBody.FORM)
                val requestBody1: RequestBody = token.toRequestBody("text/plain".toMediaTypeOrNull())
                for (part in oldPartList) {
                    builder.addPart(part);
                    postBodyString += (bodyToString(part.body) + "\n");
                }
                postBodyString += bodyToString(requestBody1).toString() + "\n"
                //              builder.addPart(oldBody);  //不能用这个方法，因为不知道oldBody的类型，可能是PartMap过来的，也可能是多个Part过来的，所以需要重新逐个加载进去
                builder.addPart(requestBody1)
                newRequestBuild = oldRequest.newBuilder()
                newRequestBuild.post(builder.build())
                Log.e("HttpParamInterceptor", "MultipartBody," + oldRequest.url)
            } else {
                val formBodyBuilder = FormBody.Builder()
                formBodyBuilder.add("token", token)
                val formBody: RequestBody = formBodyBuilder.build()
                postBodyString = bodyToString(oldRequest.body)
                postBodyString += (if (postBodyString!!.isNotEmpty()) "&" else "") + bodyToString(formBody)
                newRequestBuild = oldRequest.newBuilder()

             //   var jsons= "application/json; charset=utf-8".toMediaTypeOrNull()
             //   newRequestBuild.post(postBodyString.toRequestBody(jsons))
                newRequestBuild.post(postBodyString.toRequestBody("application/x-www-form-urlencoded;charset=UTF-8".toMediaTypeOrNull()))
            }*/
        } else {

            // 添加新的参数
            val commonParamsUrlBuilder: HttpUrl.Builder = oldRequest.url
                .newBuilder()
                .scheme(oldRequest.url.scheme)
                .host(oldRequest.url.host)
                .addQueryParameter("token", token)
            newRequestBuild = oldRequest.newBuilder()
                .method(oldRequest.method, oldRequest.body)
                .url(commonParamsUrlBuilder.build())
        }
 */