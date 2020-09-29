package com.nets.applibs.loadsir.core

import android.content.Context
import android.view.View
import android.view.ViewGroup

/**
 * Description:TODO
 * Create Time:2017/9/4 16:28
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
class TargetContext(
    val context: Context,
    val parentView: ViewGroup?,
    val oldContent: View,
    val childIndex: Int
)