package com.zzp.applicationkotlin;

import android.Manifest;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.WallpaperManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.print.PrintManager;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.print.PrintHelper;

import com.amap.api.maps.MapsInitializer;
import com.zzp.applicationkotlin.dialog.EditDialog;
import com.zzp.applicationkotlin.model.SwipeData;
import com.zzp.applicationkotlin.util.TimeMonitor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by samzhang on 2021/2/25.
 */
public class NewMainActivity extends AppCompatActivity {

    private String TAG = "NewMainActivityTag_";

    private float dip;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        Log.d("zzp", "displayMetrics:" + displayMetrics.toString());
        //displayMetrics.scaledDensity = displayMetrics.density * 2;
        Log.d("zzp", "RELEASE:" + Build.VERSION.RELEASE);
        super.onCreate(savedInstanceState);

        DisplayMetrics displayMetric = getResources().getDisplayMetrics();

        dip = displayMetric.density;

        /*String type = getResources().getResourceTypeName(0x7f0800b7);
        String name = getResources().getResourceEntryName(0x7f0800b7);
        String resourceName = getResources().getResourceName(0x7f0800b7);
        TypedValue typedValue = new TypedValue();
        getResources().getValue(0x7f0c0015,typedValue,true);
        Log.d("zzp_resource","type:" + typedValue.toString());
        Log.d("zzp_resource","name:" + name + " type:"+ type + " resourceName:" + resourceName);
        Log.d("zzp_resource","id:" + Integer.toHexString(getResources().getIdentifier(name,type,getPackageName())));*/

        Log.d("zzp", "density:" + displayMetric.density + " densityDpi:" + displayMetric.densityDpi);
        Log.d("zzp", "displayMetrics:" + displayMetric.toString());

        //Log.d("zzp","ndk:" + NDKTool.getStringFromNDK());

        ListView listView = new ListView(this);

        listView.setBackgroundColor(Color.WHITE);

        TitleAdapter adapter = new TitleAdapter();

        listView.setAdapter(adapter);

        listView.animate().alphaBy(10f).setDuration(1000).start();

        Log.d("zzp123", "device:" + Build.DEVICE + " model:" + Build.MODEL + " MANUFACTURER:" + Build.MANUFACTURER + " BOARD:" + Build.BOARD + " BRAND:" + Build.BRAND);

        Log.d("zzp123", "device:" + Settings.Secure.getString(getContentResolver(), "bluetooth_name"));

        Log.d("zzp123", "device:" + Settings.Global.getString(getContentResolver(), Settings.Global.DEVICE_NAME));

        Log.d("zzp123", "device:" + BluetoothAdapter.getDefaultAdapter().getName());

        Log.d("zzp123", "appName:" + getResources().getString(getApplication().getApplicationInfo().labelRes));

        Log.d("zzp123", "force_fsg_nav_bar:" + Settings.Global.getInt(getContentResolver(), "force_fsg_nav_bar", 0));


        //BitmapkitUtils.load(getBaseContext());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this, InstrumentationActivity.class);
                    startActivity(intent);
                } else if (position == 1) {
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this, JobActivity.class);
                    startActivity(intent);
                } else if (position == 2) {
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this, ForegroundServiceActivity.class);
                    startActivity(intent);
                } else if (position == 3) {
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this, TrafficActivity.class);
                    startActivity(intent);
                } else if (position == 4) {
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this, AddWindowActivity.class);
                    startActivity(intent);
                } else if (position == 5) {
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this, WebViewActivity.class);
                    startActivity(intent);
                } else if (position == 6) {
                    if (ContextCompat.checkSelfPermission(NewMainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        ComponentName componentName = new ComponentName(getPackageName(), "com.zzp.applicationkotlin.StaticWallpaper");
                        Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
                        intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, componentName);
                        startActivity(intent);
                    } else {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x123);
                    }
                } else if (position == 7) {
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this, ShareWeixinActivity.class);
                    startActivity(intent);
                } else if (position == 8) {
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this, DollMachineActivity.class);
                    startActivity(intent);
                } else if (position == 9) {
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this, KotlinActivity.class);
                    startActivity(intent);
                } else if (position == 10) {
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this, VideoPlayerActivity.class);
                    startActivity(intent);
                } else if (position == 11) {
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this, ViewPagerActivity.class);
                    startActivity(intent);
                } else if (position == 12) {
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this, RoomActivity.class);
                    startActivity(intent);
                } else if (position == 13) {
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this, NotifyActivity.class);
                    startActivity(intent);
                } else if (position == 14) {
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this, FaceTestWifi.class);
                    startActivity(intent);
                } else if (position == 15) {
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this, ProviderActivity.class);
                    startActivity(intent);
                } else if (position == 16) {
                    boolean result = false;
                    AccessibilityManager mAccessibilityManager = (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
                    List<AccessibilityServiceInfo> accessibilityServices =
                            mAccessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK);
                    for (AccessibilityServiceInfo info : accessibilityServices) {
                        if (info.getId().contains(".DealAccessibilityService")) {
                            result = true;
                        }
                    }
                    if (result) {
                        Toast.makeText(NewMainActivity.this, "已申请成功", Toast.LENGTH_SHORT).show();
                        //requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0x123);
                    } else {
                        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                } else if (position == 17) {
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this, HookServiceActivity.class);
                    startActivity(intent);
                } else if (position == 18) {
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this, BitmapActivity.class);
                    startActivity(intent);
                } else if (position == 19) {
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this, TouchViewActivity.class);
                    startActivity(intent);
                } else if (position == 20) {
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this, TestDtkActivity.class);
                    startActivity(intent);
                } else if (position == 21) {
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this, ShareActivity.class);
                    startActivity(intent);
                } else if (position == 22) {
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this, NestScrollActivity.class);
                    startActivity(intent);
                } else if (position == 23) {
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this, CoordinatorActivity.class);
                    startActivity(intent);
                } else if (position == 24) {
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this, RxJavaActivity.class);
                    startActivity(intent);
                } else if (position == 25) {
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this, GreenDaoActivity.class);
                    startActivity(intent);
                } else if (position == 26) {
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this, FingerActivity.class);
                    startActivity(intent);
                } else if (position == 27) {
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this, NightActivity.class);
                    startActivity(intent);
                } else if (position == 28) {
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this, ImageEditActivity.class);
                    startActivity(intent);
                } else if (position == 29) {
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this, FormatActivity.class);
                    startActivity(intent);
                } else if (position == 30) {
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this, PdfViewActivity.class);
                    startActivity(intent);
                } else if (position == 31) {
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this, PrintActivity.class);
                    startActivity(intent);
                } else if (position == 32) {
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this, RecyclerViewActivity.class);
                    startActivity(intent);
                } else if (position == 33) {
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this, SensorActivity.class);
                    startActivity(intent);
                } else if (position == 34) {
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this, AMapActivity.class);
                    startActivity(intent);
                }else if (position == 35) {
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this, TestFragmentActivity.class);
                    startActivity(intent);
                }
                /*else if(position == 22){
                    Intent intent = new Intent();
                    intent.setClass(NewMainActivity.this,XmlActivity.class);
                    startActivity(intent);
                }*/
            }
        });

        setContentView(listView);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("zzp1234", "Looper name:" + Thread.currentThread().getName());

                Looper.prepare();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("zzp1234", "name:" + Thread.currentThread().getName());
                        handler.getLooper().quitSafely();
                    }
                }, 1000);
                Looper.loop();
                Log.d("zzp1234", "end");
            }
        }, "zzp-thread");
        thread.start();


        MapsInitializer.updatePrivacyShow(this, true, true);
        MapsInitializer.updatePrivacyAgree(this, true);
    }
    class TitleAdapter extends BaseAdapter {

        private String[] titles = new String[]{"Instrumentation", "job", "startService", "traffic", "addWindow", "webView",
                "动态壁纸", "微信分享", "娃娃机", "kotlin", "video", "viewPager2", "room", "通知栏", "FaceTestWifi", "provider", "辅助服务",
                "hookService", "bitmap", "touchBall", "大淘客", "分享", "nestScroll", "Coordinator", "Rxjava", "GreenDao","figger","night"
                ,"imageEdit","文档解析","pdf","print","recyclerView","sensor","map","testFragment"};

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
            textView.setPadding(padding, padding, 0, padding);
            textView.setTextColor(Color.BLUE);

            return textView;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("globalTimeMonitor", "onResume");
        Log.d(TAG, "onResume");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        TimeMonitor.getGlobalInstance().record("onWindowFocusChanged hasFocus:" + hasFocus);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("globalTimeMonitor", "onPause");
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
