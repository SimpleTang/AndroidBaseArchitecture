package com.tyl.plugin.autoregister.utils

import org.gradle.api.Project

object Logger {
    private var logger: org.gradle.api.logging.Logger? = null

    fun make(project: Project) {
        logger = project.logger
    }

    fun i(info: String?) {
        if (null != info && null != logger) {
            logger?.error("TYLPlugin::autoRegister >>> $info")
        }
    }

    fun e(error: String?) {
        if (null != error && null != logger) {
            logger?.error("TYLPlugin::autoRegister >>> $error")
        }
    }

    fun w(warning: String?) {
        if (null != warning && null != logger) {
            logger?.warn("TYLPlugin::autoRegister >>> $warning")
        }
    }
}