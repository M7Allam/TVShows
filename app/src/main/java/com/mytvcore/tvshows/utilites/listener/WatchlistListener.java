package com.mytvcore.tvshows.utilites.listener;

import com.mytvcore.tvshows.pojo.model.TVShowModel;

public interface WatchlistListener {

    void onTVShowClicked(TVShowModel tvShow);

    void removeTVShowFromWatchlist(TVShowModel tvShow, int position);
}
