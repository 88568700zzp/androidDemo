package com.zzp.watchapplication

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_list.*

class ListActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        var layoutManager =  LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = ViewAdapter(this)
    }

    class ViewAdapter(val activity:Activity):RecyclerView.Adapter<VH>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            var view = activity.layoutInflater.inflate(R.layout.list_item,parent,false)
            var vh = VH(view)
            return vh
        }


        override fun getItemCount(): Int {
            return 10
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            holder.textView.text = "position:${position}"
        }

    }

    class VH(itemView: View):RecyclerView.ViewHolder(itemView){
        var textView:TextView

        init {
            textView = itemView.findViewById(R.id.textView)
        }
    }
}