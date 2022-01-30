package com.asc.yazy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.asc.yazy.R;
import com.asc.yazy.cash.room.model.FullSearchHistoryModel;
import com.asc.yazy.databinding.RvFullSearchHistoryBinding;
import com.asc.yazy.fragment.SearchHistoryFragment;

import java.util.List;

public class FullSearchHistoryAdapter extends RecyclerView.Adapter<FullSearchHistoryAdapter.ViewHolder> {


    private final List<FullSearchHistoryModel> fullSearchHistoryModels;
    private final SearchHistoryFragment listener;

    public FullSearchHistoryAdapter(List<FullSearchHistoryModel> fullSearchHistoryModels, SearchHistoryFragment searchHistoryFragment) {
        this.fullSearchHistoryModels = fullSearchHistoryModels;
        this.listener = searchHistoryFragment;
    }

    @NonNull
    @Override
    public FullSearchHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RvFullSearchHistoryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.rv_full_search_history, parent, false);
        binding.setListener(listener);
        return new FullSearchHistoryAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FullSearchHistoryAdapter.ViewHolder holder, int position) {
        if (position != RecyclerView.NO_POSITION)
            holder.setViewModel(fullSearchHistoryModels.get(position));
    }

    @Override
    public int getItemCount() {
        if (fullSearchHistoryModels == null) return 0;
        return fullSearchHistoryModels.size();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull FullSearchHistoryAdapter.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.bind();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull FullSearchHistoryAdapter.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.unbind();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private RvFullSearchHistoryBinding binding;

        ViewHolder(RvFullSearchHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind() {
            if (binding == null) {
                binding = DataBindingUtil.bind(itemView);
            }
        }

        void unbind() {
            if (binding != null) {
                binding.unbind();
            }
        }

        void setViewModel(FullSearchHistoryModel offerModel) {
            if (binding != null) {
                binding.setData(offerModel);

                if (getAdapterPosition() != RecyclerView.NO_POSITION && getAdapterPosition() == fullSearchHistoryModels.size() - 1)
                    binding.line.setVisibility(View.GONE);

            }
        }

    }

}




