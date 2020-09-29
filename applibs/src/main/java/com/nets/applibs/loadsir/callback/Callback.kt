package com.nets.applibs.loadsir.callback

import android.content.Context
import android.view.View
import java.io.*

/**
 * Description:TODO
 * Create Time:2017/9/2 17:04
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
abstract class Callback : Serializable {
    var rootViews: View? = null
    var context: Context? = null
    var onViewListener: OnViewListener? = null
    var successVisible = false
    var type: CallBackType? = null

    constructor() {
        type = CallBackType.EMPTY
    }

    internal constructor(
        view: View?,
        context: Context?,
        onViewListener: OnViewListener?
    ) {
        rootViews = view
        this.context = context
        this.onViewListener = onViewListener
    }

    fun setCallback(
        view: View?,
        context: Context?,
        onViewListener: OnViewListener?
    ): Callback {
        rootViews = view
        this.context = context
        this.onViewListener = onViewListener
        return this
    }

    fun getRootView(): View? {
        if (rootViews == null) {
            rootViews = View.inflate(context, layout, null)
            onViewCreate(context, rootViews)
        }
        rootViews!!.setOnClickListener { v ->
            if (onViewListener != null) {
                onViewListener!!.onReload(v, type, 100, false)
            }
        }
        onViewBuild(context, rootViews)
        return rootViews
    }

    fun copy(): Callback? {
        val bao = ByteArrayOutputStream()
        val oos: ObjectOutputStream
        var obj: Any? = null
        try {
            oos = ObjectOutputStream(bao)
            oos.writeObject(this)
            oos.close()
            val bis = ByteArrayInputStream(bao.toByteArray())
            val ois = ObjectInputStream(bis)
            obj = ois.readObject()
            ois.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return obj as Callback?
    }

    fun obtainRootView(): View? {
        if (rootViews == null) {
            rootViews = getRootView()
        }
        return rootViews
    }

    interface OnViewListener : Serializable {
        fun onReload(
            v: View?,
            type: CallBackType?,
            stat: Int,
            isBnt: Boolean
        )

        val pageType: Int
        val isShowBnt: Boolean
        val callBackData: Any?
    }

    protected abstract val layout: Int
    open fun onViewCreate(context: Context?, view: View?) {}
    open fun onViewBuild(
        context: Context?,
        view: View?
    ) {
    }

    open fun onAttach(context: Context?, callBackType: CallBackType?) {}
    fun onDetach() {}
}