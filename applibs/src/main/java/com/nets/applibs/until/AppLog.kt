package com.nets.applibs.until

import android.util.Log
import com.nets.applibs.AppAppliction

object AppLog {
    const val V = Log.VERBOSE
    const val D = Log.DEBUG
    const val I = Log.INFO
    const val W = Log.WARN
    const val E = Log.ERROR
    const val A = Log.ASSERT
    private val LINE_SEP = System.getProperty("line.separator")
    private const val LEFT_BORDER = "│ "
    private const val MAX_LEN = 1100
    private const val NOTHING = "log nothing"
    private const val NULL = "null"
    private const val ARGS = "args"
    fun appLog(key: String?, value: String?) {
        try {
            log(I, key, value)
        } catch (e: Exception) {
        }
    }

    private const val log_tag = "nets"
    private const val net_tag = "http_log"
    private const val json_tag = "json"
    fun log(value: String?) {
        appLog(log_tag, value)
    }

    fun net(value: String?) {
        appLog(net_tag, value)
    }

    fun json(value: String?) {
        appLog(json_tag, value)
    }

    fun log(type: Int, tag: String?, vararg contents: Any?) {
        if (!AppAppliction.appliction!!.isDebug) {
            return
        }
        val body = processBody(*contents as Array<out Any>)
        printMsg(type, tag, body)
    }

    private fun processBody(vararg contents: Any): String {
        var body = NULL
        if (contents != null) {
            body = if (contents.size == 1) {
                contents[0].toString()
            } else {
                val sb = StringBuilder()
                var i = 0
                val len = contents.size
                while (i < len) {
                    val content = contents[i].toString()
                    sb.append(ARGS)
                        .append("[")
                        .append(i)
                        .append("]")
                        .append(" = ")
                        .append(content)
                        .append(LINE_SEP)
                    ++i
                }
                sb.toString()
            }
        }
        return if (body.length == 0) NOTHING else body
    }

    private fun printMsg(type: Int, tag: String?, msg: String) {
        val len = msg.length
        val countOfSub = len / MAX_LEN
        if (countOfSub > 0) {
            var index = 0
            for (i in 0 until countOfSub) {
                printSubMsg(type, tag, msg.substring(index, index + MAX_LEN))
                index += MAX_LEN
            }
            if (index != len) {
                printSubMsg(type, tag, msg.substring(index, len))
            }
        } else {
            printSubMsg(type, tag, msg)
        }
    }

    private fun printSubMsg(type: Int, tag: String?, msg: String) {
        val lines = msg.split(LINE_SEP!!.toRegex()).toTypedArray()
        for (line in lines) {
            Log.println(type, tag, LEFT_BORDER + line)
        }
    }
}