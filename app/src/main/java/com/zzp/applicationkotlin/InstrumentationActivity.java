package com.zzp.applicationkotlin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zzp.applicationkotlin.instrumentation.InfoInstrumentation;

/**
 * Created by samzhang on 2021/2/25.
 */
public class InstrumentationActivity extends AppCompatActivity {

    private final String TAG = "InstrumentationActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instrumentation);

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    @Override
                    public void run() {
                        InfoInstrumentation infoInstrumentation = new InfoInstrumentation();
                        Intent intent = new Intent();
                        intent.setClassName(getPackageName(), InstrumentationActivity.class.getName());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        Activity activity = (Activity) infoInstrumentation.startActivitySync(intent);
                        Button button = (Button) activity.findViewById(R.id.btn2);
                        infoInstrumentation.runOnMainSync(new Runnable() {
                            @Override
                            public void run() {
                                button.performClick();
                            }
                        });
                    }
                }.start();

            }
        });

        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"btn2 doClick");
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("zzp","onKeyDown:" + keyCode);
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop");
    }
}
