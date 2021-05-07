package com.zzp.applicationkotlin.database;

import android.content.Context;

import androidx.room.Room;

/**
 * Created by samzhang on 2021/4/23.
 */
public class DataBaseManager {
    private static MusicDatabase mMusicDatabase;

    public static MusicDatabase getDataBase(Context context){
        if(mMusicDatabase == null){
            synchronized (DataBaseManager.class){
                if(mMusicDatabase == null){
                    mMusicDatabase = Room
                            .databaseBuilder(context, MusicDatabase.class, "music_db").allowMainThreadQueries()//允许在主线程中查询
                            .build();
                }
            }
        }
        return mMusicDatabase;
    }
}
