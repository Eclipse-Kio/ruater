@file:Suppress("unused")

package studio.kio.android.routeur.api

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import studio.kio.android.routeur.api.exception.NoDestinationFoundException
import studio.kio.android.routeur.api.gen.RouterMapping
import studio.kio.android.routeur.api.middleware.RouteurConnection
import studio.kio.android.routeur.api.route.Empty
import studio.kio.android.routeur.api.route.Route
import java.io.Serializable

/**
 * created by KIO on 2020/10/26
 */
object Routeur {

    const val ROUTEUR_KEY = "ROUTEUR_INTERNAL_KEY"
    const val ROUTEUR_CODE = 80

    inline fun <reified I : Serializable, reified O : Serializable> navigateTo(
        context: Context,
        route: Route<I, O>,
        parameter: I,
        startModeFlag: Int = 0
    ): RouteurConnection<O> {

        val connection = RouteurConnection<O>()

        val destination = RouterMapping.getActivityClassWithRoute(route)
        if (destination != null) {

            val intent = Intent(context, destination.java)
            intent.apply {
                addFlags(startModeFlag)
                if (I::class != Empty::class) {
                    intent.putExtra(ROUTEUR_KEY, parameter)
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
    ): RouteurConnection<O> {
        return navigateTo(context, route, Empty, startModeFlag)
    }

    inline fun <reified T : Serializable> getParameter(
        intent: Intent,
        route: Route<T, out Serializable>
    ): T {
        Log.d("Routeur", route.toString())
        if (T::class == Empty::class) {
            throw IllegalArgumentException("Can't get parameter from an Route with input type <EmptyInput>")
        }
        return T::class.java.cast(intent.getSerializableExtra(ROUTEUR_KEY))!!
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
        i.putExtra(ROUTEUR_KEY, data)
        activity.setResult(Activity.RESULT_OK, i)
    }

    fun init(context: Context) {
        Log.e("TAG", "init: Router")
        RouterMapping.register(context)
    }

}