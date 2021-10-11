package com.mytvcore.tvshows.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.mytvcore.tvshows.R;
import com.mytvcore.tvshows.databinding.ItemContainerTvShowBinding;
import com.mytvcore.tvshows.pojo.model.TVShowModel;
import com.mytvcore.tvshows.utilites.listener.WatchlistListener;

import java.util.List;

public class WatchlistAdapter extends RecyclerView.Adapter<WatchlistAdapter.WatchlistViewHolder> {

    private List<TVShowModel> tvShowsList;
    private LayoutInflater layoutInflater;
    private WatchlistListener watchlistListener;

    public WatchlistAdapter(List<TVShowModel> tvShowsList, WatchlistListener watchlistListener) {
        this.tvShowsList = tvShowsList;
        this.watchlistListener = watchlistListener;
    }

    @NonNull
    @Override
    public WatchlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemContainerTvShowBinding binding = DataBindingUtil.inflate(layoutInflater,
                R.layout.item_container_tv_show, parent, false);

        return new WatchlistViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WatchlistViewHolder holder, int position) {
        holder.bindTVShow(tvShowsList.get(position));
    }

    @Override
    public int getItemCount() {
        return tvShowsList.size();
    }


    /*
    -> Class ViewHolder
    */
    class WatchlistViewHolder extends RecyclerView.ViewHolder {

        private ItemContainerTvShowBinding itemBinding;

        public WatchlistViewHolder(ItemContainerTvShowBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        public void bindTVShow(TVShowModel tvShowModel) {
            itemBinding.setTvShow(tvShowModel);
            itemBinding.executePendingBindings();
            itemBinding.getRoot().setOnClickListener(view -> watchlistListener.onTVShowClicked(tvShowModel));
            itemBinding.imageDelete.setOnClickListener(view -> watchlistListener.removeTVShowFromWatchlist(tvShowModel, getAdapterPosition()));
            itemBinding.imageDelete.setVisibility(View.VISIBLE);
        }
    }
}
