package studio.kio.android.routeur.api.route

import kotlin.reflect.KClass

/**
 * created by KIO on 2020/10/26
 */

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
@Repeatable
annotation class Destination(val value: KClass<out Route<*, *>>)