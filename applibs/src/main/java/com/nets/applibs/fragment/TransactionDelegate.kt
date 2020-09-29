package com.nets.applibs.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.nets.applibs.fragment.ISupport.ISupportFragment

class TransactionDelegate {
    private val mHandler: Handler
    fun loadRootTransaction(
        fm: FragmentManager,
        containerId: Int,
        to: ISupportFragment
    ) {
        enqueue(
            fm,
            object :
                Action(Action.Companion.ACTION_LOAD) {
                override fun run() {
                    bindContainerId(containerId, to)
                    val toFragmentTag = to.javaClass.name
                    start(fm, null, to, toFragmentTag, TYPE_REPLACE)
                }
            })
    }

    fun loadMultipleRootTransaction(
        fm: FragmentManager,
        containerId: Int,
        showPosition: Int,
        vararg tos: ISupportFragment?
    ) {
        enqueue(
            fm,
            object :
                Action(Action.Companion.ACTION_LOAD) {
                override fun run() {
                    val ft = fm.beginTransaction()
                    for (i in 0 until tos.size) {
                        val to =
                            tos[i] as Fragment
                        bindContainerId(containerId, tos[i])
                        val toName = to.javaClass.name
                        ft.add(containerId, to, toName)
                        if (i != showPosition) {
                            ft.hide(to)
                        }
                    }
                    supportCommit(fm, ft)
                }
            })
    }

    /**
     * Show showFragment then hide hideFragment
     */
    fun showHideFragment(
        fm: FragmentManager,
        showFragment: ISupportFragment,
        hideFragment: ISupportFragment?
    ) {
        enqueue(fm, object : Action() {
            override fun run() {
                doShowHideFragment(fm, showFragment, hideFragment)
            }
        })
    }

    /**
     * Dispatch the pop-event. Priority of the top of the stack of Fragment
     */
    fun dispatchBackPressedEvent(activeFragment: ISupportFragment?): Boolean {
        if (activeFragment != null) {
            val result = activeFragment.onBackPressedSupport()
            if (result) {
                return true
            }
            val parentFragment =
                (activeFragment as Fragment).parentFragment
            if (dispatchBackPressedEvent(parentFragment as ISupportFragment?)) {
                return true
            }
        }
        return false
    }

    private fun enqueue(
        fm: FragmentManager?,
        action: Action
    ) {
        if (fm == null) {
            return
        }
        runAction(action)
    }

    fun runAction(action: Action) {
        mHandler.postDelayed({ action.run() }, 100)
    }

    private fun start(
        fm: FragmentManager,
        from: ISupportFragment?,
        to: ISupportFragment,
        toFragmentTag: String,
        type: Int
    ) {
        val ft = fm.beginTransaction()
        val addMode =
            type == TYPE_ADD || type == TYPE_ADD_RESULT || type == TYPE_ADD_WITHOUT_HIDE || type == TYPE_ADD_RESULT_WITHOUT_HIDE
        val fromF = from as Fragment?
        val toF = to as Fragment
        val args = getArguments(toF)
        if (!addMode) {
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        }
        if (from == null) {
            ft.replace(args.getInt(CONTAINER_ID), toF, toFragmentTag)
            if (!addMode) {
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            }
        } else {
            if (addMode) {
                ft.add(from.containerId, toF, toFragmentTag)
                if (type != TYPE_ADD_WITHOUT_HIDE && type != TYPE_ADD_RESULT_WITHOUT_HIDE) {
                    ft.hide(fromF!!)
                }
            } else {
                ft.replace(from.containerId, toF, toFragmentTag)
            }
        }
        if (type != TYPE_REPLACE_DONT_BACK) {
            ft.addToBackStack(toFragmentTag)
        }
        supportCommit(fm, ft)
    }

    private fun doShowHideFragment(
        fm: FragmentManager,
        showFragment: ISupportFragment,
        hideFragment: ISupportFragment?
    ) {
        if (showFragment === hideFragment) {
            return
        }
        val ft =
            fm.beginTransaction().show((showFragment as Fragment))
        if (hideFragment == null) {
            val fragmentList =
                fm.fragments
            if (fragmentList != null) {
                for (fragment in fragmentList) {
                    if (fragment != null && fragment !== showFragment) {
                        ft.hide(fragment)
                    }
                }
            }
        } else {
            ft.hide((hideFragment as Fragment?)!!)
        }
        supportCommit(fm, ft)
    }

    private fun bindContainerId(containerId: Int, to: ISupportFragment?) {
        val args = getArguments(to as Fragment)
        args.putInt(CONTAINER_ID, containerId)
    }

    private fun getArguments(fragment: Fragment): Bundle {
        var bundle = fragment.arguments
        if (bundle == null) {
            bundle = Bundle()
            fragment.arguments = bundle
        }
        return bundle
    }

    private fun supportCommit(
        fm: FragmentManager,
        transaction: FragmentTransaction
    ) {
        transaction.commitAllowingStateLoss()
    }

    companion object {
        const val CONTAINER_ID = "fragment_container_id"
        const val IS_HIDDEN = "fragment_state_save_status"
        const val TYPE_ADD = 0
        const val TYPE_ADD_RESULT = 1
        const val TYPE_ADD_WITHOUT_HIDE = 2
        const val TYPE_ADD_RESULT_WITHOUT_HIDE = 3
        const val TYPE_REPLACE = 10
        const val TYPE_REPLACE_DONT_BACK = 11
    }

    init {
        mHandler = Handler(Looper.getMainLooper())
    }
}