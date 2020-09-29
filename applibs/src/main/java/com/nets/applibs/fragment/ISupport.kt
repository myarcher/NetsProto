package com.nets.applibs.fragment

import android.os.Bundle

interface ISupport {
    interface ISupportActivity {
        val transactionDelegate: TransactionDelegate?
    }

    interface ISupportFragment {
        val containerId: Int
        fun onLazyInitView(savedInstanceState: Bundle?)
        fun onSupportVisible()
        fun onSupportInvisible()
        val isSupportVisible: Boolean
        fun onBackPressedSupport(): Boolean
        val visibleDelegate: VisibleDelegate
    }
}