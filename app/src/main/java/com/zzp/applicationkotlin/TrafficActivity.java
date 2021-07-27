package com.zzp.applicationkotlin;

import android.app.AppOpsManager;
import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.os.RemoteException;
import android.provider.Settings;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
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
    private TelephonyManager mTelephonyManager;

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
        mTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mNetworkStatsManager.registerUsageCallback(ConnectivityManager.TYPE_WIFI, null, 100, mUsageCallback);
        }

        Log.i(TAG, "Total1: " + (TrafficStats.getMobileRxBytes() + TrafficStats.getMobileTxBytes())/1024/1024 + "m");
        Log.i(TAG, "Total2: " + (TrafficStats.getTotalTxBytes() + TrafficStats.getTotalRxBytes())/1024/1024 + "m");

        checkTitle();
        getTotalRecTraffic();
        ThreadPoolUtil.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                getUidByPackageName(TrafficActivity.this);
            }
        });
        mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        getMobileDbm();
        if(!hasPermissionToReadNetworkStats()){
            requestReadNetworkStats();
        }
    }

    public void getMobileDbm() {
        int dbm = -1;
        TelephonyManager tm = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        List<CellInfo> cellInfoList = tm.getAllCellInfo();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
        {
            if (null != cellInfoList)
            {
                for (CellInfo cellInfo : cellInfoList)
                {
                    if (cellInfo instanceof CellInfoGsm)
                    {
                        CellSignalStrengthGsm cellSignalStrengthGsm = ((CellInfoGsm)cellInfo).getCellSignalStrength();
                        dbm = cellSignalStrengthGsm.getDbm();
                        Log.e("getMobileDbm", "cellSignalStrengthGsm" + cellSignalStrengthGsm.toString());
                    }
                    else if (cellInfo instanceof CellInfoCdma)
                    {
                        CellSignalStrengthCdma cellSignalStrengthCdma = ((CellInfoCdma)cellInfo).getCellSignalStrength();
                        dbm = cellSignalStrengthCdma.getDbm();
                        Log.e("getMobileDbm", "cellSignalStrengthCdma" + cellSignalStrengthCdma.toString() );
                    }
                    else if (cellInfo instanceof CellInfoWcdma)
                    {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
                        {
                            CellSignalStrengthWcdma cellSignalStrengthWcdma = ((CellInfoWcdma)cellInfo).getCellSignalStrength();
                            dbm = cellSignalStrengthWcdma.getDbm();
                            Log.e("getMobileDbm", "cellSignalStrengthWcdma" + cellSignalStrengthWcdma.toString() );
                        }
                    }
                    else if (cellInfo instanceof CellInfoLte)
                    {
                        CellSignalStrengthLte cellSignalStrengthLte = ((CellInfoLte)cellInfo).getCellSignalStrength();
                        dbm = cellSignalStrengthLte.getDbm();
                        Log.e("getMobileDbm", "cellSignalStrengthLte.getAsuLevel()\t" + cellSignalStrengthLte.getAsuLevel() );
                        Log.e("getMobileDbm", "cellSignalStrengthLte.getCqi()\t" + cellSignalStrengthLte.getCqi() );
                        Log.e("getMobileDbm", "cellSignalStrengthLte.getDbm()\t " + cellSignalStrengthLte.getDbm() );
                        Log.e("getMobileDbm", "cellSignalStrengthLte.getLevel()\t " + cellSignalStrengthLte.getLevel() );
                        Log.e("getMobileDbm", "cellSignalStrengthLte.getRsrp()\t " + cellSignalStrengthLte.getRsrp() );
                        Log.e("getMobileDbm", "cellSignalStrengthLte.getRsrq()\t " + cellSignalStrengthLte.getRsrq() );
                        Log.e("getMobileDbm", "cellSignalStrengthLte.getRssnr()\t " + cellSignalStrengthLte.getRssnr() );
                        Log.e("getMobileDbm", "cellSignalStrengthLte.getTimingAdvance()\t " + cellSignalStrengthLte.getTimingAdvance() );
                    }
                }
            }
        }
    }


    private PhoneStateListener mPhoneStateListener = new PhoneStateListener(){
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            Log.d(TAG,"onSignalStrengthsChanged:" + signalStrength);
            Log.d(TAG,"onSignalStrengthsChanged:" + signalStrength.getLevel());

        }
    };

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
            Log.e(TAG, "Total1: " + (bucket.getRxBytes() + bucket.getTxBytes())/1024/1024 + "m");

            bucket = mNetworkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_MOBILE, null, getTimesMonthMorning(), System.currentTimeMillis());
            Log.e(TAG, "Total2: " + (bucket.getRxBytes() + bucket.getTxBytes())/1024/1024 + "m");

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public long getRecKugouTraffic2(String name,int uuid){
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
        if(!hasPermissionToReadNetworkStats()){
            return;
        }
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
        mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);
    }
}
