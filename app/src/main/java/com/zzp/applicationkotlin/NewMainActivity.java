package com.zzp.applicationkotlin;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import kotlinx.coroutines.GlobalScope;

/**
 * Created by samzhang on 2021/2/25.
 */
public class NewMainActivity extends AppCompatActivity {

    private float dip;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dip = getResources().getDisplayMetrics().density;

        ListView listView = new ListView(this);

        TitleAdapter adapter = new TitleAdapter();

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this,InstrumentationActivity.class);
                    startActivity(intent);
                }else if(position == 1){
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this,JobActivity.class);
                    startActivity(intent);
                }else if(position == 2){
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this,ForegroundServiceActivity.class);
                    startActivity(intent);
                }else if(position == 3){
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this,TrafficActivity.class);
                    startActivity(intent);
                }else if(position == 4){
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this,AddWindowActivity.class);
                    startActivity(intent);
                }else if(position == 5){
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this,WebViewActivity.class);
                    startActivity(intent);
                }else if(position == 6){
                    if(ContextCompat.checkSelfPermission(NewMainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        ComponentName componentName = new ComponentName(getPackageName(), "com.zzp.applicationkotlin.StaticWallpaper");
                        Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
                        intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, componentName);
                        startActivity(intent);
                    }else{
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},0x123);
                    }
                }else if(position == 7){
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this,ShareWeixinActivity.class);
                    startActivity(intent);
                }else if(position == 8){
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this,DollMachineActivity.class);
                    startActivity(intent);
                }else if(position == 9){
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this,KotlinActivity.class);
                    startActivity(intent);
                }else if(position == 10){
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this,VideoPlayerActivity.class);
                    startActivity(intent);
                }else if(position == 11){
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this,ViewPager2Activity.class);
                    startActivity(intent);
                }else if(position == 12){
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this,RoomActivity.class);
                    startActivity(intent);
                }
            }
        });

        setContentView(listView);
    }

    class TitleAdapter extends BaseAdapter{

        private String[] titles = new String[]{"Instrumentation","job","startService","traffic","addWindow","webView","动态壁纸","微信分享","娃娃机","kotlin","video","viewPager2","room"};

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public Object getItem(int position) {
            return titles[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = new TextView(NewMainActivity.this);
            textView.setText(titles[position]);
            textView.setTextSize(20);
            int padding = (int) (15 * dip);
            textView.setPadding(padding,padding,0,padding);
            textView.setTextColor(Color.BLUE);
            return textView;
        }
    }
}
