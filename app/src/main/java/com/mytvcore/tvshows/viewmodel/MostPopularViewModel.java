package com.mytvcore.tvshows.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.mytvcore.tvshows.pojo.response.TVShowResponse;
import com.mytvcore.tvshows.repo.MostPopularRepository;

public class MostPopularViewModel extends ViewModel {

    private MostPopularRepository repository;

    public MostPopularViewModel() {
        repository = new MostPopularRepository();
    }

    public LiveData<TVShowResponse> getMostPopularTVShows(int page) {
        return repository.getMostPopularTVShows(page);
    }
}
