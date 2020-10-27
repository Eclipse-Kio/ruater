package studio.kio.ruater.api.gen

import android.app.Activity
import studio.kio.ruater.api.route.Route
import java.io.Serializable
import kotlin.reflect.KClass

/**
 * created by KIO on 2020/10/13
 */
object RouterMapping {

    private val map = HashMap<Route<out Serializable, out Serializable>, KClass<out Activity>>()

    fun <I : Serializable, O : Serializable> getActivityClassWithRoute(route: Route<I, O>): KClass<out Activity>? {
        return map[route]
    }

    fun register() {
        (Class.forName("studio.kio.ruater.api.gen.RouterMapperImpl")
            .newInstance() as IRouterMapper).loadMapping(map)
    }

}