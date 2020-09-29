package com.nets.applibs.until

import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.nets.applibs.dialog.LoadDialog
import java.lang.ref.SoftReference

class LoadingDialogUntil private constructor(activity: FragmentActivity?) {
    private var activitySRF: SoftReference<FragmentActivity?>? = null
    private var dialog: LoadDialog? = null
    val isCanShow: Boolean
        get() = null != activitySRF && null != activitySRF!!.get() && !activitySRF!!.get()!!.isFinishing

    fun showDialog() {
        try {
            if (isCanShow) {
                if (dialog == null) {
                    dialog = LoadDialog()
                }
                val fm: FragmentManager = activitySRF!!.get()!!.supportFragmentManager
                fm.executePendingTransactions()
                if (!dialog!!.isAdded) {
                    dialog?.show(fm)
                }
            }
        } catch (e: Exception) {
        }
    }

    fun cancelDialog() {
        if (dialog != null && dialog!!.isShowing) {
            dialog?.dismiss()
            dialog = null
        }
    }

    companion object {
        operator fun get(activity: FragmentActivity?): LoadingDialogUntil {
            return LoadingDialogUntil(activity)
        }
    }

    init {
        activitySRF = SoftReference<FragmentActivity?>(activity)
    }
}