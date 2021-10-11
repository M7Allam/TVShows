package com.mytvcore.tvshows.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.mytvcore.tvshows.R;
import com.mytvcore.tvshows.adapter.TVShowsAdapter;
import com.mytvcore.tvshows.databinding.ActivityMainBinding;
import com.mytvcore.tvshows.pojo.model.TVShowModel;
import com.mytvcore.tvshows.utilites.listener.TVShowListener;
import com.mytvcore.tvshows.viewmodel.MostPopularViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TVShowListener {

    private ActivityMainBinding binding;
    private MostPopularViewModel viewModel;
    private TVShowsAdapter adapter;
    private List<TVShowModel> tvShowsList = new ArrayList<>();
    private int currentPage = 1;
    private int totalAvailablePages = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initViewModel();
        initRecycler();
        recyclerScrollListener();
        imageWatchlistClickListener();
        imageSearchClickListener();
        getMostPopularTVShows();


    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(MostPopularViewModel.class);
    }

    private void initRecycler() {
        //binding.recyclerTVShows.setHasFixedSize(true);
        adapter = new TVShowsAdapter(tvShowsList, this);
        binding.recyclerTVShows.setAdapter(adapter);

    }

    private void recyclerScrollListener() {
        binding.recyclerTVShows.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!binding.recyclerTVShows.canScrollVertically(1)) {
                    if (currentPage < totalAvailablePages) {
                        currentPage++;
                        getMostPopularTVShows();
                    } else if (currentPage == totalAvailablePages) {
                        getMostPopularTVShows();
                    }
                }
            }
        });
    }

    private void getMostPopularTVShows() {
        toggleLoading();
        //GET data from API
        viewModel.getMostPopularTVShows(currentPage).observe(this, tvShowResponse -> {
            toggleLoading();
            if (tvShowResponse != null) {
                totalAvailablePages = tvShowResponse.getTotalPages();
                if (tvShowResponse.getTvShows() != null) {
                    int oldCount = tvShowsList.size();
                    tvShowsList.addAll(tvShowResponse.getTvShows());
                    adapter.notifyItemRangeInserted(oldCount, tvShowsList.size());
                }
            }
        });
    }

    private void toggleLoading() {
        if (currentPage == 1) {
            if (binding.getIsLoading() != null && binding.getIsLoading()) {
                binding.setIsLoading(false);
            } else
                binding.setIsLoading(true);
        } else {
            if (binding.getIsLoadingMore() != null && binding.getIsLoadingMore()) {
                binding.setIsLoadingMore(false);
            } else
                binding.setIsLoadingMore(true);
        }
    }

    private void imageWatchlistClickListener() {
        binding.imageWatchList.setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, WatchlistActivity.class)));
    }

    private void imageSearchClickListener() {
        binding.imageSearch.setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, SearchActivity.class)));
    }

    @Override
    public void onTVShowClicked(TVShowModel tvShow) {
        Intent intent = new Intent(getApplicationContext(), TVShowDetailsActivity.class);
        intent.putExtra("tvShow", tvShow);
        startActivity(intent);
    }
}