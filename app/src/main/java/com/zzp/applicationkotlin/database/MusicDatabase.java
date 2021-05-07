package com.zzp.applicationkotlin.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * Created by samzhang on 2021/4/23.
 */
@Database(entities = {Song.class}, version = 1)
public abstract class MusicDatabase extends RoomDatabase {
    public abstract SongDao getSongDao();
}
