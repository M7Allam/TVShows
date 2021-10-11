package com.mytvcore.tvshows.pojo.response;

import com.google.gson.annotations.SerializedName;
import com.mytvcore.tvshows.pojo.model.TVShowDetailsModel;

public class TVShowDetailsResponse {

    @SerializedName("tvShow")
    private TVShowDetailsModel tvShowDetails;

    public TVShowDetailsModel getTvShowDetails() {
        return tvShowDetails;
    }
}
