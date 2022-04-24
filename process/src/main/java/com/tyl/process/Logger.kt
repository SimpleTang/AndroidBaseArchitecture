package com.tyl.process

class Logger(private val moduleName: String, private val processorName: String) {

    private val logger = java.util.logging.Logger.getGlobal()

    fun e(error: String) {
//        logger.severe("$moduleName::$processorName >>> XXXXXXXXXX $error XXXXXXXXXX")
        println("$moduleName::$processorName >>> XXXXXXXXXX $error XXXXXXXXXX")
    }

    fun i(info: String) {
//        logger.info("$moduleName::$processorName >>> $info")
        println("$moduleName::$processorName >>> $info")
    }

    fun w(warning: String) {
//        logger.warning("$moduleName::$processorName >>> !!!!!!!!!! $warning !!!!!!!!!!")
        println("$moduleName::$processorName >>> !!!!!!!!!! $warning !!!!!!!!!!")
    }
}