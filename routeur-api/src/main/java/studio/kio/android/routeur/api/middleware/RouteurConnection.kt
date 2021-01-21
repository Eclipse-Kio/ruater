package studio.kio.android.routeur.api.middleware

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.FragmentActivity
import studio.kio.android.routeur.api.Routeur
import java.io.Serializable
import java.lang.ref.WeakReference

/**
 * created by KIO on 2020/10/13
 */

@Suppress("UNCHECKED_CAST")
class RouteurConnection<T : Serializable> {

    private var contextReference: WeakReference<FragmentActivity>? = null

    private var callback: ((T) -> Unit)? = null

    private val activityResultProxyFragment = ActivityResultProxyFragment(this)

    fun onReturn(callback: (T) -> Unit) {
        this.callback = callback
    }

    @SuppressWarnings("UNCHECKED")
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (!activityResultProxyFragment.isDetached) {
            contextReference?.get()
                ?.supportFragmentManager
                ?.beginTransaction()
                ?.remove(activityResultProxyFragment)
                ?.commit()
        }

        if (requestCode == Routeur.ROUTEUR_CODE && resultCode == Activity.RESULT_OK) {
            callback?.invoke(data?.getSerializableExtra(Routeur.ROUTEUR_KEY) as T)
        }

    }

    fun connectTo(context: Context): Jumper {

        if (this.contextReference != null) {
            throw IllegalStateException("RuaterConnection Can connect only one time")
        }

        if (context !is FragmentActivity) {
            throw IllegalArgumentException("When Output Type is not EmptyOutput, context $context must be an instance of FragmentActivity")
        }

        contextReference = WeakReference(context)

        contextReference?.get()?.supportFragmentManager?.beginTransaction()
            ?.add(0, activityResultProxyFragment)
            ?.commitNow()

        return object : Jumper {
            override fun jumpTo(intent: Intent) {
                activityResultProxyFragment.startActivityForResult(
                    intent,
                    Routeur.ROUTEUR_CODE
                )
            }
        }
    }

}