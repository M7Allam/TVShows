package com.mytvcore.tvshows.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mytvcore.tvshows.database.TVShowsDatabase;
import com.mytvcore.tvshows.pojo.model.TVShowModel;
import com.mytvcore.tvshows.pojo.response.TVShowDetailsResponse;
import com.mytvcore.tvshows.repo.TVShowDetailsRepository;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class TVShowDetailsViewModel extends AndroidViewModel {

    private TVShowDetailsRepository repository;
    private TVShowsDatabase database;

    public TVShowDetailsViewModel(@NonNull Application application) {
        super(application);
        repository = new TVShowDetailsRepository();
        database = TVShowsDatabase.getInstance(application);
    }

    public LiveData<TVShowDetailsResponse> getTVShowDetails(String tvShowId) {
        return repository.getTVShowDetails(tvShowId);
    }

    public Completable addToWatchList(TVShowModel tvShow) {
        return database.tvShowDao().addToWatchList(tvShow);
    }

    public Flowable<TVShowModel> getTVShowFromWatchlist(String tvShowId) {
        return database.tvShowDao().getTVShowFromWatchlist(tvShowId);
    }

    public Completable removeTVShowFromWatchlist(TVShowModel tvShow) {
        return database.tvShowDao().removeFromWatchList(tvShow);
    }
}
