package com.zzp.applicationkotlin.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.andview.refreshview.XRefreshView;
import com.zzp.applicationkotlin.R;
import com.zzp.applicationkotlin.adapter.SingleFragmentAdapter;

public class SingleFragment extends Fragment {

    private int orientation;
    private RecyclerView recyclerView;

    private XRefreshView refreshView;

    private SingleFragmentAdapter adapter;

    public SingleFragment(int orientation){
        this.orientation = orientation;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_single,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new SingleFragmentAdapter(getActivity());

        refreshView = view.findViewById(R.id.refreshView);
        refreshView.setPullRefreshEnable(true);
        refreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                adapter.randomBeginIndex();
                adapter.notifyDataSetChanged();
                refreshView.stopRefresh(true);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),orientation,false);

        recyclerView = view.findViewById(R.id.recycle);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }
}
