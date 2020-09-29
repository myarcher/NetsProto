package com.nets.applibs.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import com.nets.applibs.fragment.ISupport.ISupportFragment

class VisibleDelegate(private val mSupportF: ISupportFragment) {
    // SupportVisible相关
    var isSupportVisible = false
         set
    private var mNeedDispatch = true
    private var mInvisibleWhenLeave = false
    private var mIsFirstVisible = true
    private var mFirstCreateViewCompatReplace = true
    private var mHandler: Handler? = null
    private var mSaveInstanceState: Bundle? = null
    private val mFragment: Fragment
    fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            mSaveInstanceState = savedInstanceState
            // setUserVisibleHint() may be called before onCreate()
            mInvisibleWhenLeave =
                savedInstanceState.getBoolean(IS_INVISIBLE_WHEN_LEAVE)
            mFirstCreateViewCompatReplace =
                savedInstanceState.getBoolean(SAVE_COMPAT_REPLACE)
        }
    }

    fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(IS_INVISIBLE_WHEN_LEAVE, mInvisibleWhenLeave)
        outState.putBoolean(
            SAVE_COMPAT_REPLACE,
            mFirstCreateViewCompatReplace
        )
    }

    fun onActivityCreated(savedInstanceState: Bundle?) {
        if (!mFirstCreateViewCompatReplace && mFragment.tag != null && mFragment.tag!!
                .startsWith("android:switcher:")
        ) {
            return
        }
        if (mFirstCreateViewCompatReplace) {
            mFirstCreateViewCompatReplace = false
        }
        if (!mInvisibleWhenLeave && !mFragment.isHidden && mFragment.userVisibleHint) {
            if (mFragment.parentFragment != null && isFragmentVisible(mFragment.parentFragment)
                || mFragment.parentFragment == null
            ) {
                mNeedDispatch = false
                safeDispatchUserVisibleHint(true)
            }
        }
    }

    fun onResume() {
        if (!mIsFirstVisible) {
            if (!isSupportVisible && !mInvisibleWhenLeave && isFragmentVisible(mFragment)) {
                mNeedDispatch = false
                dispatchSupportVisible(true)
            }
        }
    }

    fun onPause() {
        if (isSupportVisible && isFragmentVisible(mFragment)) {
            mNeedDispatch = false
            mInvisibleWhenLeave = false
            dispatchSupportVisible(false)
        } else {
            mInvisibleWhenLeave = true
        }
    }

    fun onHiddenChanged(hidden: Boolean) {
        if (!hidden && !mFragment.isResumed) {
            //if fragment is shown but not resumed, ignore...
            mInvisibleWhenLeave = false
            return
        }
        if (hidden) {
            safeDispatchUserVisibleHint(false)
        } else {
            enqueueDispatchVisible()
        }
    }

    fun onDestroyView() {
        mIsFirstVisible = true
    }

    fun setUserVisibleHint(isVisibleToUser: Boolean) {
        if (mFragment.isResumed || !mFragment.isAdded && isVisibleToUser) {
            if (!isSupportVisible && isVisibleToUser) {
                safeDispatchUserVisibleHint(true)
            } else if (isSupportVisible && !isVisibleToUser) {
                dispatchSupportVisible(false)
            }
        }
    }

    private fun safeDispatchUserVisibleHint(visible: Boolean) {
        if (mIsFirstVisible) {
            if (!visible) {
                return
            }
            enqueueDispatchVisible()
        } else {
            dispatchSupportVisible(visible)
        }
    }

    private fun enqueueDispatchVisible() {
        handler.post { dispatchSupportVisible(true) }
    }

    private fun dispatchSupportVisible(visible: Boolean) {
        if (visible && isParentInvisible) {
            return
        }
        if (isSupportVisible == visible) {
            mNeedDispatch = true
            return
        }
        isSupportVisible = visible
        if (visible) {
            if (checkAddState()) {
                return
            }
            mSupportF.onSupportVisible()
            if (mIsFirstVisible) {
                mIsFirstVisible = false
                mSupportF.onLazyInitView(mSaveInstanceState)
            }
            dispatchChild(true)
        } else {
            dispatchChild(false)
            mSupportF.onSupportInvisible()
        }
    }

    private fun dispatchChild(visible: Boolean) {
        if (!mNeedDispatch) {
            mNeedDispatch = true
        } else {
            if (checkAddState()) {
                return
            }
            val fragmentManager =
                mFragment.childFragmentManager
            val childFragments =
                fragmentManager.fragments
            if (childFragments != null) {
                for (child in childFragments) {
                    if (child is ISupportFragment && !child.isHidden && child.userVisibleHint) {
                        (child as ISupportFragment).visibleDelegate
                            .dispatchSupportVisible(visible)
                    }
                }
            }
        }
    }

    private val isParentInvisible: Boolean
        private get() = try {
            val fragment = mFragment.parentFragment as ISupportFragment?
            fragment != null && !fragment.isSupportVisible
        } catch (e: Exception) {
            true
        }

    private fun checkAddState(): Boolean {
        if (!mFragment.isAdded) {
            isSupportVisible = !isSupportVisible
            return true
        }
        return false
    }

    private fun isFragmentVisible(fragment: Fragment?): Boolean {
        return !fragment!!.isHidden && fragment.userVisibleHint
    }

    private val handler: Handler
        private get() {
            if (mHandler == null) {
                mHandler = Handler(Looper.getMainLooper())
            }
            return mHandler!!
        }

    companion object {
        private const val IS_INVISIBLE_WHEN_LEAVE = "invisible_when_leave"
        private const val SAVE_COMPAT_REPLACE = "compat_replace"
    }

    init {
        mFragment = mSupportF as Fragment
    }
}