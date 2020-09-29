package com.nets.applibs.fragment

import androidx.fragment.app.FragmentManager

abstract class Action {
    var fragmentManager: FragmentManager? = null
    var action = ACTION_NORMAL

    constructor() {}
    constructor(action: Int) {
        this.action = action
    }

    constructor(
        action: Int,
        fragmentManager: FragmentManager?
    ) : this(action) {
        this.fragmentManager = fragmentManager
    }

    abstract fun run()

    companion object {
        const val ACTION_NORMAL = 0
        const val ACTION_BACK = 3
        const val ACTION_LOAD = 4
    }
}