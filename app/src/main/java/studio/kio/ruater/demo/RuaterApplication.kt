package studio.kio.demo

import android.app.Application
import studio.kio.ruater.api.Ruater

/**
 * created by KIO on 2020/10/26
 */
class RuaterApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Ruater.init(this)
    }
}