package com.nets.applibs.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.nets.applibs.R

class LoadDialog : DialogFragment() {
    @Nullable
    override
    fun onCreateView(@NonNull inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.load_dialog, container, false)
    }

    override
    fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.loading_dialog)
        isCancelable = true
        if (dialog != null) {
            dialog?.setCanceledOnTouchOutside(false)
        }
    }

    override
    fun onStart() {
        super.onStart()
    }

    val isShowing: Boolean get() = this.isAdded && this.dialog != null && this.dialog?.isShowing ?:false

    fun show(fm: FragmentManager) {
        show(fm, "loadDialog")
    }

    override
    fun show(fm: FragmentManager, tag: String?) {
        try {
            val ft: FragmentTransaction = fm.beginTransaction()
            if (!this.isAdded()) {
                ft.remove(this)
            }
            ft.add(this, tag)
            ft.commitAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}