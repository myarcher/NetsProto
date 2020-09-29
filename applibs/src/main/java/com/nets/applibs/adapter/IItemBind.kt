package com.nets.applibs.adapter

/**
 * created by yhao on 2017/9/8.
 */
interface IItemBind<T> {
    fun bind(
        holder: ItemView?,
        data: T,
        position: Int,
        itemCount: Int
    )
}