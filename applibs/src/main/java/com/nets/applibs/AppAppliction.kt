package com.nets.applibs

import androidx.multidex.MultiDexApplication
import com.nets.applibs.until.SpUtils

open class AppAppliction : MultiDexApplication() {
    @JvmField
    var isDebug = false
    var appTest = 0
    var appWebType = 1
    override fun onCreate() {
        super.onCreate()
        appliction = this
        SpUtils.init();
    }

    companion object {
        @JvmField
        var appliction: AppAppliction? = null
    }
}