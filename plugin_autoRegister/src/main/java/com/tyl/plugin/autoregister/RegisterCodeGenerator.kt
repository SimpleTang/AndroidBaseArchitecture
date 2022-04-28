package com.tyl.plugin.autoregister

import com.tyl.plugin.autoregister.utils.Logger
import com.tyl.plugin.autoregister.utils.ScanSetting
import org.apache.commons.io.IOUtils
import org.objectweb.asm.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

class RegisterCodeGenerator(val scanTargetConfig: ArrayList<ScanSetting>) {

    companion object {
        fun insertInitCodeTo(target: File, scanTargetConfig: ArrayList<ScanSetting>) {
            RegisterCodeGenerator(scanTargetConfig).insertInitCodeIntoFile(target)
        }
    }

    private fun insertInitCodeIntoFile(target: File) {
        if (target.absolutePath.endsWith(".jar")) {
            insertInitCodeIntoJarFile(target)
        } else if (target.absolutePath.endsWith(".class")) {

        }
    }

    /**
     * generate code into jar file
     * @param jarFile the jar file which contains LogisticsCenter.class
     * @return
     */
    private fun insertInitCodeIntoJarFile(jarFile: File?): File? {
        if (jarFile != null) {
            val optJar = File(jarFile.parent, jarFile.name + ".opt")
            if (optJar.exists())
                optJar.delete()
            val file = JarFile(jarFile)
            val enumeration = file.entries()
            val jarOutputStream = JarOutputStream(FileOutputStream(optJar))

            while (enumeration.hasMoreElements()) {
                val jarEntry = enumeration.nextElement() as JarEntry
                val entryName = jarEntry.name
                val zipEntry = ZipEntry(entryName)
                val inputStream = file.getInputStream(jarEntry)
                jarOutputStream.putNextEntry(zipEntry)
                if (ScanSetting.GENERATE_TO_CLASS_FILE_NAME == entryName) {

                    Logger.i("Insert init code to class >> $entryName")

                    val bytes = referHackWhenInit(inputStream)
                    jarOutputStream.write(bytes)
                } else {
                    jarOutputStream.write(IOUtils.toByteArray(inputStream))
                }
                inputStream.close()
                jarOutputStream.closeEntry()
            }
            jarOutputStream.close()
            file.close()

            if (jarFile.exists()) {
                jarFile.delete()
            }
            optJar.renameTo(jarFile)
        }
        return jarFile
    }

    private fun insertInitCodeIntoClassFile(classFile: File) {
        val optClass = File(classFile.parent, classFile.name + ".opt")
        if (optClass.exists())
            optClass.delete()

        val classInputStream = FileInputStream(classFile)
        val classOutputStream = FileOutputStream(optClass)
        val bytes = referHackWhenInit(classInputStream)
        classOutputStream.write(bytes)

        classOutputStream.close()
        classInputStream.close()

        if (classFile.exists()) {
            classFile.delete()
        }
        optClass.renameTo(classFile)

    }

    //refer hack class when object init
    fun referHackWhenInit(inputStream: InputStream): ByteArray {
        val cr = ClassReader(inputStream)
        val cw = ClassWriter(cr, 0)
        val cv = MyClassVisitor(Opcodes.ASM9, cw)
        cr.accept(cv, ClassReader.EXPAND_FRAMES)
        return cw.toByteArray()
    }

    inner class MyClassVisitor(api: Int, cv: ClassVisitor) : ClassVisitor(api, cv) {
        override fun visit(
            version: Int,
            access: Int,
            name: String?,
            signature: String?,
            superName: String?,
            interfaces: Array<out String>?
        ) {
            super.visit(version, access, name, signature, superName, interfaces)
        }

        override fun visitMethod(
            access: Int,
            name: String?,
            descriptor: String?,
            signature: String?,
            exceptions: Array<out String>?
        ): MethodVisitor {
            var mv = super.visitMethod(access, name, descriptor, signature, exceptions)
            //generate code into this method
            if (name == ScanSetting.GENERATE_TO_METHOD_NAME) {
                mv = RouteMethodVisitor(Opcodes.ASM9, mv)
            }
            return mv
        }
    }

    inner class RouteMethodVisitor(api: Int, mv: MethodVisitor) : MethodVisitor(api, mv) {

        override fun visitInsn(opcode: Int) {
            //generate code before return
            if ((opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN)) {
                scanTargetConfig.forEach {
                    it.classList.forEach { name ->
                        val newName = name.replace("/", ".")
                        mv.visitLdcInsn(newName)//类名
                        // generate invoke register method into LogisticsCenter.loadRouterMap()
                        mv.visitMethodInsn(
                            Opcodes.INVOKESTATIC,
                            ScanSetting.GENERATE_TO_CLASS_NAME,
                            ScanSetting.REGISTER_METHOD_NAME,
                            "(Ljava/lang/String;)V",
                            false
                        )
                    }
                }
            }
            super.visitInsn(opcode)
        }

        override fun visitMaxs(maxStack: Int, maxLocals: Int) {
            super.visitMaxs(maxStack + 4, maxLocals)
        }

    }
}