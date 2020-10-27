package studio.kio.ruater.api.route

import studio.kio.ruater.api.route.Route
import kotlin.reflect.KClass

/**
 * created by KIO on 2020/10/26
 */

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
@Repeatable
annotation class Destination(val value: KClass<out Route<*, *>>)