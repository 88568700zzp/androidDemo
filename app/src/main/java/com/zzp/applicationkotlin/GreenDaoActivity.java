package com.zzp.applicationkotlin;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.zzp.applicationkotlin.manager.GreenDaoManager;
import com.zzp.applicationkotlin.model.Book;
import com.zzp.applicationkotlin.model.BookDao;

import java.util.List;

public class GreenDaoActivity extends AppCompatActivity implements View.OnClickListener {

    private BookDao mBookDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_green_dao);

        mBookDao = GreenDaoManager.getInstance().getDaoSession().getBookDao();

        findViewById(R.id.green_add).setOnClickListener(this);
        findViewById(R.id.green_delete).setOnClickListener(this);
        findViewById(R.id.green_list).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.green_add) {
            mBookDao.insert(new Book());
        }else if (v.getId() == R.id.green_delete) {
            mBookDao.deleteByKey(new Long(1));
        }else if (v.getId() == R.id.green_list) {
            List<Book> list = mBookDao.loadAll();
            for(int i = 0;i < list.size();i++){
                Log.d("zzp12","info:" + list.get(i).getId());
            }
        }
    }
}
