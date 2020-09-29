package com.nets.applibs.vo

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.gyf.immersionbar.ImmersionBar
import com.nets.applibs.AppManager.Companion.get
import com.nets.applibs.fragment.Action
import com.nets.applibs.fragment.ISupport.ISupportActivity
import com.nets.applibs.fragment.ISupport.ISupportFragment
import com.nets.applibs.fragment.SupportHelper.findFragment
import com.nets.applibs.fragment.SupportHelper.getActiveFragment
import com.nets.applibs.fragment.SupportHelper.getTopFragment
import com.nets.applibs.fragment.TransactionDelegate
import com.nets.applibs.until.ToolUntil

abstract class BaseActivity : AppCompatActivity(), ISupportActivity {
    private var mTransactionDelegate: TransactionDelegate? = null
    protected var mImmersionBar: ImmersionBar? = null
    @SuppressLint("SourceLockedOrientationActivity")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        get().addActivity(this)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        mTransactionDelegate = transactionDelegate
        val layout = layoutId
        if (layout > 0) {
            setContentView(layout)
        }
        if (isImmersionBarEnabled) {
            initImmersionBar()
        }
        initData(savedInstanceState)
    }

    override val transactionDelegate: TransactionDelegate
        get() {
            if (mTransactionDelegate == null) {
                mTransactionDelegate = TransactionDelegate()
            }
            return mTransactionDelegate!!
        }

    fun initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this)
        mImmersionBar?.keyboardEnable(true)
            ?.statusBarDarkFont(true, 0.3f)
            ?.init()
    }

    protected val isImmersionBarEnabled: Boolean
        protected get() = true
    protected abstract val layoutId: Int
    open fun initData(savedInstanceState: Bundle?) {}
    override fun finish() {
        hideSoftKeyBoard()
        super.finish()
    }

    override fun onBackPressed() {
        mTransactionDelegate!!.runAction(object : Action(ACTION_BACK) {
            override fun run() {
                val activeFragment = getActiveFragment(supportFragmentManager)
                if (mTransactionDelegate!!.dispatchBackPressedEvent(activeFragment)) {
                    return
                }
                onBackPressedSupport()
            }
        })
    }

    fun onBackPressedSupport() {
        ActivityCompat.finishAfterTransition(this)
    }

    fun start(containerId: Int, toFragment: ISupportFragment?) {
        mTransactionDelegate!!.loadRootTransaction(
            supportFragmentManager,
            containerId,
            toFragment!!
        )
    }

    fun start(containerId: Int, showPosition: Int, vararg toFragments: ISupportFragment?) {
        mTransactionDelegate!!.loadMultipleRootTransaction(
            supportFragmentManager,
            containerId,
            showPosition,
            *toFragments
        )
    }

    fun start(showFragment: ISupportFragment?) {
        mTransactionDelegate!!.showHideFragment(supportFragmentManager, showFragment!!, null)
    }

    private val topFragment: ISupportFragment?
        private get() = getTopFragment(supportFragmentManager)

    fun <T : ISupportFragment?> findFragment(fragmentClass: Class<T>?): T? {
        return findFragment(supportFragmentManager, fragmentClass)
    }

    fun hideSoftKeyBoard() {
        val imm =
            this.applicationContext.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.window.decorView.windowToken, 0)
    }

    public override fun onDestroy() {
        get().finishActivity(this)
        if (isImmersionBarEnabled) {
            if (mImmersionBar != null) {
                //  mImmersionBar.destroy();
                mImmersionBar = null
            }
        }
        //软键盘造成的内存泄漏
        ToolUntil.inputLeak(this)
        super.onDestroy()
    }
}