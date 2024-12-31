package com.zzp.applicationkotlin.adapter;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zzp.applicationkotlin.R;
import com.zzp.applicationkotlin.model.TrafficData;

import java.util.List;
import java.util.Random;

public class SingleFragmentAdapter extends RecyclerView.Adapter<SingleFragmentAdapter.SingleHolder>{

    private Activity mActivity;

    private int beginIndex = 1;

    public SingleFragmentAdapter(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void randomBeginIndex(){
        beginIndex = new Random().nextInt(10) + 1;
    }

    @NonNull
    @Override
    public SingleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mActivity.getLayoutInflater().inflate(R.layout.single_list_item,parent,false);
        SingleHolder singleHolder = new SingleHolder(view);
        return singleHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SingleHolder holder, int position) {
        holder.name.setText("zzp:" + (position + beginIndex));
        holder.content.setText("content:" + (position + beginIndex));
        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mActivity,"content",Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return 10;
    }

    class SingleHolder extends RecyclerView.ViewHolder{

        public TextView name;
        public TextView content;

        public SingleHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            content = itemView.findViewById(R.id.content);
        }
    }
}
