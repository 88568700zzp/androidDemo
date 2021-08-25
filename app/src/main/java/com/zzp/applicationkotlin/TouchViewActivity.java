package com.zzp.applicationkotlin;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zzp.applicationkotlin.view.BallTouchView;

/**
 * Created by samzhang on 2021/8/20.
 */
public class TouchViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BallTouchView view = new BallTouchView(this,null);
        view.setMCallBack(new BallTouchView.ICallBack() {
            @Override
            public void onExpandClick() {
                Toast.makeText(getApplicationContext(),"onExpandClick",Toast.LENGTH_LONG).show();
            }
        });
        setContentView(view);
    }
}
