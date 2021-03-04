package com.zzp.addemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by samzhang on 2021/3/3.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void doClick(View view){
        if(view.getTag() .equals("1")){
            Intent intent = new Intent(this,TTMainActivity.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(this,GdtMainActivity.class);
            startActivity(intent);
        }
    }

}
