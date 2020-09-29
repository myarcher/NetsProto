package com.nets.applibs

import android.app.Activity
import android.app.ActivityManager
import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.os.Process
import androidx.annotation.RequiresApi
import java.util.*

class AppManager private constructor() {
    private var activityStack: Stack<Activity>? = null
    fun addActivity(activity: Activity) {
        if (activityStack == null) {
            activityStack = Stack()
        }
        activityStack!!.add(activity)
    }

    fun currentActivity(): Activity? {
        return try {
            activityStack!!.lastElement()
        } catch (e: Exception) {
            null
        }
    }

    fun finishActivity() {
        var activity = activityStack!!.lastElement()
        if (activity != null) {
            activity.finish()
            activity = null
        }
    }

    fun finishActivity(activity: Activity?) {
        var activity = activity
        if (activity != null) {
            activityStack!!.remove(activity)
            activity.finish()
            activity = null
        }
    }

    fun finishActivity(cls: Class<Activity?>) {
        for (i in activityStack!!.indices.reversed()) {
            val activity = activityStack!![i]
            if (activity.javaClass == cls) {
                this.finishActivity(activity)
            }
        }
    }

    /**
     * 结束指定类名的Activity
     */
    fun finishAllOneActivity(cls: Class<*>) {
        try {
            val iterator = activityStack!!.iterator()
            while (iterator.hasNext()) {
                val activity = iterator.next()
                if (activity!!.javaClass == cls) {
                    iterator.remove()
                    activity?.finish()
                }
            }
        } catch (e: Exception) {
        }
    }

    /**
     * 移除所有的activity
     */
    fun finishAllActivity() {
        for (i in activityStack!!.indices.reversed()) {
            val activity = activityStack!![i]
            if (activity != null) {
                this.finishActivity(activity)
            }
        }
        activityStack!!.clear()
    }

    /**
     * 退出应用（注意这个在6.0版本上，是需要动态权限的）
     *
     * @param context 上下环境
     */
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    fun AppExit(context: Activity) {
        try {
            finishAllActivity()
            val activityMgr =
                context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            activityMgr.restartPackage(context.packageName)
            //	System.exit(0);
            Process.killProcess(Process.myPid())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showExitDialog(context: Activity) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage("你确定退出应用吗?")
        builder.setNegativeButton("取消", null)
        builder.setPositiveButton("确定") { dialog, which ->
            AppExit(context)
            context.finish()
        }
        val alert = builder.create()
        alert.show()
    }

    companion object {
        private var instance: AppManager? = null
        @JvmStatic
        fun get(): AppManager {
            if (instance == null) {
                instance = AppManager()
            }
            return instance!!
        }
    }

    init {
        if (activityStack == null) {
            activityStack = Stack()
        }
    }
}