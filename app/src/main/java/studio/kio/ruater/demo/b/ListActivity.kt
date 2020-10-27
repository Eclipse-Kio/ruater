package studio.kio.ruater.demo.b

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import studio.kio.demo.R
import studio.kio.ruater.api.Ruater
import studio.kio.ruater.api.route.Destination
import studio.kio.ruater.demo.common.ListRoute

@Destination(ListRoute::class)
class ListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        recyclerView = findViewById(R.id.rv_select)

        recyclerView.layoutManager = LinearLayoutManager(this)

        val data = listOf(
            "Daniel",
            "Lee",
            "Ronald",
            "Vaughan",
            "Gerald",
            "Erik",
            "Strawberry",
            "Gerald",
            "Ives",
            "Royal",
            "Crown",
            "Warren",
            "Philip",
            "Gilbert",
            "Rosemary",
            "Timothea"
        )

        val adapter = MyAdapter(data, Ruater.getParameter(intent, ListRoute))

        adapter.setOnItemClickListener { _, _, position, _ ->
            Ruater.setResult(this, ListRoute, position)
            finish()
        }

        recyclerView.adapter = adapter

    }

}

class MyAdapter(private val data: List<String>, private var activeIndex: Int?) :
    RecyclerView.Adapter<MyViewHolder>() {

    private var onItemClickListener: AdapterView.OnItemClickListener? = null

    fun setOnItemClickListener(onItemSelectedListener: AdapterView.OnItemClickListener) {
        this.onItemClickListener = onItemSelectedListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (position == activeIndex) {
            holder.textView.setTextColor(Color.parseColor("#FF4400"))
        } else {
            holder.textView.setTextColor(Color.parseColor("#000000"))
        }
        holder.textView.text = data[position]
        holder.textView.setOnClickListener {
            onItemClickListener?.onItemClick(null, it, position, it.id.toLong())
        }
    }

    override fun getItemCount() = data.size

}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textView: TextView = itemView.findViewById(android.R.id.text1)
}