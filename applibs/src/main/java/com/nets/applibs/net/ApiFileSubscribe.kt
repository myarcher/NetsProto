package com.nets.applibs.net

import android.os.Build
import okhttp3.ResponseBody
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class ApiFileSubscribe : Subscriber<ResponseBody> {
    var sub: Subscription? = null
    var start: () -> Unit = { listener?.start(tag) }// show loading dialog
    private var subscribe: (Subscription?) -> Unit = {}//used to add Dispose into CompositeDispose
    private var success: (File) -> Unit = {
        listener?.onNext(tag, it)
    }
    private var end: () -> Unit = { listener?.finish(tag) }
    private var fail: (ApiException?) -> Unit = { listener?.onFail(tag, it) }
    var tag: Any = ""
    var listener: ApiFileCallListener? = null


    var defalt_pth_ex = File.separator + "download" + File.separator //下载目标文件夹

    private var folder: String? = null//目标文件存储的文件夹路径
    private var fileName: String? = null //目标文件存储的文件名

    constructor() : this(null,null, null, null)

    constructor(filep:String,fileName:String){
        this.folder=filep+defalt_pth_ex
        this.fileName=fileName
    }


    constructor(fileFold:String?,fileName: String?, tag: Any?, listener: ApiFileCallListener?) {
        this.folder=fileFold
        this.fileName=fileName
        this.tag = tag ?: ""
        this.listener = listener
    }

    override fun onSubscribe(sub: Subscription?) {
        subscribe(sub)
        this.sub = sub
        sub?.request(1)
    }


    override fun onNext(t: ResponseBody?) {
        if (t != null) {
         writeFile(t)
        } else {
            fail(ApiException(-3, "数据异常"))
        }
    }


    private fun writeFile(body: ResponseBody) {
        var  dir =  File(folder);
        IOUtils.createFolder(dir);
        var timeStr: String=""
         if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
             timeStr=LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
        } else {
             val c = Calendar.getInstance()
             val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
             timeStr=dateFormat.format(c.time)
        }
        timeStr=fileName+"_"+timeStr
        var  file = File(dir, timeStr);
        IOUtils.delFileOrFolder(file);

        var  bodyStream: InputStream? = null;
        var  buffer =  ByteArray(8192)
        var  fileOutputStream: FileOutputStream?=null
        try {
            if (body == null) return

            bodyStream = body.byteStream();
            var totalSize = body.contentLength();
           var filePath = file.getAbsolutePath();
            var  fileSizeDownloaded:Long = 0;
            var  len:Int=0
            fileOutputStream =  FileOutputStream(file);
            while (len!= -1) {
                len= bodyStream.read(buffer);
                fileOutputStream.write(buffer, 0, len);
                fileSizeDownloaded += len
                listener?.onProgress(fileSizeDownloaded,totalSize,filePath)
            }
            fileOutputStream.flush();
            success(file)
        }catch (e:Exception){
            onError(e)
        } finally {
            IOUtils.closeQuietly(bodyStream);
            IOUtils.closeQuietly(fileOutputStream);
        }
    }


    override fun onError(t: Throwable?) {
        var ex = ApiException.handleException(t)
        fail(ex)
    }

    override fun onComplete() {
        end()
    }

    fun cancel(): ApiFileSubscribe {
        sub?.cancel()
        return this
    }

    fun onStart(start: () -> Unit): ApiFileSubscribe {
        this.start = start
        return this
    }

    fun onSuccess(success: (File) -> Unit): ApiFileSubscribe {
        this.success = success
        return this
    }

    fun onFail(fail: (ApiException?) -> Unit): ApiFileSubscribe {
        this.fail = fail
        return this
    }

    fun onEnd(end: () -> Unit): ApiFileSubscribe {
        this.end = end
        return this
    }


}