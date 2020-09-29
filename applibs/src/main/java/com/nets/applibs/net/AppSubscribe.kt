package  com.nets.applibs.net


class AppSubscribe<T> : ApiSubscribe<T> {
    var onTokenErr: (Any) -> Unit = {}

    constructor()
    constructor(tag: Any, listener: ApiCallListener) : super(tag, listener)


    override fun onNext(result: Result<T>?) {
        if (result != null) {
            if(result.code == 0) {
                success(result)
            } else if (result.code == 1001) {
                onTokenErr(tag)
            } else {
                fail(ApiException(result.code, result.msg))
            }
        } else {
            fail(ApiException(-3, "数据异常"))
        }
    }

    fun onToken(token: (Any) -> Unit): ApiSubscribe<T> {
        this.onTokenErr = token
        return this
    }


}