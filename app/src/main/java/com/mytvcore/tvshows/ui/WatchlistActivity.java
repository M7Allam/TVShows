package com.mytvcore.tvshows.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.mytvcore.tvshows.R;
import com.mytvcore.tvshows.adapter.WatchlistAdapter;
import com.mytvcore.tvshows.databinding.ActivityWatchlistBinding;
import com.mytvcore.tvshows.pojo.model.TVShowModel;
import com.mytvcore.tvshows.utilites.TempDataHolder;
import com.mytvcore.tvshows.utilites.listener.WatchlistListener;
import com.mytvcore.tvshows.viewmodel.WatchlistViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class WatchlistActivity extends AppCompatActivity implements WatchlistListener {

    private ActivityWatchlistBinding binding;
    private WatchlistViewModel viewModel;
    private WatchlistAdapter adapter;
    private List<TVShowModel> watchlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_watchlist);

        initViewModel();
        imageBackClickListener();
        loadWatchlist();
    }

    private void initViewModel() {
        viewModel = new WatchlistViewModel(getApplication());
    }

    private void initRecycler() {
        adapter = new WatchlistAdapter(watchlist, this);
        binding.watchlistRecycler.setAdapter(adapter);
        binding.watchlistRecycler.setVisibility(View.VISIBLE);
    }

    private void imageBackClickListener() {
        binding.imageBack.setOnClickListener(view -> onBackPressed());
    }

    private void loadWatchlist() {
        binding.setIsLoading(true);
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(viewModel.getWatchlist()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tvShows -> {
                    binding.setIsLoading(false);
                    if (watchlist.size() > 0) {
                        watchlist.clear();
                    }
                    watchlist.addAll(tvShows);
                    initRecycler();
                    compositeDisposable.dispose();


                }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (TempDataHolder.IS_WATCHLIST_UPDATED) {
            loadWatchlist();
            TempDataHolder.IS_WATCHLIST_UPDATED = false;
        }

    }

    @Override
    public void onTVShowClicked(TVShowModel tvShow) {
        Intent intent = new Intent(getApplicationContext(), TVShowDetailsActivity.class);
        intent.putExtra("tvShow", tvShow);
        startActivity(intent);
    }

    @Override
    public void removeTVShowFromWatchlist(TVShowModel tvShow, int position) {
        CompositeDisposable compositeDisposableDelete = new CompositeDisposable();
        compositeDisposableDelete.add(viewModel.removeTVShowFromWatchlist(tvShow)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    watchlist.remove(position);
                    adapter.notifyItemRemoved(position);
                    adapter.notifyItemRangeChanged(position, adapter.getItemCount());
                    compositeDisposableDelete.dispose();
                }));
    }
}