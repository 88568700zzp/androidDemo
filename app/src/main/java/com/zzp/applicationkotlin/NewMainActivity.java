package com.zzp.applicationkotlin;

import android.Manifest;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.zzp.applicationkotlin.util.TimeMonitor;

import java.util.List;

import kotlinx.coroutines.GlobalScope;

/**
 * Created by samzhang on 2021/2/25.
 */
public class NewMainActivity extends AppCompatActivity {

    private float dip;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        Log.d("zzp","displayMetrics:" + displayMetrics.toString());
        //displayMetrics.scaledDensity = displayMetrics.density * 2;

        super.onCreate(savedInstanceState);

        DisplayMetrics displayMetric =  getResources().getDisplayMetrics();

        dip = displayMetric.density;

        Log.d("zzp","displayMetrics:" + displayMetric.toString());

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
                }else if(position == 13){
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this,NotifyActivity.class);
                    startActivity(intent);
                }else if(position == 14){
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this,FaceTestWifi.class);
                    startActivity(intent);
                }else if(position == 15){
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this,ProviderActivity.class);
                    startActivity(intent);
                }else if(position == 16){
                    boolean result = false;
                    AccessibilityManager mAccessibilityManager = (AccessibilityManager)getSystemService(Context.ACCESSIBILITY_SERVICE);
                    List<AccessibilityServiceInfo> accessibilityServices =
                            mAccessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK);
                    for (AccessibilityServiceInfo info : accessibilityServices) {
                        if (info.getId().contains(".AutoAccessibilityService")) {
                            result = true;
                        }
                    }
                    if(result){
                        Toast.makeText(NewMainActivity.this,"已申请成功",Toast.LENGTH_SHORT).show();
                        //requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0x123);
                    }else {
                        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }else if(position == 17){
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this,HookServiceActivity.class);
                    startActivity(intent);
                }else if(position == 18){
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this,BitmapActivity.class);
                    startActivity(intent);
                }else if(position == 19){
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this,TouchViewActivity.class);
                    startActivity(intent);
                }
            }
        });

        setContentView(listView);

        Log.d("zzp","listView getDescendantFocusability:" + (listView.getDescendantFocusability() == ViewGroup.FOCUS_BEFORE_DESCENDANTS));
    }

    class TitleAdapter extends BaseAdapter{

        private String[] titles = new String[]{"Instrumentation","job","startService","traffic","addWindow","webView","动态壁纸","微信分享","娃娃机","kotlin","video","viewPager2","room","通知栏","FaceTestWifi","provider","辅助服务","hookService","bitmap","touchBall"};

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

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("globalTimeMonitor","onResume");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        TimeMonitor.getGlobalInstance().record("onWindowFocusChanged hasFocus:" + hasFocus);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("globalTimeMonitor","onPause");
    }

}
