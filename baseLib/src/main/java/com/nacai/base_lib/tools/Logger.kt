package com.nacai.base_lib.tools

import android.util.Log
import com.nacai.base_lib.BuildConfig

object L {
    private const val TAG_DEFAULT = "TLog"
    private const val TAG_NET = "TLog_NET"

    private var enable: Boolean = !BuildConfig.DEBUG

    fun enable(enable: Boolean) = apply {
        this.enable = enable
    }

    private var defaultLogImpl: ILog = object : ILog {
        override fun log(level: LogLevel, tag: String, content: String) {
            when (level) {
                LogLevel.VERBOSE -> {
                    Log.v(tag, content)
                }
                LogLevel.DEBUG -> {
                    Log.d(tag, content)
                }
                LogLevel.INFO -> {
                    Log.i(tag, content)
                }
                LogLevel.WARN -> {
                    Log.w(tag, content)
                }
                LogLevel.ERROR -> {
                    Log.e(tag, content)
                }
                LogLevel.ASSERT -> {
                    Log.e("${tag}_ASSERT", content)
                }
            }
        }
    }

    fun setLogImpl(iLog: ILog) = apply {
        this.defaultLogImpl = iLog
    }

    fun v(tag: String = TAG_DEFAULT, msg: String) {
        defaultLogImpl.log(LogLevel.VERBOSE, tag, msg)
    }

    fun d(tag: String = TAG_DEFAULT, msg: String) {
        defaultLogImpl.log(LogLevel.DEBUG, tag, msg)
    }

    fun i(tag: String = TAG_DEFAULT, msg: String) {
        defaultLogImpl.log(LogLevel.INFO, tag, msg)
    }

    fun w(tag: String = TAG_DEFAULT, msg: String) {
        defaultLogImpl.log(LogLevel.WARN, tag, msg)
    }

    fun e(tag: String = TAG_DEFAULT, msg: String) {
        defaultLogImpl.log(LogLevel.ERROR, tag, msg)
    }

    fun net(msg: String) {
        defaultLogImpl.log(LogLevel.DEBUG, TAG_NET, msg)
    }
}

interface ILog {
    fun log(level: LogLevel, tag: String, content: String)
}

enum class LogLevel {
    VERBOSE,
    DEBUG,
    INFO,
    WARN,
    ERROR,
    ASSERT
}