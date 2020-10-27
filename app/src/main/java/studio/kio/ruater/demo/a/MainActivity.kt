package studio.kio.ruater.demo.a

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import studio.kio.demo.R
import studio.kio.ruater.api.Ruater
import studio.kio.ruater.api.route.RoutePath
import studio.kio.ruater.demo.common.DemoRoute
import studio.kio.ruater.demo.common.EmptyRoute
import studio.kio.ruater.demo.common.ListRoute
import studio.kio.ruater.demo.common.User

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.to_list).setOnClickListener(
            object : View.OnClickListener {
                var i = 0
                override fun onClick(v: View?) {
                    Ruater.navigateTo(this@MainActivity, ListRoute, i).onReturn {
                        Toast.makeText(this@MainActivity, "Back from list $it", Toast.LENGTH_LONG)
                            .show()
                        i = it
                    }
                }

            }
        )

        findViewById<Button>(R.id.to_demo).setOnClickListener {
            Ruater.navigateTo(this, DemoRoute, User("kio", 20))
        }

        findViewById<Button>(R.id.to_this).setOnClickListener {
            Ruater.navigateTo(this, EmptyRoute)
        }


    }

}