package com.zzp.applicationkotlin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.zzp.applicationkotlin.adapter.ViewPager2Adapter;
import com.zzp.applicationkotlin.view.tab.CustomTabLayout;

import java.util.ArrayList;
import java.util.List;

public class ViewPager2Activity extends AppCompatActivity {

    private CustomTabLayout mCustomTabLayout;
    private ViewPager2 mViewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager2);
        mCustomTabLayout = findViewById(R.id.tabLayout);
        mViewPager2 = findViewById(R.id.view_pager2);


        mCustomTabLayout.setTabListener(new CustomTabLayout.TabListener() {
            @Override
            public void onTabSelect(int index) {
                mViewPager2.setCurrentItem(index);
            }

            @Override
            public void onViewSelect(int index, View view) {
                if(view instanceof TextView){
                   TextView textView = ((TextView)view);
                    textView.setTextSize(23);
                }
            }
        });

        mViewPager2.setAdapter(new ViewPager2Adapter());
        mViewPager2.registerOnPageChangeCallback(pageChangeCallback);

        List<String> data = new ArrayList<>();
        for(int i = 0;i < 10;i++){
            data.add("data" + i);
        }

        mCustomTabLayout.setTab(data);
        mCustomTabLayout.setSelectIndex(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewPager2.unregisterOnPageChangeCallback(pageChangeCallback);
    }

    private ViewPager2.OnPageChangeCallback pageChangeCallback = new ViewPager2.OnPageChangeCallback(){
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            mCustomTabLayout.setSelectIndex(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            super.onPageScrollStateChanged(state);
        }
    };
}