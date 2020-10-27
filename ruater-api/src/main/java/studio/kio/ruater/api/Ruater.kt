package studio.kio.ruater.api

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import studio.kio.ruater.api.exception.NoDestinationFoundException
import studio.kio.ruater.api.gen.RouterMapping
import studio.kio.ruater.api.middleware.RuaterConnection
import studio.kio.ruater.api.route.Empty
import studio.kio.ruater.api.route.Route
import java.io.Serializable

/**
 * created by KIO on 2020/10/26
 */

/**
 * 字符串常量，用做传递parameter的key
 */

object Ruater {

    const val RUATER_KEY = "_\$RUATER_INTERNAL_KEY"
    const val RUATER_CODE = 80

    inline fun <reified I : Serializable, reified O : Serializable> navigateTo(
        context: Context,
        route: Route<I, O>,
        parameter: I,
        startModeFlag: Int = 0
    ): RuaterConnection<O> {

        val connection = RuaterConnection<O>()

        val destination = RouterMapping.getActivityClassWithRoute(route)
        if (destination != null) {

            val intent = Intent(context, destination.java)
            intent.apply {
                addFlags(startModeFlag)
                if (I::class != Empty::class) {
                    intent.putExtra(RUATER_KEY, parameter)
                }
            }

            if (O::class == Empty::class) {
                context.startActivity(intent)
            } else {
                val jumper = connection.connectTo(context)
                jumper.jumpTo(intent)
            }

        } else {
            throw NoDestinationFoundException("No page found matching route $route")
        }
        return connection
    }

    inline fun <reified O : Serializable> navigateTo(
        context: Context,
        route: Route<Empty, O>,
        startModeFlag: Int = 0
    ): RuaterConnection<O> {
        return navigateTo(context, route, Empty, startModeFlag)
    }

    inline fun <reified T : Serializable> getParameter(
        intent: Intent,
        route: Route<T, out Serializable>
    ): T {
        Log.d("Ruater", route.toString())
        if (T::class == Empty::class) {
            throw IllegalArgumentException("Can't get parameter from an Route with input type <EmptyInput>")
        }
        return T::class.java.cast(intent.getSerializableExtra(RUATER_KEY))!!
    }

    inline fun <reified T : Serializable> setResult(
        activity: Activity,
        route: Route<out Serializable, T>, data: T
    ) {
        Log.d("Ruater", route.toString())
        if (T::class == Empty::class) {
            throw java.lang.IllegalArgumentException("Can't setReturn on an Route with output type <EmptyOutput>")
        }
        val i = Intent()
        i.putExtra(RUATER_KEY, data)
        activity.setResult(Activity.RESULT_OK, i)
    }

    fun init(app: Application) {
        RouterMapping.register()
    }

}