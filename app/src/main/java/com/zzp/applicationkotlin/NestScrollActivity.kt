package com.zzp.applicationkotlin

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_nest_scroll.*

/**
 *
 * Created by samzhang on 2021/11/23.
 */
class NestScrollActivity :AppCompatActivity(){

    private var adapter:PicApdater ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nest_scroll)

        adapter = PicApdater(this)

        var layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

     class PicApdater(val activity:AppCompatActivity) : RecyclerView.Adapter<StringHolder>(){
         override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StringHolder {
             var itemView = activity.layoutInflater.inflate(R.layout.nestscroll_list_item,null)
             return StringHolder(itemView)
         }

         override fun onBindViewHolder(holder: StringHolder, position: Int) {
             holder.textView.text = "item:${position + 1}"
         }

         override fun getItemCount(): Int {
             return 10
         }

     }

    class StringHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView:TextView = itemView.findViewById(R.id.textView)
    }
}