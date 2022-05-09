package com.tyl.plugin.autoregister

import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import com.tyl.plugin.autoregister.utils.Logger
import com.tyl.plugin.autoregister.utils.ScanSetting
import com.tyl.plugin.autoregister.utils.ScanUtil
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import java.io.File

class AutoRegisterTransform(private val scanTargetConfig: ArrayList<ScanSetting>) : Transform() {

    override fun getName(): String {
        return ScanSetting.PLUGIN_NAME
    }

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        return TransformManager.CONTENT_CLASS
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    override fun isIncremental(): Boolean {
        return false
    }

    override fun transform(transformInvocation: TransformInvocation) {
        val startTime = System.currentTimeMillis()
        val leftSlash = File.separator == "/"
        var generatorCodeTargetFile: File? = null

        // 1. 查找指定接口的实现类 和 插桩的目标
        Logger.i("Start scan class by jar file and directory.")
        transformInvocation.inputs.forEach { input ->
            // 查找所有jar文件
            input.jarInputs.forEach { jarInput ->
                var destName = jarInput.name
                val hexName = DigestUtils.md5Hex(jarInput.file.absolutePath)
                if (destName.endsWith(".jar")) {
                    destName = destName.substring(0, destName.length - 4)
                }
                // input file
                val src = jarInput.file
                // output file
                val dest = transformInvocation.outputProvider.getContentLocation(
                    destName + "_" + hexName,
                    jarInput.contentTypes,
                    jarInput.scopes,
                    Format.JAR
                )
                // 先将jar复制到目标位置
                FileUtils.copyFile(src, dest)
                // 复制完成后再查找类
                if (ScanUtil.scanJar(dest, scanTargetConfig)) {
                    generatorCodeTargetFile = dest
                }
            }
            // 查找所有目录下的 class 文件
            input.directoryInputs.forEach { directoryInput ->
                val dest = transformInvocation.outputProvider.getContentLocation(
                    directoryInput.name,
                    directoryInput.contentTypes,
                    directoryInput.scopes,
                    Format.DIRECTORY
                )
                var root = directoryInput.file.absolutePath
                if (!root.endsWith(File.separator))
                    root += File.separator
                // 先将整个目录完整复制到目标位置
                FileUtils.copyDirectory(directoryInput.file, dest)
                // 遍历整个目录，找出插入代码的目标类
                val targetFile = ScanUtil.scanClassByDirectory(leftSlash, dest, scanTargetConfig)
                generatorCodeTargetFile = generatorCodeTargetFile ?: targetFile
            }
        }
        Logger.i("Scan finish, current cost time " + (System.currentTimeMillis() - startTime) + "ms")

        // 2. 找出所有指定接口的实现类和代码插入的目标类后，执行代码插入
        generatorCodeTargetFile?.let{
            Logger.i("Start Generate code.")

            RegisterCodeGenerator.insertInitCodeTo(it,scanTargetConfig)

            Logger.i("Generate code finish, current cost time: " + (System.currentTimeMillis() - startTime) + "ms")
        }



    }
}