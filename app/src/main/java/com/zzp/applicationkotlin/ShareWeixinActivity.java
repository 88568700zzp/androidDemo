package com.zzp.applicationkotlin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelpay.JumpToOfflinePay;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zzp.applicationkotlin.constant.Constants;

/**
 * Created by samzhang on 2021/3/23.
 */
public class ShareWeixinActivity extends AppCompatActivity {

    private Button gotoBtn, regBtn, launchBtn, scanBtn, subscribeMsgBtn,subscribeMiniProgramMsgBtn;

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_wx);
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, false);

        regBtn = (Button) findViewById(R.id.reg_btn);
        regBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                api.registerApp(Constants.APP_ID);
            }
        });

        gotoBtn = (Button) findViewById(R.id.goto_send_btn);
        gotoBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //startActivity(new Intent(ShareWeixinActivity.this, SendToWXActivity.class));
//		        finish();
            }
        });

        launchBtn = (Button) findViewById(R.id.launch_wx_btn);
        launchBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(ShareWeixinActivity.this, "launch result = " + api.openWXApp(), Toast.LENGTH_LONG).show();
            }
        });

        subscribeMsgBtn = (Button) findViewById(R.id.goto_subscribe_message_btn);
        subscribeMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // startActivity(new Intent(ShareWeixinActivity.this, SubscribeMessageActivity.class));
//				finish();
            }
        });

        subscribeMiniProgramMsgBtn = (Button) findViewById(R.id.goto_subscribe_mini_program_msg_btn);
        subscribeMiniProgramMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // startActivity(new Intent(ShareWeixinActivity.this, SubscribeMiniProgramMsgActivity.class));
            }
        });


        View jumpToOfflinePay = (Button) findViewById(R.id.jump_to_offline_pay);
        jumpToOfflinePay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int wxSdkVersion = api.getWXAppSupportAPI();
                if (wxSdkVersion >= Build.OFFLINE_PAY_SDK_INT) {
                    api.sendReq(new JumpToOfflinePay.Req());
                }else {
                    Toast.makeText(ShareWeixinActivity.this, "not supported", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
