package com.nets.applibs.vo

import android.annotation.TargetApi
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.LinearLayout
import android.text.TextUtils
import android.webkit.CookieSyncManager
import androidx.lifecycle.MutableLiveData
import com.nets.applibs.R
import com.nets.applibs.until.AppUntil
import com.nets.applibs.until.SpUtils


abstract class CustomWebFragment : BaseFragment() {
    private var webSettings: WebSettings? = null
    private var wv_detail: WebView? = null
    private var wv_detail_linear: LinearLayout? = null
    private var showDialogData: MutableLiveData<Int>? = null
    var isShowResume=false

   override
    fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        if(!isShowResume) {
            showWeb(wv_detail!!)
        }
    }

    fun getWeb(): WebView {
        return wv_detail!!
    }
    open fun  getShowWeb():WebView{
        return WebView(context?.applicationContext)
    }

    override val layoutId: Int= R.layout.fragment_detail_web


    override fun initView() {
        showDialogData = dialog()
        showDialogData?.observe(this,
            DialogObserver(activity!!)
        )
        wv_detail_linear = mView?.findViewById(R.id.wv_detail_linear)
        var lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        wv_detail = getShowWeb()
        wv_detail?.layoutParams = lp;
        wv_detail?.isHorizontalScrollBarEnabled = false;//水平不显示
        wv_detail?.isVerticalScrollBarEnabled = false; //垂直不显示
        wv_detail?.overScrollMode = View.OVER_SCROLL_NEVER
        wv_detail?.isFocusable = false
        webSettings = wv_detail?.settings
        webSettings!!.allowFileAccess = true
        webSettings!!.setSupportZoom(false)
        webSettings!!.setSupportMultipleWindows(true)
        webSettings!!.javaScriptEnabled = true
        webSettings!!.setAppCacheMaxSize(1024 * 1024 * 8)
        wv_detail?.isScrollContainer = false
        wv_detail?.isScrollbarFadingEnabled = false
        wv_detail?.scrollBarStyle = View.SCROLLBARS_OUTSIDE_OVERLAY
        webSettings!!.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        webSettings!!.cacheMode = WebSettings.LOAD_DEFAULT
        webSettings!!.javaScriptCanOpenWindowsAutomatically = true
        webSettings!!.domStorageEnabled = true
        webSettings!!.setGeolocationEnabled(true)
        webSettings!!.databaseEnabled = true
        webSettings!!.useWideViewPort = true
        webSettings!!.loadWithOverviewMode = true
        webSettings!!.displayZoomControls = false
        webSettings!!.blockNetworkImage = false
        webSettings!!.defaultTextEncodingName = "utf-8"
        webSettings!!.builtInZoomControls = true
        webSettings!!.loadsImagesAutomatically = true
        var  appCachePath = context!!.applicationContext.cacheDir.absolutePath
        webSettings!!.setAppCachePath(appCachePath)
        webSettings!!.allowFileAccess = true
        webSettings!!.setAppCacheEnabled(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings!!.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            CookieManager.getInstance().setAcceptThirdPartyCookies(wv_detail, true);
        }

        wv_detail!!.webViewClient = object : WebViewClient() {
            override
            fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    wv_detail!!.loadUrl(request.url.toString())
                } else {
                    wv_detail!!.loadUrl(request.toString())
                }
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                webSettings!!.blockNetworkImage = false
                pageFinished(view, url)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                onPageStart(view, url, favicon)
            }

            override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
                onReceivedErrored(view)
            }
            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                onReceivedErrored(view)
            }
            override fun onLoadResource(view: WebView?, url: String?) {
                super.onLoadResource(view, url)
            }

            override fun onReceivedHttpError(view: WebView?, request: WebResourceRequest?, errorResponse: WebResourceResponse?) {
                super.onReceivedHttpError(view, request, errorResponse)
            }
            override fun onRenderProcessGone(view: WebView?, detail: RenderProcessGoneDetail?): Boolean {
                return super.onRenderProcessGone(view, detail)
            }
            override
            fun shouldInterceptRequest(view: WebView, url: String): WebResourceResponse? {
                dealurl(url)
                return null
            }


            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
                if (request != null) {
                    dealurl("" + request.url.toString())
                }
                return null
            }

            override
            fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
                handler.proceed() // 接受所有网站的证书
            }
        }
        wv_detail!!.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                onProgressChang(view, newProgress)
            }

            override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                return onJsAlerted(view, url, message, result)
            }

            override fun onJsPrompt(view: WebView?, url: String?, message: String?, defaultValue: String?, result: JsPromptResult?): Boolean {
                return onJsPrompted(view, url, message, defaultValue, result)
            }

            override fun onJsConfirm(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                return onJsConfirmed(view, url, message, result)
            }
        }
        val list = ArrayList<String>()
        list.add("复制")
        wv_detail_linear?.addView(wv_detail)
    }

    open fun onReceivedErrored(view: WebView?) {
        showDialogData?.postValue(2)
    }

    open fun onProgressChang(view: WebView?, newProgress: Int) {
        if (newProgress != 100) {
            showDialogData?.postValue(1)
        } else {
            showDialogData?.postValue(2)
        }

    }


    open fun onPageStart(view: WebView?, url: String?, favicon: Bitmap?) {

    }

    open fun pageFinished(view: WebView, url: String) {

    }

    open fun onJsConfirmed(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
        return false
    }

    open fun onJsPrompted(view: WebView?, url: String?, message: String?, defaultValue: String?, result: JsPromptResult?): Boolean {
        return false
    }

    open fun onJsAlerted(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
        result?.cancel()
        return true
    }

    fun notShowTitleBar(view: WebView) {
        var javascript = "javascript:function hideOther() {" +
                "try{"+
                "var headers = document.getElementsByTagName('header');" +
                "var lastHeader = headers[headers.length-1];" +
                "lastHeader.remove();" +
                "var divs = document.getElementsByTagName('nav');" +
                "var lastDiv = divs[divs.length-1];" +
                "lastDiv.remove();" +
                "}catch(e){}"+
                "}"
        //创建方法
        view.loadUrl(javascript)

        //加载方法
        view.loadUrl("javascript:hideOther();")
    }

    abstract fun showWeb(wv: WebView)

    abstract fun dealurl(url: String, url_para: MutableMap<String, String>?)

    private fun dealurl(url: String) {
        Log.i("json", url)
        val url_para = AppUntil.getRequestParamMap(url)
        dealurl(url, url_para)
    }

    override fun onPause() {
        wv_detail?.onPause()
        wv_detail?.pauseTimers()
        super.onPause()
    }

    override fun onResume() {
        wv_detail?.onResume()
        wv_detail?.resumeTimers()
        if(isShowResume) {
            showWeb(wv_detail!!)
        }
        super.onResume()
    }

    private fun deleteView() {
        if (wv_detail != null) {
            // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再
            // destory()
            var parent = wv_detail!!.parent
            if (parent != null) {
                (parent as ViewGroup).removeView(wv_detail)
            }
            wv_detail!!.stopLoading()
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            wv_detail!!.settings.javaScriptEnabled = false;
            wv_detail!!.clearHistory()
            wv_detail!!.clearView()
            wv_detail!!.clearCache(true)
            wv_detail!!.freeMemory()
            wv_detail!!.removeAllViews()

            try {
                wv_detail!!.destroy()
                wv_detail = null
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }
    fun canGoBack():Boolean{
        return getWeb().canGoBack()
    }
    fun back() {
        getWeb().settings.cacheMode = WebSettings.LOAD_NO_CACHE
        getWeb().goBack()
    }

    override fun onDestroyView() {
        deleteView()
        super.onDestroyView()
    }
    fun dialog(): MutableLiveData<Int> {
        if (showDialogData == null) {
            showDialogData = MutableLiveData()
        }
        return showDialogData!!
    }

    fun syncCookie(url: String): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(context!!.applicationContext)
        }
        val cookieManager = CookieManager.getInstance()
        var token=SpUtils.decodeString("token", "")
        val cookieStr = "access_token="+token
        cookieManager.setCookie(url, cookieStr)
        if (Build.VERSION.SDK_INT < 21) {
            CookieSyncManager.getInstance().sync();
        } else {
            CookieManager.getInstance().flush();
        }
        val newCookie = cookieManager.getCookie(url)
        return if (TextUtils.isEmpty(newCookie)) false else true
    }

    fun stopSyncCookie(){
        if (Build.VERSION.SDK_INT < 21) {
            CookieSyncManager.getInstance().stopSync()
        } else {
            CookieManager.getInstance().removeAllCookies(null)
            CookieManager.getInstance().flush();
        }
    }

    fun writeData(webView: WebView) {
        val key = "access_token"
        var value= SpUtils.decodeString("token", "")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript("window.sessionStorage.setItem('$key','$value');", null)
        } else {
            webView.loadUrl("javascript:sessionStorage.setItem('$key','$value');")
            webView.reload();
        }
    }
}