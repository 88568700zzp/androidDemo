package com.zzp.applicationkotlin;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zzp.applicationkotlin.event.HitAnimateEvent;
import com.zzp.applicationkotlin.view.doll.DollMachineView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by samzhang on 2021/3/23.
 */
public class DollMachineActivity extends AppCompatActivity {

    private DollMachineView mDollMachineView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        setContentView(R.layout.activity_doll_machine);

        mDollMachineView = findViewById(R.id.doll_machine);
        mDollMachineView.initData(3000L,1500L);

        findViewById(R.id.doll_btn).setOnClickListener(v -> {
            mDollMachineView.doHitDoll();
        });
        findViewById(R.id.pause_btn).setOnClickListener(v -> {
            mDollMachineView.pauseDoll();
        });
        findViewById(R.id.resume_btn).setOnClickListener(v -> {
            mDollMachineView.resumeDoll();
        });
        findViewById(R.id.small_btn).setOnClickListener(v -> {
            mDollMachineView.changeClampShape(false);
        });
        findViewById(R.id.big_btn).setOnClickListener(v -> {
            mDollMachineView.changeClampShape(true);
        });
        findViewById(R.id.fast_btn).setOnClickListener(v -> {
            mDollMachineView.setMode(true,3000);
        });
        findViewById(R.id.slow_btn).setOnClickListener(v -> {
            mDollMachineView.setMode(false,3000);
        });
        findViewById(R.id.bomb_btn).setOnClickListener(v -> {
            mDollMachineView.bomp(3000);
        });
        //((DollMachineView)findViewById(R.id.doll_machine)).beginDollMachine();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDollMachineView.stopDollMachine();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(HitAnimateEvent event) {
        //mDollMachineView.pauseDoll();
    }
}
