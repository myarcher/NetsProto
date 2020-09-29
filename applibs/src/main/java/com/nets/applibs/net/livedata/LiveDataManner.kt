package com.nets.applibs.net.livedata

import androidx.lifecycle.MutableLiveData
import java.util.concurrent.ConcurrentHashMap

class LiveDataManner {
    private var dataValues: MutableMap<String, MutableLiveData<*>> = ConcurrentHashMap()
    fun <T> post(key: String, value: T) {
        if (dataValues[key] == null) {
            dataValues[key] = MutableLiveData<T>()
        }
        (dataValues[key]!! as MutableLiveData<T>).postValue(value)
    }

    fun <T>get(key: String): MutableLiveData<T> {
        if (dataValues[key] == null) {
            dataValues[key] = MutableLiveData<T>()
        }
        return dataValues[key]!!  as MutableLiveData<T>
    }

    fun clear(){
        dataValues.clear()
    }

}