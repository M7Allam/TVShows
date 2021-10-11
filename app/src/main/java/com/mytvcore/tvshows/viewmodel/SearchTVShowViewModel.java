package com.mytvcore.tvshows.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.mytvcore.tvshows.pojo.response.TVShowResponse;
import com.mytvcore.tvshows.repo.SearchTVShowRepository;

public class SearchTVShowViewModel extends ViewModel {

    private SearchTVShowRepository repository;

    public SearchTVShowViewModel() {
        repository = new SearchTVShowRepository();
    }

    public LiveData<TVShowResponse> searchTVShow(String query, int page) {
        return repository.searchTVShow(query, page);
    }
}
