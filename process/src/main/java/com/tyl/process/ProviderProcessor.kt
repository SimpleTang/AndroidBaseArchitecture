package com.tyl.process

import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.tyl.process_annotation.Provider
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import kotlin.random.Random

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions("ModuleName")
@SupportedAnnotationTypes("com.tyl.process_annotation.Provider")
class ProviderProcessor : AbstractProcessor() {

    // 打印日志工具类
    private lateinit var mMessage: Messager

    // 文件操作类，我们将通过此类生成kotlin文件
    private lateinit var mFiler: Filer

    // 类型工具类，处理Element的类型
    private lateinit var mTypeTools: Types

    private lateinit var mElementUtils: Elements

    private lateinit var logger: Logger

    // gradle传进来的模块名
    private var mModuleName: String? = null

    override fun init(processingEnv: ProcessingEnvironment?) {
        super.init(processingEnv)
        if (processingEnv == null) return
        mMessage = processingEnv.messager
        mFiler = processingEnv.filer
        mElementUtils = processingEnv.elementUtils
        mTypeTools = processingEnv.typeUtils

        mModuleName = processingEnv.options["ModuleName"]

        logger = Logger(mModuleName ?: "unknown", "ProviderProcessor")

        logger.i("ProviderProcessor 初始化完成...")
    }

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment?
    ): Boolean {

        //------------ 1.查找@Provider注解下的所有类 ---------------------------
        if (annotations.isNullOrEmpty() || roundEnv == null) {
//            logger.i("未找到 @Provider")
            return false
        }
        // 获取所有的被注解的节点
        val elements = roundEnv.getElementsAnnotatedWith(Provider::class.java)

        val moduleProviderInterfaceElement =
            processingEnv.elementUtils.getTypeElement("com.tyl.base_lib.provider.ModuleProvider")

        // 找到的实现类
        val map = HashMap<String, String>()
        elements?.forEach { element ->
            if (element.kind.isInterface) {
                logger.e("@Provider 不能能使用在接口上，${element} 是一个接口")
                throw IllegalArgumentException("@Provider 只能使用在实现类上")
            }
            var isModuleProviderSub = false
            val providerClass = element as TypeElement
            providerClass.interfaces.forEach {
                val name = "$it"
                val providerParentInterface = processingEnv.elementUtils.getTypeElement(name)
                if (mTypeTools.isSubtype(
                        providerParentInterface.asType(),
                        moduleProviderInterfaceElement.asType()
                    )
                ) {
                    map[providerParentInterface.toString()] = providerClass.toString()
                    isModuleProviderSub = true
                }
            }
            if (!isModuleProviderSub) {
                logger.e("@Provider $element 不是 ModuleProvider 的实现")
                throw IllegalArgumentException("@Provider 只能使用在 ModuleProvider 的实现类上")
            }
        }

        map.forEach {
            logger.i("@Provider ${it.key} : ${it.value}")
        }
        //------------ 2.生成需要的类 ---------------------------

        map.takeIf { it.isNotEmpty() }?.let {
            val packageName = "com.tyl.provider.generate"
            val fileName = "ProviderRegister_${getModuleNameOrRandomStr()}"
            val moduleProviderInterface =
                ClassName("com.tyl.base_lib.provider", "IProviderRegister")

            val mapType = HashMap::class.java.asClassName().parameterizedBy(
                String::class.asTypeName(),
                String::class.asTypeName()
            )

            val registerFun = FunSpec.builder("register")
                .addModifiers(KModifier.OVERRIDE)
                .addParameter("map", mapType)

            it.forEach { (s, s2) ->
                registerFun.addStatement(
                    "map[%S] = %S".trimMargin(),
                    s, s2
                )
            }

            val registerClass = TypeSpec.classBuilder(fileName)
                .addSuperinterface(moduleProviderInterface)
                .addFunction(registerFun.build())
                .build()

            // 生成文件
            val file = FileSpec.builder(packageName, fileName)
                .addType(registerClass)
                .build()

            logger.i("${packageName}.$fileName 生成成功")

            file.writeTo(mFiler)
        }
        return true
    }

    private fun getModuleNameOrRandomStr(): String {
        return mModuleName ?: "${Random.nextInt(10000, 100000)}"
    }
}