package com.nets.applibs.vo


import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.nets.applibs.R

abstract class CustomActivity : BaseActivity() {
     var frag: BaseFragment? = null
    override val layoutId: Int=R.layout.activity_base

    override fun initData(bundle: Bundle?) {
        super.initData(bundle)
        defaultSetting()
        frag = getFragment()
        if(frag!=null) {
            var ex=intent.extras
            if(ex!=null) {
                frag?.arguments = ex
            }
            initCreate(bundle)
            if (frag != null) {
                start(getFragId(), frag)
            }
        }
    }

    open fun getFragId(): Int {
        return R.id.base_fragment
    }
    open fun initCreate(bundle: Bundle?) {

    }

     open fun getFragment(): BaseFragment?{
         return null
     }
    fun setRedBar() {
        setTitileColor(android.R.color.white)
        setRightTvColor(android.R.color.white)
        setBarLineVis(View.GONE)
    }



    open fun defaultSetting() {
        findViewById<View>(R.id.base_title_back)?.setOnClickListener { back() }
    }

    open fun setBarLineVis(v:Int){
        findViewById<View>(R.id.base_title_line)?.visibility = v
    }
    open fun setTitile(title: String) {
        findViewById<TextView>(R.id.base_title_name)?.text = title
    }
    open fun setTitileColor(color: Int) {
        findViewById<TextView>(R.id.base_title_name)?.setTextColor(ContextCompat.getColor(this,color))
    }
    open fun setBack(listener: View.OnClickListener) {
        findViewById<View>(R.id.base_title_back)?.setOnClickListener(listener)
    }

    open fun setRightTvColor(color: Int) {
        findViewById<TextView>(R.id.base_title_right_tv)?.setTextColor(ContextCompat.getColor(this,color))
    }

    open fun setRightTvText(text: String) {
        findViewById<TextView>(R.id.base_title_right_tv)?.text = text
    }

    open fun setRightTvVis(v: Int) {
        findViewById<View>(R.id.base_title_right_tv)?.visibility = v
    }

    open fun setRightVis(v: Int) {
        findViewById<View>(R.id.base_title_right)?.visibility = v
    }

    open fun setRightRes(res: Int) {
        findViewById<ImageView>(R.id.base_title_right)?.setImageResource(res)
    }

    open fun setBackRes(res: Int) {
        findViewById<ImageView>(R.id.base_title_back)?.setImageResource(res)
    }

    open fun setBarBgColor(res: Int) {
        findViewById<View>(R.id.base_title_bar)?.setBackgroundColor(ContextCompat.getColor(this,res))
    }



    override
    fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return back()
        } else {
            return super.onKeyDown(keyCode, event)
        }
    }

    open fun back(): Boolean {
        finish()
        return false
    }

    override fun onDestroy() {
        hideSoftKeyBoard()
        super.onDestroy()
    }

}
