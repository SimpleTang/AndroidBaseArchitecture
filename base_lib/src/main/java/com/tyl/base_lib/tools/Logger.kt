package com.tyl.base_lib.tools

import android.util.Log
import com.tyl.base_lib.BuildConfig

object L {
    private const val TAG_DEFAULT = "TLog"
    private const val TAG_NET = "TLog_NET"

    private var enable: Boolean = !BuildConfig.DEBUG

    @JvmStatic
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

    @JvmStatic
    fun setLogImpl(iLog: ILog) = apply {
        this.defaultLogImpl = iLog
    }

    @JvmStatic
    fun v(msg: String, tag: String = TAG_DEFAULT) {
        defaultLogImpl.log(LogLevel.VERBOSE, tag, msg)
    }

    @JvmStatic
    fun d(msg: String, tag: String = TAG_DEFAULT) {
        defaultLogImpl.log(LogLevel.DEBUG, tag, msg)
    }

    @JvmStatic
    fun i(msg: String, tag: String = TAG_DEFAULT) {
        defaultLogImpl.log(LogLevel.INFO, tag, msg)
    }

    @JvmStatic
    fun w(msg: String, tag: String = TAG_DEFAULT) {
        defaultLogImpl.log(LogLevel.WARN, tag, msg)
    }

    @JvmStatic
    fun e(msg: String, tag: String = TAG_DEFAULT) {
        defaultLogImpl.log(LogLevel.ERROR, tag, msg)
    }

    @JvmStatic
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