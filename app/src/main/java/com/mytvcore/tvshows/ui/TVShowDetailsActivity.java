package com.mytvcore.tvshows.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mytvcore.tvshows.R;
import com.mytvcore.tvshows.adapter.EpisodesAdapter;
import com.mytvcore.tvshows.adapter.ImageSliderAdapter;
import com.mytvcore.tvshows.databinding.ActivityTVShowDetailsBinding;
import com.mytvcore.tvshows.databinding.LayoutEpisodesBottomSheetBinding;
import com.mytvcore.tvshows.pojo.model.EpisodeModel;
import com.mytvcore.tvshows.pojo.model.TVShowModel;
import com.mytvcore.tvshows.utilites.TempDataHolder;
import com.mytvcore.tvshows.viewmodel.TVShowDetailsViewModel;

import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class TVShowDetailsActivity extends AppCompatActivity {

    private ActivityTVShowDetailsBinding binding;
    private TVShowDetailsViewModel viewModel;
    private BottomSheetDialog episodesBottomSheet;
    private LayoutEpisodesBottomSheetBinding episodesBottomSheetBinding;
    private TVShowModel tvShow;
    private boolean isTVShowInWatchlist = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_t_v_show_details);
        //Intent
        tvShow = (TVShowModel) getIntent().getSerializableExtra("tvShow");

        imageBackClickListener();
        initViewModel();
        getTVShowDetails();
    }

    private void initViewModel() {
        viewModel = new TVShowDetailsViewModel(getApplication());
    }

    private void checkTVShowInWatchlist() {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(viewModel.getTVShowFromWatchlist(String.valueOf(tvShow.getId()))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tvShow -> {
                    isTVShowInWatchlist = true;
                    binding.imageWatchList.setImageResource(R.drawable.ic_added);
                    compositeDisposable.dispose();
                }));
    }

    private void imageBackClickListener() {
        binding.imageBack.setOnClickListener(view -> onBackPressed());
    }

    private void getTVShowDetails() {
        binding.setIsLoading(true);
        String tvShowId = String.valueOf(tvShow.getId());

        //API response
        viewModel.getTVShowDetails(tvShowId).observe(this, response -> {
            binding.setIsLoading(false);
            if (response != null) {
                //TVShow ImageView
                binding.setTvShowImageURL(response.getTvShowDetails().getImage_path());
                binding.imageTVShow.setVisibility(View.VISIBLE);

                //TextViews
                loadBasicTVShowDetails();

                //TextView Description
                binding.setDescription(String.valueOf(HtmlCompat.fromHtml(
                        response.getTvShowDetails().getDescription(), HtmlCompat.FROM_HTML_MODE_LEGACY)));
                binding.tvDescription.setVisibility(View.VISIBLE);
                binding.tvReadMore.setVisibility(View.VISIBLE);
                tvReadMoreClickListener();

                //Slider Images URL
                if (response.getTvShowDetails().getPictures() != null) {
                    loadImageSlider(response.getTvShowDetails().getPictures());
                }

                //Rating
                binding.setRating(String.format(Locale.getDefault(),
                        "%.2f", Double.parseDouble(response.getTvShowDetails().getRating())));

                //Genres
                if (response.getTvShowDetails().getGenres() != null) {
                    binding.setGenre(response.getTvShowDetails().getGenres()[0]);
                } else {
                    binding.setGenre("N/A");
                }

                //Runtime
                binding.setRuntime(response.getTvShowDetails().getRuntime() + " Min");

                //Dividers
                binding.viewDivider1.setVisibility(View.VISIBLE);
                binding.viewDivider2.setVisibility(View.VISIBLE);
                binding.layoutMisc.setVisibility(View.VISIBLE);

                //Buttons
                buttonWebsiteClickListener(response.getTvShowDetails().getUrl());
                buttonEpisodesClickListener(response.getTvShowDetails().getEpisodes());
                binding.buttonWebsite.setVisibility(View.VISIBLE);
                binding.buttonEpisodes.setVisibility(View.VISIBLE);

                //Watchlist
                checkTVShowInWatchlist();
                imageWatchlistClickListener();
                binding.imageWatchList.setVisibility(View.VISIBLE);

            }
        });
    }

    private void loadImageSlider(String[] sliderImages) {
        binding.sliderViewPager.setOffscreenPageLimit(1);
        binding.sliderViewPager.setAdapter(new ImageSliderAdapter(sliderImages));
        binding.sliderViewPager.setVisibility(View.VISIBLE);
        binding.viewFadingEdge.setVisibility(View.VISIBLE);
        setupSliderIndicators(sliderImages.length);
        binding.sliderViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentSliderIndicator(position);
            }
        });
    }

    private void setupSliderIndicators(int count) {
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(8, 0, 8, 0);

        for (ImageView image : indicators) {
            image = new ImageView(getApplicationContext());
            image.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                    R.drawable.background_slider_indicator_inactive));
            image.setLayoutParams(layoutParams);
            binding.layoutSliderIndicators.addView(image);
        }
        binding.layoutSliderIndicators.setVisibility(View.VISIBLE);
        setCurrentSliderIndicator(0);
    }

    private void setCurrentSliderIndicator(int position) {
        int childCount = binding.layoutSliderIndicators.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) binding.layoutSliderIndicators.getChildAt(i);
            if (i == position) {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                        R.drawable.background_slider_indicator_active));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                        R.drawable.background_slider_indicator_inactive));
            }
        }
    }

    private void loadBasicTVShowDetails() {
        binding.setTvShowName(tvShow.getName());
        binding.tvName.setVisibility(View.VISIBLE);

        binding.setNetworkCountry(tvShow.getNetwork() + " (" +
                tvShow.getCountry() + ")");
        binding.tvNetwork.setVisibility(View.VISIBLE);

        binding.setStatus(tvShow.getStatus());
        binding.tvStatus.setVisibility(View.VISIBLE);

        binding.setStartedDate(tvShow.getStart_date());
        binding.tvStartedDate.setVisibility(View.VISIBLE);
    }

    private void tvReadMoreClickListener() {
        //TextView ReadMore
        binding.tvReadMore.setOnClickListener(view -> {
            if (binding.tvReadMore.getText().toString().equals("Read More")) {
                binding.tvDescription.setMaxLines(Integer.MAX_VALUE);
                binding.tvDescription.setEllipsize(null);
                binding.tvReadMore.setText(R.string.read_less);
            } else {
                binding.tvDescription.setMaxLines(4);
                binding.tvDescription.setEllipsize(TextUtils.TruncateAt.END);
                binding.tvReadMore.setText(R.string.read_more);
            }
        });
        //TextView Description
        binding.tvDescription.setOnClickListener(view -> {
            if (binding.tvReadMore.getText().toString().equals("Read More")) {
                binding.tvDescription.setMaxLines(Integer.MAX_VALUE);
                binding.tvDescription.setEllipsize(null);
                binding.tvReadMore.setText(R.string.read_less);
            } else {
                binding.tvDescription.setMaxLines(4);
                binding.tvDescription.setEllipsize(TextUtils.TruncateAt.END);
                binding.tvReadMore.setText(R.string.read_more);
            }
        });

    }

    private void buttonWebsiteClickListener(String url) {
        binding.buttonWebsite.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });
    }

    private void buttonEpisodesClickListener(List<EpisodeModel> episodesList) {
        binding.buttonEpisodes.setOnClickListener(view -> {
            if (episodesBottomSheet == null) {
                episodesBottomSheet = new BottomSheetDialog(TVShowDetailsActivity.this);
                episodesBottomSheetBinding = DataBindingUtil.inflate(LayoutInflater.from(TVShowDetailsActivity.this),
                        R.layout.layout_episodes_bottom_sheet, findViewById(R.id.episodesContainer), false);
                episodesBottomSheet.setContentView(episodesBottomSheetBinding.getRoot());
                episodesBottomSheetBinding.episodesRecycler.setAdapter(new EpisodesAdapter(episodesList));
                episodesBottomSheetBinding.tvTitle.setText(String.format("Episodes | %s", tvShow.getName()));
                episodesBottomSheetBinding.imageClose.setOnClickListener(v -> episodesBottomSheet.dismiss());
            }

            // ---- Optional section start ---- //
            FrameLayout frameLayout = episodesBottomSheet.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (frameLayout != null) {
                BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(frameLayout);
                bottomSheetBehavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
            // ---- Optional section end ---- //
            episodesBottomSheet.show();
        });
    }

    private void imageWatchlistClickListener() {
        binding.imageWatchList.setOnClickListener(view -> {
            CompositeDisposable compositeDisposable = new CompositeDisposable();
            if (isTVShowInWatchlist) {
                compositeDisposable.add(viewModel.removeTVShowFromWatchlist(tvShow)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            isTVShowInWatchlist = false;
                            TempDataHolder.IS_WATCHLIST_UPDATED = true;
                            binding.imageWatchList.setImageResource(R.drawable.ic_watchlist);
                            Toast.makeText(getApplicationContext(), "Removed from watchlist", Toast.LENGTH_SHORT).show();
                        })
                );

            } else {
                compositeDisposable.add(viewModel.addToWatchList(tvShow)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            TempDataHolder.IS_WATCHLIST_UPDATED = true;
                            binding.imageWatchList.setImageResource(R.drawable.ic_added);
                            Toast.makeText(getApplicationContext(), "Added to watchlist", Toast.LENGTH_SHORT).show();
                        })
                );
            }
        });
    }
}