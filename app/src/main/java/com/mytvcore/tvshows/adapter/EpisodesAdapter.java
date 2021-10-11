package com.mytvcore.tvshows.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.mytvcore.tvshows.R;
import com.mytvcore.tvshows.databinding.ItemContainerEpisodeBinding;
import com.mytvcore.tvshows.pojo.model.EpisodeModel;

import java.util.List;

public class EpisodesAdapter extends RecyclerView.Adapter<EpisodesAdapter.EpisodeViewHolder> {

    private List<EpisodeModel> episodeList;
    private LayoutInflater layoutInflater;

    public EpisodesAdapter(List<EpisodeModel> episodeList) {
        this.episodeList = episodeList;
    }


    @NonNull
    @Override
    public EpisodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemContainerEpisodeBinding binding = DataBindingUtil.inflate(layoutInflater,
                R.layout.item_container_episode, parent, false);

        return new EpisodeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodeViewHolder holder, int position) {
        holder.bindEpisode(episodeList.get(position));
    }

    @Override
    public int getItemCount() {
        return episodeList.size();
    }

    static class EpisodeViewHolder extends RecyclerView.ViewHolder {

        private ItemContainerEpisodeBinding binding;

        public EpisodeViewHolder(ItemContainerEpisodeBinding itemContainerEpisodeBinding) {
            super(itemContainerEpisodeBinding.getRoot());
            binding = itemContainerEpisodeBinding;
        }

        public void bindEpisode(EpisodeModel episode) {
            String season = episode.getSeason();
            if (season.length() == 1) {
                season = "0".concat(season);
            }

            String episodeNumber = episode.getEpisode();
            if (episodeNumber.length() == 1) {
                episodeNumber = "0".concat(episodeNumber);
            }
            episodeNumber = "E".concat(episodeNumber);

            String title = "S".concat(season).concat(episodeNumber);

            binding.setTitle(title);
            binding.setName(episode.getName());
            binding.setAirDate(episode.getAir_date());

        }


    }
}
