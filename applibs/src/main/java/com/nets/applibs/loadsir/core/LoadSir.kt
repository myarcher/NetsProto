package com.nets.applibs.loadsir.core

import com.nets.applibs.loadsir.callback.CallBackType
import com.nets.applibs.loadsir.callback.Callback
import com.nets.applibs.loadsir.callback.Callback.OnViewListener
import java.util.*

class LoadSir {
    private var builder: Builder

    private constructor() {
        builder = Builder()
    }

    private fun setBuilder(builder: Builder) {
        this.builder = builder
    }

    private constructor(builder: Builder) {
        this.builder = builder
    }

    fun register(target: Any?): LoadService<*> {
        return register(target, null)
    }

    fun register(target: Any?, onReloadListener: OnViewListener?): LoadService<*> {
        val targetContext = LoadSirUtil.getTargetContext(target)
        return LoadService<Any?>(targetContext, onReloadListener, builder)
    }

    class Builder {
         var callbacks = ArrayList<Callback>()

        var defaultCallback: Class<out Callback>? =
            null
            private set
        var defaultCallType = CallBackType.LOADING
            private set

        fun addCallback(callback: Callback): Builder {
            callbacks.add(callback)
            return this
        }

        fun setDefaultCallback(defaultCallback: Class<out Callback>): Builder {
            this.defaultCallback = defaultCallback
            return this
        }

        fun setDefaultCallType(defaultCallType: CallBackType): Builder {
            this.defaultCallType = defaultCallType
            return this
        }

        fun getCallbacks(): List<Callback> {
            return callbacks
        }

        fun commit() {
            default!!.setBuilder(this)
        }

        fun build(): LoadSir {
            return LoadSir(this)
        }
    }

    companion object {
        @Volatile
        private var loadSir: LoadSir? = null
        val default: LoadSir?
            get() {
                if (loadSir == null) {
                    synchronized(LoadSir::class.java) {
                        if (loadSir == null) {
                            loadSir = LoadSir()
                        }
                    }
                }
                return loadSir
            }

        fun beginBuilder(): Builder {
            return Builder()
        }
    }
}