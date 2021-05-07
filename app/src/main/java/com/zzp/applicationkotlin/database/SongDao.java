package com.zzp.applicationkotlin.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * Created by samzhang on 2021/4/23.
 */
@Dao
public interface SongDao {
    @Query("SELECT * FROM song")
    LiveData<List<Song>> loadAll();
    @Query("SELECT * FROM song WHERE id IN (:songIds)")
    LiveData<List<Song>> loadAllBySongId(int... songIds);

    @Query("SELECT * FROM song WHERE name LIKE :name AND release_year = :year LIMIT 1")
    Song loadOneByNameAndReleaseYear(String name, int year);
    @Insert
    void insertAll(Song... songs);
    @Delete
    void delete(Song song);
}
