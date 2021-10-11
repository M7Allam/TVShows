package com.mytvcore.tvshows.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.mytvcore.tvshows.pojo.model.TVShowModel;

@Database(entities = TVShowModel.class, version = 1, exportSchema = false)
public abstract class TVShowsDatabase extends RoomDatabase {

    private static TVShowsDatabase database;

    public static synchronized TVShowsDatabase getInstance(Context context){
        if(database == null){
            database = Room.databaseBuilder(context, TVShowsDatabase.class, "tv_shows_db").build();
        }
        return database;
    }

    public abstract TVShowDao tvShowDao();
}
