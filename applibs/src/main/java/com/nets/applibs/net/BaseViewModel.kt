package  com.nets.applibs.net

import android.app.Application
import android.text.TextUtils
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.nets.applibs.AppManager
import com.nets.applibs.net.livedata.LiveDataManner
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableEmitter
import io.reactivex.FlowableOnSubscribe
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

open class BaseViewModel : AndroidViewModel, ApiRespListener {
    private var errData: MutableLiveData<ResponseData>? = null
    private var showDialogData: MutableLiveData<Int>? = null
    var context: Application? = null
    var liveDatas: LiveDataManner? = null
    private var emitter: FlowableEmitter<Any>? = null
    private var disposable: Disposable?=null
    constructor(context: Application) : super(context) {
        liveDatas = LiveDataManner()
        this.context = context
        errData = MutableLiveData()
        showDialogData = MutableLiveData()
        createTokenHandler()
    }



    private fun createTokenHandler() {
        disposable= Flowable.create(FlowableOnSubscribe<Any> {
            this.emitter = it
        }, BackpressureStrategy.ERROR)
            .take(120, TimeUnit.SECONDS)
            .subscribe {
                dealTokenErr()
            }

    }

    fun <T> request(): ApiSubscribe<T> {
        return AppSubscribe<T>().onToken { onTokenErr() }.onFail { errEd(it) }
    }


    fun getLiveData(): LiveDataManner {
        if (liveDatas == null) {
            liveDatas = LiveDataManner()
        }
        return liveDatas!!
    }

    fun getErrData(): MutableLiveData<ResponseData> {
        if (errData == null) {
            errData = MutableLiveData()
        }
        return errData!!
    }

    fun dialog(): MutableLiveData<Int> {
        if (showDialogData == null) {
            showDialogData = MutableLiveData()
        }
        return showDialogData!!
    }

    open fun errEd(err: ApiException?, isTrue: Boolean = false) {
        var da = ResponseData("", err)
        da.isShowStat = isTrue
        errEd(da)
    }

    open fun errEd(data: ResponseData) {
        getErrData().postValue(data)
    }


    fun showDialog() {
        dialog().postValue(1)
    }

    fun dissDialog() {
        dialog().postValue(2)
    }


   open fun toast(mess: String?) {
        if (!TextUtils.isEmpty(mess) && mess != "null") {
            Toast.makeText(context!!, mess, Toast.LENGTH_SHORT).show()
        }
    }


    override fun onStart(url: Any) {

    }

    override fun onNext(url: Any, data: ResponseData) {

    }

    override fun onFail(url: Any, data: ResponseData) {
        errEd(data)
    }

    override fun onFinish(url: Any) {
    }

     fun onTokenErr() {
        emitter?.onNext(1)
         emitter?.onComplete()
    }

    open fun dealTokenErr() {

    }


    override fun onCleared() {
        super.onCleared()
        liveDatas?.clear()
        liveDatas=null
        emitter=null
        disposable?.dispose()
        disposable=null
        errData=null
        showDialogData=null
    }

}