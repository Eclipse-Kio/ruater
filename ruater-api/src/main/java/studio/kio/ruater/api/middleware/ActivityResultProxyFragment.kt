package studio.kio.ruater.api.middleware

import android.content.Intent
import androidx.fragment.app.Fragment
import java.io.Serializable

/**
 * created by KIO on 2020/10/18
 */
internal class ActivityResultProxyFragment(private val connection: RuaterConnection<out Serializable>) :
    Fragment() {
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        connection.onActivityResult(requestCode, resultCode, data)
    }
}