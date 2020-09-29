package com.nets.applibs.vo

import android.R
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.nets.applibs.fragment.ISupport.ISupportActivity
import com.nets.applibs.fragment.ISupport.ISupportFragment
import com.nets.applibs.fragment.SupportHelper
import com.nets.applibs.fragment.TransactionDelegate
import com.nets.applibs.fragment.VisibleDelegate

abstract class BaseFragment : Fragment(), ISupportFragment {
    var mView: View? = null
    private val mFirstCreateView = true
    private var mIsHidden = true
    override var containerId = 0
    private var mVisibleDelegate: VisibleDelegate? = null
    protected var _mActivity: FragmentActivity? = null
    private var mSupport: ISupportActivity? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    open fun initData(savedInstanceState: Bundle?) {}
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        visibleDelegate?.onActivityCreated(savedInstanceState)
        val view = view
        if (view != null) {
            view.isClickable = true
            setBackground(view)
        }
        initData(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mView != null) {
            reMoveOld()
        } else {
            if (layoutId > 0) {
                mView = inflater.inflate(layoutId, container, false)
            }
        }
        initView()
        return mView
    }

    fun reMoveOld() {
        val parent = mView!!.parent as ViewGroup
        parent?.removeView(mView)
    }

    protected abstract val layoutId: Int
    open fun initView() { //初始化数据
    }

    protected val isImmersionBarEnabled: Boolean
        protected get() = true

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            visibleDelegate?.onCreate(savedInstanceState)
            val bundle = arguments
            if (bundle != null) {
                containerId = bundle.getInt(TransactionDelegate.CONTAINER_ID)
            }
            if (savedInstanceState != null) {
                mIsHidden = savedInstanceState.getBoolean(TransactionDelegate.IS_HIDDEN)
                containerId = savedInstanceState.getInt(TransactionDelegate.CONTAINER_ID)
            }
            processRestoreInstanceState(savedInstanceState)
        } catch (e: Exception) {
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ISupportActivity) {
            mSupport = context
            _mActivity = context as FragmentActivity
        } else {
            throw RuntimeException(context.javaClass.simpleName + " must impl ISupportActivity!")
        }
    }

    override fun onDetach() {
        _mActivity = null
        super.onDetach()
    }

    private fun processRestoreInstanceState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            val ft = fragmentManager!!.beginTransaction()
            if (mIsHidden) {
                ft.hide(this)
            } else {
                ft.show(this)
            }
            ft.commitAllowingStateLoss()
        }
    }

    fun setBackground(view: View) {
        val isTrue = tag != null && tag!!.startsWith("android:switcher:") || view.background != null
        if (isTrue) {
            return
        }
        val background = windowBackground
        view.setBackgroundResource(background)
    }

    private val windowBackground: Int
        private get() {
            val a = _mActivity!!.theme.obtainStyledAttributes(
                intArrayOf(
                    R.attr.windowBackground
                )
            )
            val background = a.getResourceId(0, 0)
            a.recycle()
            return background
        }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        visibleDelegate?.onSaveInstanceState(outState)
        outState.putBoolean(TransactionDelegate.IS_HIDDEN, isHidden)
        outState.putInt(TransactionDelegate.CONTAINER_ID, containerId)
    }

    override fun onResume() {
        super.onResume()
        visibleDelegate?.onResume()
    }

    override fun onPause() {
        super.onPause()
        visibleDelegate?.onPause()
    }

    override fun onDestroyView() {
        visibleDelegate?.onDestroyView()
        super.onDestroyView()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        visibleDelegate?.onHiddenChanged(hidden)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        visibleDelegate?.setUserVisibleHint(isVisibleToUser)
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {}
    override fun onSupportVisible() {}
    override fun onSupportInvisible() {}
    override val isSupportVisible: Boolean get() = visibleDelegate?.isSupportVisible?:false

    override fun onBackPressedSupport(): Boolean {
        return false
    }

    fun <T : ISupportFragment?> findChildFragment(fragmentClass: Class<T>?): T? {
        return SupportHelper.findFragment(childFragmentManager, fragmentClass)
    }

    override val visibleDelegate:VisibleDelegate
        get() {
            if (mVisibleDelegate == null) {
                mVisibleDelegate = VisibleDelegate(this)
            }
            return mVisibleDelegate!!
        }

    /**
     * 隐藏软键盘
     */
    fun hideSoftInput() {
        val activity = activity ?: return
        val view = activity.window.decorView
        SupportHelper.hideSoftInput(view)
    }

    fun showSoftInput(view: View?) {
        SupportHelper.showSoftInput(view)
    }

    fun root(): BaseActivity {
        return if (_mActivity is BaseActivity && _mActivity != null) {
            _mActivity as BaseActivity
        } else {
            throw ClassCastException("this activity mast be extends RootActivity")
        }
    }
}