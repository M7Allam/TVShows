package com.mytvcore.tvshows.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.mytvcore.tvshows.R;
import com.mytvcore.tvshows.adapter.TVShowsAdapter;
import com.mytvcore.tvshows.databinding.ActivitySearchBinding;
import com.mytvcore.tvshows.pojo.model.TVShowModel;
import com.mytvcore.tvshows.utilites.listener.TVShowListener;
import com.mytvcore.tvshows.viewmodel.SearchTVShowViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SearchActivity extends AppCompatActivity implements TVShowListener {

    private ActivitySearchBinding binding;
    private SearchTVShowViewModel viewModel;
    private List<TVShowModel> tvShowList = new ArrayList<>();
    private TVShowsAdapter adapter;
    private Timer timer;
    private int currentPage = 1, totalPages = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);

        initViewModel();
        initRecycler();
        imageBackClickListener();
        inputTextChangedListener();
        recyclerScrollListener();
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(SearchTVShowViewModel.class);
    }

    private void initRecycler() {
        binding.searchRecycler.setHasFixedSize(true);
        adapter = new TVShowsAdapter(tvShowList, this);
        binding.searchRecycler.setAdapter(adapter);
    }

    private void inputTextChangedListener() {
        binding.inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (timer != null) {
                    timer.cancel();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().trim().isEmpty()) {
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            new Handler(Looper.getMainLooper()).post(() -> {
                                currentPage = 1;
                                totalPages = 1;
                                searchTVShow(editable.toString());

                            });
                        }
                    }, 750);

                } else {
                    tvShowList.clear();
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void recyclerScrollListener() {
        binding.searchRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!binding.searchRecycler.canScrollVertically(1)) {
                    if (!binding.inputSearch.getText().toString().isEmpty()) {
                        if (currentPage < totalPages) {
                            currentPage++;
                            searchTVShow(binding.inputSearch.getText().toString());
                        }
                    }
                }
            }
        });
        binding.inputSearch.requestFocus();
    }

    private void imageBackClickListener() {
        binding.imageBack.setOnClickListener(view -> onBackPressed());
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

    private void searchTVShow(String query) {
        toggleLoading();
        viewModel.searchTVShow(query, currentPage).observe(this, response -> {
            toggleLoading();
            if (response != null) {
                totalPages = response.getTotalPages();
                if (response.getTvShows() != null) {
                    int oldCount = tvShowList.size();
                    tvShowList.addAll(response.getTvShows());
                    adapter.notifyItemRangeInserted(oldCount, tvShowList.size());
                }
            }
        });
    }


    @Override
    public void onTVShowClicked(TVShowModel tvShow) {
        Intent intent = new Intent(getApplicationContext(), TVShowDetailsActivity.class);
        intent.putExtra("tvShow", tvShow);
        startActivity(intent);
    }
}