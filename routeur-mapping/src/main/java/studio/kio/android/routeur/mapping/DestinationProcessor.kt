package studio.kio.android.routeur.mapping

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedAnnotationTypes
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

@Suppress("UNCHECKED_CAST", "unused")
@SupportedAnnotationTypes("$ANNOTATION_ROUTE_PATH_PACKAGE.$ANNOTATION_ROUTE_PATH_NAME")
class DestinationProcessor : AbstractProcessor() {
    private val annotationRoutePathClass =
        Class.forName("$ANNOTATION_ROUTE_PATH_PACKAGE.$ANNOTATION_ROUTE_PATH_NAME") as Class<Annotation>

    private lateinit var moduleName: String

    override fun init(env: ProcessingEnvironment?) {
        super.init(env)
        moduleName =
            requireNotNull(env?.options?.get(KEY_MODULE_NAME)) { "No module name provided" }
    }

    override fun process(
        typeElements: MutableSet<out TypeElement>,
        env: RoundEnvironment
    ): Boolean {
        if (typeElements.isNotEmpty()) {
            generateRouterMapper(env.getElementsAnnotatedWith(annotationRoutePathClass))
        }
        return false
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    private fun generateRouterMapper(elementsAnnotatedWith: MutableSet<out Element>) {

        val loadToMapStatement = StringBuilder()

        elementsAnnotatedWith.forEach {

            val routeType =
                it.getAnnotation(annotationRoutePathClass)

            val routeTypeStr = routeType.toString()
            val equalIndex = routeTypeStr.indexOf('=')
            val m =
                "map[${
                    routeTypeStr.substring(equalIndex + 1, routeTypeStr.length - 1).trimIndent()
                }] = $it::class"
            println(routeType)
            loadToMapStatement.append(m).append("\n")
        }

        val file = FileSpec.builder(GEN_PACKAGE, "${CLASS_ROUTE_MAPPER_IMPL_NAME}$${moduleName}")
            .addType(
                TypeSpec.classBuilder("${CLASS_ROUTE_MAPPER_IMPL_NAME}$${moduleName}")
                    .addSuperinterface(Class.forName("$MAPPER_PACKAGE.$INTERFACE_I_ROUTE_MAPPER_NAME"))
                    .addFunction(
                        FunSpec.builder("loadMapping")
                            .addParameter("map", createParameterOfLoadMapping())
                            .addModifiers(KModifier.OVERRIDE)
                            .addStatement(loadToMapStatement.toString())
                            .build()
                    )
                    .build()
            )
            .build()
        file.writeTo(processingEnv.filer)
    }

    /**
     * 为RouterMapperImpl中的loadMapping创建参数类型描述
     */
    private fun createParameterOfLoadMapping(): ParameterizedTypeName {

        //<out Serializable>
        val outSerializable = WildcardTypeName.producerOf(ClassName("java.io", "Serializable"))

        //Route<out Serializable,out Serializable>
        val route = ClassName(
            ABS_CLASS_ROUTE_PACKAGE,
            ABS_CLASS_ROUTE_NAME
        ).parameterizedBy(
            outSerializable,
            outSerializable
        )

        //<out Activity>
        val outActivity = WildcardTypeName.producerOf(ClassName("android.app", "Activity"))

        //KClass<out Activity>
        val kClass = ClassName("kotlin.reflect", "KClass").parameterizedBy(outActivity)

        //MutableMap<Route<out Serializable, out Serializable>, KClass<out Activity>>
        return ClassName("kotlin.collections", "MutableMap").parameterizedBy(route, kClass)
    }

    companion object {
        private const val KEY_MODULE_NAME = "ROUTEUR_MODULE_NAME"
    }
}