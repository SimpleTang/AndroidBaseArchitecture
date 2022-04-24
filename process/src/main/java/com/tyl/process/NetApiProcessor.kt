package com.tyl.process

import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.tyl.process_annotation.NetApi
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions("ModuleName")
@SupportedAnnotationTypes("com.tyl.process_annotation.NetApi")
class NetApiProcessor : AbstractProcessor() {

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

        logger = Logger(mModuleName ?: "unknown", "NetApiProcessor")

        logger.i("NetApiProcessor 初始化完成...")
    }

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment?
    ): Boolean {

        //------------ 1.查找@NetApi注解下的所有类 ---------------------------
        if (annotations.isNullOrEmpty() || roundEnv == null) {
//            logger.i("未找到 @NetApi")
            return false
        }
        // 获取所有的被注解的节点
        val elements = roundEnv.getElementsAnnotatedWith(NetApi::class.java)

        when {
            elements.size > 1 -> {
                logger.e("发现了多个@NetApi：")

                elements.forEach {
                    logger.e("$it")
                }
                throw IllegalArgumentException("发现了多个网络接口类")

            }
            elements.size < 1 -> {
//                logger.i("未找到 @NetApi")
                return false
            }
            else -> {
                logger.i("@NetApi ${elements.first()}")
            }
        }
        val element = elements.first()

        if (!element.kind.isInterface) {
            logger.e("@NetApi 只能使用在接口上，${elements.first()} 不是一个接口")
            throw IllegalArgumentException("@NetApi 只能使用在接口上")
        }

        //------------ 2.生成需要的类 ---------------------------
        val apiClass = element as TypeElement
        val packageName = processingEnv.elementUtils.getPackageOf(element).qualifiedName.toString()
        val netManagerClassName = ClassName("com.tyl.base_lib.network", "NetManager")
        val apiClassName = ClassName(packageName, apiClass.simpleName.toString())

        // 生成包常量
        // private val apiServer by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { NetManager[ApiServer::class.java] }
        val host = apiClass.getAnnotation(NetApi::class.java).host
        val fieldName = "m${apiClass.simpleName}"
        val apiServerField = PropertySpec.builder(fieldName, apiClassName)
            .addModifiers(KModifier.PRIVATE)
            .delegate(
                CodeBlock.builder()
                    .beginControlFlow(
                        "lazy(mode = %T.SYNCHRONIZED)",
                        LazyThreadSafetyMode::class.asTypeName()
                    )
                    .add(
                        if (host.isEmpty()) "%T[%T::class.java]" else "%T[\"$host\",%T::class.java]",
                        netManagerClassName,
                        apiClassName
                    )
                    .endControlFlow()
                    .build()
            )
            .build()

        // 生成方法
        // BaseViewModel.api
        val jobClassName = ClassName("kotlinx.coroutines", "Job")
        val baseViewModelClassName = ClassName("com.tyl.base_lib.base", "BaseViewModel")
        val requestNetApiFunName = ClassName("com.tyl.base_lib.network", "requestNetApi")
        val throwableClassName = ClassName("kotlin", "Throwable")
        val viewModelScopeFieldName = ClassName("androidx.lifecycle","viewModelScope")

        val showToastParams = ParameterSpec.builder("showToast", Boolean::class.java)
            .defaultValue("true")
            .build()
        val errorParams = ParameterSpec.builder(
            "onError",
            LambdaTypeName.get(throwableClassName, returnType = Unit::class.java.asClassName())
        )
            .defaultValue("{}")
            .build()
        val finallyParams = ParameterSpec.builder(
            "onFinally",
            LambdaTypeName.get(returnType = Unit::class.java.asClassName())
        )
            .defaultValue("{}")
            .build()
        val callParams = ParameterSpec.builder(
            "call",
            LambdaTypeName.get(apiClassName, returnType = Unit::class.java.asClassName())
                .copy(suspending = true)
        )
            .build()
        val vmApiFun = FunSpec.builder("api")
            .receiver(baseViewModelClassName)
            .returns(jobClassName)
            .addParameter(showToastParams)
            .addParameter(errorParams)
            .addParameter(finallyParams)
            .addParameter(callParams)
            .addStatement(
                "return %T.%T(showToast, onError, onFinally, { call($fieldName) })",
                viewModelScopeFieldName,
                requestNetApiFunName
            )
            .build()

        // 生成方法
        // Any.leakApi
        val doLeakRequestFunName = ClassName("com.tyl.base_lib.network", "doLeakRequest")
        val coroutineContextClassName = ClassName("kotlin.coroutines", "CoroutineContext")
        val dispatchersClassName = ClassName("kotlinx.coroutines", "Dispatchers")
        val contextParams = ParameterSpec.builder("context", coroutineContextClassName)
            .defaultValue("%T.Main", dispatchersClassName)
            .build()
        val anyApiFun = FunSpec.builder("leakApi")
            .returns(jobClassName)
            .addParameter(contextParams)
            .addParameter(showToastParams)
            .addParameter(errorParams)
            .addParameter(finallyParams)
            .addParameter(callParams)
            .addStatement(
                "return %T(context, showToast, onError, onFinally, { call($fieldName) })",
                doLeakRequestFunName
            )
            .build()

        // 生成方法
        // suspendApi
        val suspendRequestNetApiFunName = ClassName("com.tyl.base_lib.network", "suspendRequestNetApi")
        val callTypeVariableParams = ParameterSpec.builder(
            "call",
            LambdaTypeName.get(apiClassName, returnType = TypeVariableName.invoke("T"))
                .copy(suspending = true)
        )
            .build()
        val suspendApiFun = FunSpec.builder("suspendApi")
            .addModifiers(KModifier.SUSPEND)
            .addTypeVariable(TypeVariableName.invoke("T"))
            .returns(TypeVariableName.invoke("T"))
            .addParameter(showToastParams)
            .addParameter(callTypeVariableParams)
            .addStatement(
                "return %T(showToast, { call($fieldName) })",
                suspendRequestNetApiFunName
            )
            .build()


        // 生成文件
        val file = FileSpec.builder(packageName, "NetApi_Impl")
            .addProperty(apiServerField)
            .addFunction(vmApiFun)
            .addFunction(anyApiFun)
            .addFunction(suspendApiFun)
            .build()

        logger.i("${packageName}.NetApi_Impl 生成成功")

        file.writeTo(mFiler)
        return true
    }
}