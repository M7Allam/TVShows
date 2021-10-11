package com.mytvcore.tvshows.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.mytvcore.tvshows.database.TVShowsDatabase;
import com.mytvcore.tvshows.pojo.model.TVShowModel;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class WatchlistViewModel extends AndroidViewModel {

    private TVShowsDatabase database;

    public WatchlistViewModel(@NonNull Application application) {
        super(application);
        database = TVShowsDatabase.getInstance(application);
    }

    public Flowable<List<TVShowModel>> getWatchlist() {
        return database.tvShowDao().getWatchList();
    }

    public Completable removeTVShowFromWatchlist(TVShowModel tvShow) {
        return database.tvShowDao().removeFromWatchList(tvShow);
    }
}
