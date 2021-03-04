package com.zzp.applicationkotlin.adapter;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zzp.applicationkotlin.R;
import com.zzp.applicationkotlin.model.TrafficData;

import java.util.List;

/**
 * Created by samzhang on 2021/3/4.
 */
public class TrafficAdapter extends RecyclerView.Adapter<TrafficAdapter.TrafficHolder> {

    private List<TrafficData> mData;
    private Activity mActivity;
    private PackageManager mPackageManager;

    public TrafficAdapter(Activity mActivity,PackageManager packageManager,List<TrafficData> data) {
        this.mActivity = mActivity;
        mPackageManager = packageManager;
        mData = data;
    }

    @NonNull
    @Override
    public TrafficHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = mActivity.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.traffic_list_item,null);
        TrafficHolder holder = new TrafficHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TrafficHolder holder, int position) {
        TrafficData data = mData.get(position);
        holder.imageView.setImageDrawable(mPackageManager.getApplicationIcon(data.getApplicationInfo()));
        holder.name.setText(mPackageManager.getApplicationLabel(data.getApplicationInfo()));
        holder.content.setText("消耗：" + data.getTotal() + "m");
    }

    @Override
    public int getItemCount() {
        if(mData == null) {
            return 0;
        }else{
            return mData.size();
        }
    }

    class TrafficHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public TextView name,content;

        public TrafficHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            content = itemView.findViewById(R.id.content);
        }
    }
}
