package studio.kio.android.routeurdemo

import android.app.Application
import studio.kio.android.routeur.api.Routeur

/**
 * created by KIO on 2021/1/20
 */
@Suppress("unused")
class RouteurApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Routeur.init(this)
    }

}