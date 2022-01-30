package com.asc.yazy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.asc.yazy.R;
import com.asc.yazy.cash.room.model.SearchHistoryModel;
import com.asc.yazy.databinding.RvSearhcHistroryBinding;
import com.asc.yazy.interfaces.OnRecentSearchListener;

import java.util.ArrayList;
import java.util.List;

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder> {


    private final List<SearchHistoryModel> filterList;
    private final OnRecentSearchListener listener;
    private List<SearchHistoryModel> offersList;


    public SearchHistoryAdapter(List<SearchHistoryModel> offersList, OnRecentSearchListener listener) {
        // different allocations  to avoid pointing to the same memory cell
        this.offersList = new ArrayList<>();
        this.offersList.addAll(offersList);
        this.filterList = new ArrayList<>();
        this.filterList.addAll(offersList);
        this.listener = listener;

    }

    @NonNull
    @Override
    public SearchHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RvSearhcHistroryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.rv_searhc_histrory, parent, false);
        binding.setListener(listener);
        return new SearchHistoryAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHistoryAdapter.ViewHolder holder, int position) {
        if (position != RecyclerView.NO_POSITION)
            holder.setViewModel(offersList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return Math.min(offersList == null ? 0 : offersList.size(), 5);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull SearchHistoryAdapter.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.bind();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull SearchHistoryAdapter.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.unbind();
    }

    public void filter(String text) {

        if (text == null || text.trim().isEmpty()) {
            this.offersList = filterList;
        } else {
            this.offersList = new ArrayList<>();
            for (SearchHistoryModel item : filterList) {
                if (item.getDestination().toLowerCase().contains(text.toLowerCase()))
                    this.offersList.add(item);
            }
        }
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private RvSearhcHistroryBinding binding;

        ViewHolder(RvSearhcHistroryBinding binding) {
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

        void setViewModel(SearchHistoryModel model, int position) {
            if (binding != null && position != RecyclerView.NO_POSITION) {
                binding.setModel(model);
                if (position == offersList.size() - 1 || position == 4)
                    binding.line.setVisibility(View.GONE);
                else
                    binding.line.setVisibility(View.VISIBLE);
            }
        }

    }

}




