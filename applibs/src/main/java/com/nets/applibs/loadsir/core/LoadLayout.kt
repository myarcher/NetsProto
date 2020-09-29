package com.nets.applibs.loadsir.core

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import com.nets.applibs.loadsir.callback.CallBackType
import com.nets.applibs.loadsir.callback.Callback
import com.nets.applibs.loadsir.callback.Callback.OnViewListener
import com.nets.applibs.loadsir.callback.SuccessCallback
import java.util.*

/**
 * Description:TODO
 * Create Time:2017/9/2 17:02
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
class LoadLayout(context: Context) : FrameLayout(context) {
    private val callbacks: MutableMap<Class<out Callback?>, Callback> =
        HashMap()
    private var onReloadListener: OnViewListener? = null
    private var preType: CallBackType? = null
    var currentCallback: Class<out Callback?>? =
        null
        private set

    constructor(
        context: Context,
        onReloadListener: OnViewListener?
    ) : this(context) {
        this.onReloadListener = onReloadListener
    }

    fun setupSuccessLayout(callback: Callback) {
        addCallback(callback)
        val successView = callback.getRootView()
        successView!!.visibility = View.GONE
        addView(successView)
        currentCallback = SuccessCallback::class.java
    }

    fun setupCallback(callback: Callback?) {
        val cloneCallback = callback!!.copy()
        cloneCallback!!.setCallback(null, context, onReloadListener)
        addCallback(cloneCallback)
    }

    fun addCallback(callback: Callback) {
        if (!callbacks.containsKey(callback.javaClass)) {
            callbacks[callback.javaClass] = callback
        }
    }

    fun showCallback(
        callback: Class<out Callback?>,
        type: CallBackType?
    ) {
        checkCallbackExist(callback)
        if (LoadSirUtil.isMainThread) {
            showCallbackView(callback, type)
        } else {
            postToMainThread(callback, type)
        }
    }

    private fun postToMainThread(
        status: Class<out Callback?>,
        type: CallBackType?
    ) {
        post { showCallbackView(status, type) }
    }

    private fun showCallbackView(
        status: Class<out Callback?>,
        type: CallBackType?
    ) {
        if (preType == type) {
            return
        }
        if (currentCallback != null) {
            callbacks[currentCallback!!]!!.onDetach()
        }
        if (childCount > 1) {
            removeViewAt(CALLBACK_CUSTOM_INDEX)
        }
        for (key in callbacks.keys) {
            if (key == status) {
                val successCallback =
                    callbacks[SuccessCallback::class.java] as SuccessCallback?
                if (key == SuccessCallback::class.java) {
                    successCallback!!.show()
                } else {
                    successCallback!!.showWithCallback(callbacks[key]!!.successVisible)
                    val rootView = callbacks[key]!!.getRootView()
                    addView(rootView)
                    callbacks[key]!!.onAttach(context, type)
                }
                preType = type
            }
        }
        currentCallback = status
    }

    private fun checkCallbackExist(callback: Class<out Callback?>) {
        require(callbacks.containsKey(callback)) {
            String.format(
                "The Callback (%s) is nonexistent.", callback
                    .simpleName
            )
        }
    }

    companion object {
        private const val CALLBACK_CUSTOM_INDEX = 1
    }
}