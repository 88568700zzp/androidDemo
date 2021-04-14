package com.zzp.applicationkotlin.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zzp.applicationkotlin.R;

/**
 * Created by samzhang on 2021/4/14.
 */
public class ViewPager2Adapter extends RecyclerView.Adapter<Holder>{

    private String[] list = new String[]{"fdsa1","fdafdas2","rewrewqre3","fdsa4","fdafdas5","rewrewqre6"
    };

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_pager2_item,parent,false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.textView.setText(list[position]);
    }

    @Override
    public int getItemCount() {
        return list.length;
    }
}

 class Holder extends RecyclerView.ViewHolder{

    public TextView textView;

    public Holder(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.title);
    }
}
