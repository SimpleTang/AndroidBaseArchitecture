package com.tyl.plugin.autoregister.utils

import org.objectweb.asm.ClassReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.util.jar.JarEntry
import java.util.jar.JarFile

object ScanUtil {

    fun shouldProcessPreDexJar(path: String): Boolean {
        return !path.contains("com.android.support") && !path.contains("/android/m2repository")
    }

    fun shouldProcessClass(entryName: String?): Boolean {
        return !entryName.isNullOrEmpty() && entryName.substringBeforeLast("/").endsWith(ScanSetting.ROUTER_CLASS_PACKAGE_NAME)
    }

    /**
     * 扫描 jar 文件中的左右 class ，找出实现指定接口的类和需要插入代码的目标类
     * @return jar 中是否包含代码插入的目标类
     */
    fun scanJar(jarFile: File, scanSettings: ArrayList<ScanSetting>): Boolean {
        var containGeneratorClass = false

        if (!shouldProcessPreDexJar(jarFile.absolutePath)) return false

        val file = JarFile(jarFile)
        val enumeration = file.entries()
        while (enumeration.hasMoreElements()) {
            val jarEntry = enumeration.nextElement() as JarEntry
            val entryName = jarEntry.name

            if (ScanSetting.GENERATE_TO_CLASS_FILE_NAME == entryName) {
                // 找到了代码插入的目标类
                containGeneratorClass = true
            } else if (entryName.endsWith(".class") && shouldProcessClass(entryName)) {
                Logger.i("file by jar: $entryName")
                val inputStream = file.getInputStream(jarEntry)
                scanClassByInputStream(inputStream, scanSettings)
                inputStream.close()
            }
        }

        file.close()

        return containGeneratorClass
    }

    private fun scanClassByInputStream(
        inputStream: InputStream,
        scanSettings: ArrayList<ScanSetting>
    ) {
        val cr = ClassReader(inputStream)
        cr.interfaces.forEach { interfaceName ->
            scanSettings.forEach {
                if (it.interfaceName == interfaceName && !it.classList.contains(cr.className))
                    it.classList.add(cr.className)
            }
        }
    }

    /**
     * 扫描目录下的所有文件 ，找出实现指定接口的类和需要插入代码的目标类
     * @return 代码插入的目标类的文件
     */
    fun scanClassByDirectory(
        leftSlash: Boolean,
        dir: File,
        scanSettings: ArrayList<ScanSetting>
    ): File? {
        return traverseDir(leftSlash, dir, scanSettings)
    }

    private fun traverseDir(
        leftSlash: Boolean, rootFile: File,
        scanSettings: ArrayList<ScanSetting>
    ): File? {
        var target: File? = null
        rootFile.listFiles()?.forEach { file ->
            var path = file.absolutePath
            if (!leftSlash) {
                path = path.replace("\\\\", "/")
            }
            if (path.endsWith(ScanSetting.GENERATE_TO_CLASS_FILE_NAME)) {
                target = File(path)
                Logger.i("file by dir: $path")
            } else if (file.isFile && shouldProcessClass(path) && path.endsWith(".class")) {
                scanClassByInputStream(FileInputStream(file), scanSettings)
                Logger.i("file by dir: $path")
            } else if (file.isDirectory) {
                val subTarget = traverseDir(leftSlash, file, scanSettings)
                target = target ?: subTarget
            }
        }
        return target
    }
}