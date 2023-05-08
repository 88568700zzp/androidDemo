package com.zzp.applicationkotlin

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zzp.applicationkotlin.view.doll.dp
import kotlinx.android.synthetic.main.activity_recycler_view.*

class RecyclerViewActivity : AppCompatActivity() {

    private var adapter = StringAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        test1_btn.setOnClickListener{
            adapter.notifyItemChanged(2)
        }

        test2_btn.setOnClickListener{
            adapter.notifyDataSetChanged()
        }
    }

    class StringAdapter(var context:Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

        private var titles = ArrayList<String>()

        init {
            for(index in 0..100){
                titles.add("index:${index}")
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
           var parent = LinearLayout(context)

            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,200.dp).let {
                parent.layoutParams = it
                parent.addView(TextView(context))
            }
            return object : RecyclerView.ViewHolder(parent) {}
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            with(holder.itemView as LinearLayout){
                if(position % 2 == 0){
                    setBackgroundColor(Color.RED)
                }else{
                    setBackgroundColor(Color.LTGRAY)
                }
                (this.getChildAt(0) as TextView).text = titles[position]
            }
        }

        override fun getItemCount(): Int {
            return titles.size
        }

        override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
            super.onViewRecycled(holder)
            Log.d("zzp1234","onViewRecycled:${holder.toString()}")
        }

        override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
            super.onViewAttachedToWindow(holder)
            Log.d("zzp1234","onViewAttachedToWindow:${holder.toString()}")
        }

        override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
            super.onViewDetachedFromWindow(holder)
            Log.d("zzp1234","onViewDetachedFromWindow:${holder.toString()}")
        }
    }
}