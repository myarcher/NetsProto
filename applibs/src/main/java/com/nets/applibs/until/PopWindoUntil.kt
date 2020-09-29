package com.nets.applibs.until

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.view.Gravity
import android.view.View
import android.widget.PopupWindow
import androidx.annotation.RequiresApi
import com.nets.applibs.listener.CallBackListener

class PopWindoUntil(
    private val context: Context,
    private val content: View,
    private val v: View
) {
    lateinit var location: IntArray
    private var popupWindow: PopupWindow? = null
    private var x = 0f
    private var y = 0f
    private var mListener: CallBackListener? = null
    fun setListener(listener: CallBackListener?) {
        mListener = listener
    }

    fun setX(x: Float) {
        this.x = x
    }

    fun setY(y: Float) {
        this.y = y
    }

    fun bulider(): PopWindoUntil {
        popupWindow = PopupWindow(content, x.toInt(), y.toInt())
        popupWindow!!.isFocusable = true
        popupWindow!!.isOutsideTouchable = true
        popupWindow!!.setBackgroundDrawable(BitmapDrawable())

        //设置监听
        popupWindow!!.setOnDismissListener {
            if (mListener != null) {
                mListener!!.callBack(12, 1, null, null)
            }
        }
        content.setOnTouchListener { view, motionEvent -> false }
        location = IntArray(2)
        v.getLocationOnScreen(location)
        return this
    }

    fun showPopUp(po: Positions) { //po 1左边,2 下方 3 右边 ，4上方
        if (po == Positions.LEFT) {
            val x1 = 5f
            popupWindow!!.showAtLocation(
                v,
                Gravity.NO_GRAVITY,
                location[0] - popupWindow!!.width,
                location[1] - x1.toInt()
            )
        } else if (po == Positions.BOTTOM) {
            popupWindow!!.showAsDropDown(v)
        } else if (po == Positions.RIGHT) {
            popupWindow!!.showAtLocation(
                v,
                Gravity.NO_GRAVITY,
                location[0] + v.width,
                location[1]
            )
        } else if (po == Positions.TOP) {
            popupWindow!!.showAtLocation(
                v,
                Gravity.NO_GRAVITY,
                location[0],
                location[1] - popupWindow!!.height
            )
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    fun showAsDropDown(
        v: View?,
        xoffset: Int,
        yoffset: Int,
        gravity: Int
    ) {
        popupWindow!!.showAsDropDown(v, xoffset, yoffset, gravity)
    }

    fun dismiss() {
        popupWindow!!.dismiss()
    }

    enum class Positions(var po: Int) {
        LEFT(1), BOTTOM(2), RIGHT(3), TOP(4);

    }

}