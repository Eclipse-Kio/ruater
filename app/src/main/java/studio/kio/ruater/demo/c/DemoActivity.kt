package studio.kio.ruater.demo.c

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import studio.kio.demo.R
import studio.kio.ruater.demo.common.DemoRoute
import studio.kio.ruater.api.Ruater
import studio.kio.ruater.api.route.RoutePath

@RoutePath(DemoRoute::class)
class DemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)

        val s = Ruater.getParameter(intent, DemoRoute)

        findViewById<TextView>(R.id.textView).text = "$s"
    }
}