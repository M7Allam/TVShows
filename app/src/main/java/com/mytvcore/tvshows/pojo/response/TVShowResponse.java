package com.mytvcore.tvshows.pojo.response;

import com.google.gson.annotations.SerializedName;
import com.mytvcore.tvshows.pojo.model.TVShowModel;

import java.util.List;

public class TVShowResponse {

    @SerializedName("page")
    private int page;

    @SerializedName("pages")
    private int totalPages;

    @SerializedName("tv_shows")
    private List<TVShowModel> tvShows;

    public int getPage() {
        return page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public List<TVShowModel> getTvShows() {
        return tvShows;
    }
}
