package com.zzp.applicationkotlin;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.zzp.applicationkotlin.fragment.SingleFragment;

public class TestFragmentActivity extends AppCompatActivity implements View.OnClickListener{

    private SingleFragment singleFragment1;
    private SingleFragment singleFragment2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_fragment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (v.getId() == R.id.btn1) {
            if(singleFragment1 == null){
                singleFragment1 = new SingleFragment(RecyclerView.VERTICAL);
                fragmentTransaction.add(R.id.content, singleFragment1);
            }else{
                if(singleFragment1.isHidden()){
                    fragmentTransaction.show(singleFragment1);
                }
            }
            if(singleFragment2 != null && !singleFragment2.isHidden()){
                fragmentTransaction.hide(singleFragment2);
            }
            fragmentTransaction.commitNowAllowingStateLoss();
        }else if(v.getId() == R.id.btn2){
            if(singleFragment2 == null){
                singleFragment2 = new SingleFragment(RecyclerView.HORIZONTAL);
                fragmentTransaction.add(R.id.content, singleFragment2);
            }else{
                if(singleFragment2.isHidden()){
                    fragmentTransaction.show(singleFragment2);
                }
            }
            if(singleFragment1 != null && !singleFragment1.isHidden()){
                fragmentTransaction.hide(singleFragment1);
            }
            fragmentTransaction.commitNowAllowingStateLoss();
        }
    }
}