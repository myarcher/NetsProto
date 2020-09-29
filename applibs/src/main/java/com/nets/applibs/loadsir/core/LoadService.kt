package com.nets.applibs.loadsir.core

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.nets.applibs.loadsir.callback.CallBackType
import com.nets.applibs.loadsir.callback.Callback
import com.nets.applibs.loadsir.callback.Callback.OnViewListener
import com.nets.applibs.loadsir.callback.SuccessCallback

class LoadService<T> internal constructor(
    targetContext: TargetContext?, onReloadListener: OnViewListener?,
    builder: LoadSir.Builder
) {
    val loadLayout: LoadLayout
    private fun initCallback(builder: LoadSir.Builder) {
        val callbacks = builder.callbacks
        val defalutCallback =
            builder.defaultCallback
        val backType = builder.defaultCallType
        if (callbacks != null && callbacks.size > 0) {
            for (callback in callbacks) {
                loadLayout.setupCallback(callback)
            }
        }
        if (defalutCallback != null) {
            loadLayout.showCallback(defalutCallback, backType)
        }
    }

    fun showSuccess() {
        loadLayout.showCallback(SuccessCallback::class.java, CallBackType.SUCCESS)
    }

    fun showCallback(
        callback: Class<out Callback?>,
        type: CallBackType?
    ) {
        loadLayout.showCallback(callback, type)
    }

    val currentCallback: Class<out Callback?>?
        get() = loadLayout.currentCallback

    /**
     * obtain rootView if you want keep the toolbar in Fragment
     * @since 1.2.2
     */
    @Deprecated("")
    fun getTitleLoadLayout(
        context: Context?,
        rootView: ViewGroup,
        titleView: View?
    ): LinearLayout {
        val newRootView = LinearLayout(context)
        newRootView.orientation = LinearLayout.VERTICAL
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        newRootView.layoutParams = layoutParams
        rootView.removeView(titleView)
        newRootView.addView(titleView)
        newRootView.addView(loadLayout, layoutParams)
        return newRootView
    }

    init {
        val context = targetContext?.context
        val oldContent = targetContext?.oldContent
        val oldLayoutParams = oldContent!!.layoutParams
        loadLayout = LoadLayout(context!!, onReloadListener)
        loadLayout.setupSuccessLayout(SuccessCallback(oldContent, context, onReloadListener))
        if (targetContext.parentView != null) {
            targetContext.parentView.addView(loadLayout, targetContext.childIndex, oldLayoutParams)
        }
        initCallback(builder)
    }
}