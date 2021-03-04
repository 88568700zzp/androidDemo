package com.zzp.applicationkotlin;

import android.app.AppOpsManager;
import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zzp.applicationkotlin.adapter.TrafficAdapter;
import com.zzp.applicationkotlin.model.TrafficData;
import com.zzp.applicationkotlin.util.ThreadPoolUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import static android.Manifest.permission.PACKAGE_USAGE_STATS;

/**
 * Created by samzhang on 2021/3/4.
 */
public class TrafficActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "TrafficActivity";

    private NetworkStatsManager mNetworkStatsManager;

    private List<TrafficData> mData = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private TrafficAdapter mAdapter;
    private TextView mTitle;
    private Switch mSwitch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic);

        mRecyclerView = findViewById(R.id.recyclerView);
        mTitle = findViewById(R.id.title);
        mSwitch = findViewById(R.id.switch_btn);
        mSwitch.setOnCheckedChangeListener(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new TrafficAdapter(this,getPackageManager(),mData);
        mRecyclerView.setAdapter(mAdapter);

        mNetworkStatsManager = (NetworkStatsManager) getSystemService(NETWORK_STATS_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mNetworkStatsManager.registerUsageCallback(ConnectivityManager.TYPE_WIFI, null, 100, mUsageCallback);
        }

        checkTitle();
        getTotalRecTraffic();
        ThreadPoolUtil.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                getUidByPackageName(TrafficActivity.this);
            }
        });
    }

    private NetworkStatsManager.UsageCallback mUsageCallback = new NetworkStatsManager.UsageCallback(){

        @Override
        public void onThresholdReached(int networkType, String subscriberId) {
            Log.d(TAG,"onThresholdReached subscriberId:" + subscriberId);
        }
    };

    private boolean hasPermissionToReadNetworkStats() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        final AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), getPackageName());
        if (mode == AppOpsManager.MODE_ALLOWED) {
            return true;
        }
        requestReadNetworkStats();
        return false;
    }
    // 打开“有权查看使用情况的应用”页面
    private void requestReadNetworkStats() {
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        startActivity(intent);
    }


    public void getTotalRecTraffic(){
        if(!hasPermissionToReadNetworkStats()){
            return;
        }
        NetworkStats.Bucket bucket = null;
        try {
            bucket = mNetworkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_WIFI, "", getTimesMonthMorning(), System.currentTimeMillis());
            Log.i(TAG, "Total: " + (bucket.getRxBytes() + bucket.getTxBytes())/1024/1024 + "m");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public long getRecKugouTraffic2(String name,int uuid){
        if(!hasPermissionToReadNetworkStats()){
            return 0;
        }
        try {
            int type = ConnectivityManager.TYPE_MOBILE;
            if(mSwitch.isChecked()){
                type= ConnectivityManager.TYPE_WIFI;
            }
            NetworkStats networkStats = mNetworkStatsManager.queryDetailsForUid(type,null , getTimesMonthMorning(), System.currentTimeMillis(),uuid);
            long summaryRx = 0;
            long summaryTx = 0;
            NetworkStats.Bucket summaryBucket = new NetworkStats.Bucket();
            do{
                networkStats.getNextBucket(summaryBucket);
                summaryRx += summaryBucket.getRxBytes();
                summaryTx += summaryBucket.getTxBytes();
                Log.e(TAG, "name:" + name + " summaryRx: " + (summaryBucket.getRxBytes())/1024/1024 + "  summaryTx:" + (summaryBucket.getTxBytes())/1024/1024 + " uid:" + summaryBucket.getUid());
            }while (networkStats.hasNextBucket());
            Log.e(TAG, "name:" + name + " total summaryRx: " + (summaryRx)/1024/1024 + "  summaryTx:" + (summaryTx)/1024/1024);
            return (summaryRx + summaryTx)/1024/1024;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long getTimesMonthMorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTimeInMillis();
    }

    private void checkTitle(){
        if(mSwitch.isChecked()){
            mTitle.setText("wifi流量消耗");
        }else{
            mTitle.setText("数据流量消耗");
        }
    }

    public void getUidByPackageName(Context context) {
        Log.d(TAG,"ThreadPoolUtil getUidByPackageName:" + Thread.currentThread().getName());
        try {
            PackageManager packageManager = context.getPackageManager();
            List<ApplicationInfo> results = packageManager.getInstalledApplications(PackageManager.MATCH_UNINSTALLED_PACKAGES);
            mData.clear();
            for(ApplicationInfo applicationInfo:results){
                if((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    int uid = applicationInfo.uid;
                    Log.i(TAG, applicationInfo.packageName + " uid:" + uid);
                    mData.add(new TrafficData(applicationInfo,getRecKugouTraffic2(packageManager.getApplicationLabel(applicationInfo).toString(),uid)));
                }
            }
            Collections.sort(mData, new Comparator<TrafficData>() {
                @Override
                public int compare(TrafficData o1, TrafficData o2) {
                    return (int) (o2.getTotal() - o1.getTotal());
                }
            });
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyDataSetChanged();
                }
            });

        } catch (Exception e) {
            Log.d(TAG,e.toString());
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG,"onRequestPermissionsResult");
        for(int i = 0;i < permissions.length;i++){
            if(permissions[i] == PACKAGE_USAGE_STATS && grantResults[i] == PackageManager.PERMISSION_GRANTED){
                getTotalRecTraffic();
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        checkTitle();
        ThreadPoolUtil.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                getUidByPackageName(TrafficActivity.this);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mNetworkStatsManager.unregisterUsageCallback(mUsageCallback);
        }
    }
}
