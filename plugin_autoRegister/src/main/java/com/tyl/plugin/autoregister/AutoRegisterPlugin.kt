package com.tyl.plugin.autoregister

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.tyl.plugin.autoregister.utils.Logger
import com.tyl.plugin.autoregister.utils.ScanSetting
import org.gradle.api.Plugin
import org.gradle.api.Project


class AutoRegisterPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        val isApp = target.plugins.hasPlugin(AppPlugin::class.java)
        //only application module needs this plugin to generate register code
        if (isApp) {
            Logger.make(target)

            Logger.i("Project enable autoRegister plugin")

            val android = target.extensions.getByType(AppExtension::class.java)

            //init arouter-auto-register settings
            val list = ArrayList<ScanSetting>()
            list.add(ScanSetting("com/tyl/base_lib/provider/IProviderRegister"))
            val transformImpl = AutoRegisterTransform(list)
            //register this plugin
            android.registerTransform(transformImpl)
        }
    }
}