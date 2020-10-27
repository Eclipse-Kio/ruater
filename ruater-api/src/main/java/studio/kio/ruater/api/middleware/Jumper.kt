package studio.kio.ruater.api.middleware

import android.content.Intent

/**
 * created by KIO on 2020/10/26
 */
interface Jumper {
    fun jumpTo(intent: Intent)
}