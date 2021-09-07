package com.zzp.applicationkotlin;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zzp.applicationkotlin.view.BallTouchView;
import com.zzp.applicationkotlin.view.WifiReportCountView;

/**
 * Created by samzhang on 2021/8/20.
 */
public class TouchViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frameLayout = new FrameLayout(this);
        BallTouchView view = new BallTouchView(this,null);
        view.setMCallBack(new BallTouchView.ICallBack() {
            @Override
            public void onExpandClick() {
                Toast.makeText(getApplicationContext(),"onExpandClick",Toast.LENGTH_LONG).show();
            }
        });
        frameLayout.addView(view,new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        WifiReportCountView reportView = new WifiReportCountView(this,null);
        reportView.resetLeftTime(5000002);
        frameLayout.addView(reportView,new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        setContentView(frameLayout);
    }
}
