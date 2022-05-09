package com.tyl.plugin.autoregister.utils

import java.io.File

class ScanSetting(var interfaceName: String) {

    /**
     * scan result for {@link #interfaceName}
     * class names in this list
     */
    val classList = ArrayList<String>()

    override fun toString(): String {
        return "ScanSetting interface : $interfaceName , find class list : ${classList.joinToString()}"
    }

    companion object {
        const val PLUGIN_NAME = "AutoRegister"

        const val ROUTER_CLASS_PACKAGE_NAME = "com/tyl/provider/generate"

        /**
         * The register code is generated into this class
         */
        const val GENERATE_TO_CLASS_NAME = "com/tyl/base_lib/provider/ProviderRegisterManager"

        /**
         * you know. this is the class file(or entry in jar file) name
         */
        const val GENERATE_TO_CLASS_FILE_NAME = "$GENERATE_TO_CLASS_NAME.class"

        /**
         * The register code is generated into this method
         */
        const val GENERATE_TO_METHOD_NAME = "loadRegister"

        /**
         * register method name in class: {@link #GENERATE_TO_CLASS_NAME}
         */
        const val REGISTER_METHOD_NAME = "loadClassMapByRegister"
    }
}