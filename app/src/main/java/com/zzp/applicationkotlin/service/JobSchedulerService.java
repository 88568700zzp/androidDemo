package com.zzp.applicationkotlin.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;


/**
 * Created by samzhang on 2021/2/25.
 */
public class JobSchedulerService extends JobService {

    private static final String TAG = "JobSchedulerService";
    private Handler mHandler;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"onStartCommand");
        return START_NOT_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG,"onStartJob");
        Toast.makeText(this,"onStartJob 66",Toast.LENGTH_LONG).show();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                jobFinished(params,false);
            }
        },5000);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG,"onStopJob");
        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler();
        Log.d(TAG,"onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
    }
}
