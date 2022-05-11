package com.zzp.applicationkotlin.manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.zzp.applicationkotlin.model.DaoMaster;
import com.zzp.applicationkotlin.model.DaoSession;

public class GreenDaoManager {

    private static GreenDaoManager mInstance;

    private DaoSession mSession;

    public static GreenDaoManager getInstance(){
        if(mInstance == null){
            synchronized (GreenDaoManager.class){
                if(mInstance == null){
                    mInstance = new GreenDaoManager();
                }
            }

        }
        return mInstance;
    }

    public void init(Context context){
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context,"test.db");
        SQLiteDatabase db = devOpenHelper.getWritableDatabase();
        // 2、创建数据库连接
        DaoMaster daoMaster = new DaoMaster(db);
        // 3、创建数据库会话
        mSession = daoMaster.newSession();
    }

    // 供外接使用
    public DaoSession getDaoSession() {
        return mSession;
    }
}
