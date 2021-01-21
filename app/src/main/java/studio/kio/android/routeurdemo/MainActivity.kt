package studio.kio.android.routeurdemo

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import studio.kio.android.common.DemoRoute
import studio.kio.android.common.ListRoute
import studio.kio.android.routeur.api.Routeur

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var index = 0
        findViewById<Button>(R.id.to_list).setOnClickListener {
            Routeur.navigateTo(this@MainActivity, ListRoute, index)
                .onReturn {
                    Toast.makeText(
                        this@MainActivity,
                        "Back from list $it",
                        Toast.LENGTH_LONG
                    ).show()
                    index = it
                }
        }

        findViewById<Button>(R.id.to_demo).setOnClickListener {
            Routeur.navigateTo(this@MainActivity, DemoRoute)
        }
    }

}