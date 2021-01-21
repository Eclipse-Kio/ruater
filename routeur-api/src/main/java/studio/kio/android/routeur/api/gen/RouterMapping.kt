package studio.kio.android.routeur.api.gen

import android.app.Activity
import android.content.Context
import android.os.AsyncTask
import studio.kio.android.routeur.api.route.Route
import studio.kio.android.routeur.api.utils.getFileNameByPackageName
import java.io.Serializable
import java.lang.ref.WeakReference
import kotlin.reflect.KClass

/**
 * created by KIO on 2020/10/13
 */
object RouterMapping {

    private val map = HashMap<Route<out Serializable, out Serializable>, KClass<out Activity>>()

    fun <I : Serializable, O : Serializable> getActivityClassWithRoute(route: Route<I, O>): KClass<out Activity>? {
        return map[route]
    }

    fun register(context: Context) {
        LoadMappingTask(context).apply { execute(Unit) }
    }

    private class LoadMappingTask(context: Context) : AsyncTask<Unit, Unit, Unit>() {
        companion object {
            private const val MAPPER_PACKAGE = "studio.kio.routeur.gen.mappers"
        }

        private val context = WeakReference(context)

        override fun doInBackground(vararg params: Unit) {
            context.get()?.run {
                map.clear()
                getFileNameByPackageName(this, MAPPER_PACKAGE).forEach {
                    (Class.forName(it).newInstance() as IRouterMapper).loadMapping(map)
                }
            }
        }

        override fun onPostExecute(result: Unit?) {

        }
    }
}
