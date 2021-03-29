package com.zzp.applicationkotlin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.JumpToOfflinePay;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zzp.applicationkotlin.constant.Constants;
import com.zzp.applicationkotlin.view.doll.DollMachineView;

/**
 * Created by samzhang on 2021/3/23.
 */
public class DollMachineActivity extends AppCompatActivity {

    private DollMachineView mDollMachineView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doll_machine);

        mDollMachineView = findViewById(R.id.doll_machine);

        findViewById(R.id.doll_btn).setOnClickListener(v -> {
            mDollMachineView.doHitDoll();
        });
        findViewById(R.id.pause_btn).setOnClickListener(v -> {
            mDollMachineView.pauseDoll();
        });
        findViewById(R.id.resume_btn).setOnClickListener(v -> {
            mDollMachineView.resumeDoll();
        });

        //((DollMachineView)findViewById(R.id.doll_machine)).beginDollMachine();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDollMachineView.stopDollMachine();
    }
}
