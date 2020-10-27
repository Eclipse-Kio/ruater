package studio.kio.ruater.demo.common

import android.app.Activity
import studio.kio.ruater.demo.b.ListActivity
import studio.kio.ruater.demo.c.DemoActivity
import studio.kio.ruater.api.gen.IRouterMapper
import studio.kio.ruater.api.route.Route
import java.io.Serializable
import kotlin.reflect.KClass

/**
 * created by KIO on 2020/10/26
 */

class RouterMapperImpl : IRouterMapper {
    override fun loadMapping(map: MutableMap<Route<out Serializable, out Serializable>, KClass<out Activity>>) {
        map[DemoRoute] = DemoActivity::class
        map[ListRoute] = ListActivity::class
    }
}