package studio.kio.android.module_b

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import studio.kio.android.common.DemoRoute
import studio.kio.android.routeur.api.Routeur
import studio.kio.android.routeur.api.route.Destination

@Destination(DemoRoute::class)
class DemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)

//        val s = Routeur.getParameter(intent, DemoRoute)

//        findViewById<TextView>(R.id.textView).text = "$s"
    }
}