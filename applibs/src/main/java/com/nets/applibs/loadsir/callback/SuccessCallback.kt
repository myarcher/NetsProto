package com.nets.applibs.loadsir.callback

import android.content.Context
import android.view.View

/**
 * Description:TODO
 * Create Time:2017/9/4 10:22
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
class SuccessCallback(
    view: View?,
    context: Context?,
    onReloadListener: OnViewListener?
) : Callback(view, context, onReloadListener) {
    protected override val layout: Int
        protected get() = 0

    fun hide() {
        obtainRootView()!!.visibility = View.INVISIBLE
    }

    fun show() {
        obtainRootView()!!.visibility = View.VISIBLE
    }

    fun showWithCallback(successVisible: Boolean) {
        obtainRootView()!!.visibility = if (successVisible) View.VISIBLE else View.INVISIBLE
    }

    override fun onViewBuild(
        context: Context?,
        view: View?
    ) {
        rootViews!!.setOnClickListener(null)
    }

    init {
        type = CallBackType.SUCCESS
    }
}