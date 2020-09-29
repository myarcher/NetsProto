package com.nets.applibs.adapter

/**
 * created by yhao on 2017/9/8.
 */
interface IItemType<T> {
    fun type(data: T, position: Int): Int
}