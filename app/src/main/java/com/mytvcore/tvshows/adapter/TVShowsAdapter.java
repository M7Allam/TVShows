package com.mytvcore.tvshows.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.mytvcore.tvshows.R;
import com.mytvcore.tvshows.databinding.ItemContainerTvShowBinding;
import com.mytvcore.tvshows.pojo.model.TVShowModel;
import com.mytvcore.tvshows.utilites.listener.TVShowListener;

import java.util.List;

public class TVShowsAdapter extends RecyclerView.Adapter<TVShowsAdapter.TVShowViewHolder> {

    private List<TVShowModel> tvShowsList;
    private LayoutInflater layoutInflater;
    private TVShowListener tvShowListener;

    public TVShowsAdapter(List<TVShowModel> tvShowsList, TVShowListener tvShowListener) {
        this.tvShowsList = tvShowsList;
        this.tvShowListener = tvShowListener;
    }

    @NonNull
    @Override
    public TVShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemContainerTvShowBinding binding = DataBindingUtil.inflate(layoutInflater,
                R.layout.item_container_tv_show, parent, false);

        return new TVShowViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TVShowViewHolder holder, int position) {
        holder.bindTVShow(tvShowsList.get(position));
    }

    @Override
    public int getItemCount() {
        return tvShowsList.size();
    }


    /*
    -> Class ViewHolder
    */
    class TVShowViewHolder extends RecyclerView.ViewHolder {

        private ItemContainerTvShowBinding itemBinding;

        public TVShowViewHolder(ItemContainerTvShowBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        public void bindTVShow(TVShowModel tvShowModel) {
            itemBinding.setTvShow(tvShowModel);
            itemBinding.executePendingBindings();
            itemBinding.getRoot().setOnClickListener(view -> tvShowListener.onTVShowClicked(tvShowModel));
        }
    }
}
