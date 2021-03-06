package studio.kio.android.routeur.api.gen

import android.app.Activity
import studio.kio.android.routeur.api.route.Route
import java.io.Serializable
import kotlin.reflect.KClass

/**
 * created by KIO on 2020/10/26
 */
interface IRouterMapper {
    fun loadMapping(map: MutableMap<Route<out Serializable, out Serializable>, KClass<out Activity>>)
}