package com.asc.yazy.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.asc.yazy.R;
import com.asc.yazy.api.model.ModelFAQs;
import com.asc.yazy.databinding.CategoryCellBinding;
import com.asc.yazy.interfaces.OnCategoryClickListener;

import java.util.List;

public class FAQsCategoriesAdapter extends RecyclerView.Adapter<FAQsCategoriesAdapter.ViewHolder> {

    private final OnCategoryClickListener listener;
    private final Context context;
    private final List<ModelFAQs> list;

    public FAQsCategoriesAdapter(Context context, List<ModelFAQs> list, OnCategoryClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.list = list;
        list.get(0).setClicked(1);
    }

    @NonNull
    @Override
    public FAQsCategoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CategoryCellBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.category_cell, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FAQsCategoriesAdapter.ViewHolder holder, int position) {
        if (position != RecyclerView.NO_POSITION)
            holder.setViewModel(list.get(position));
    }

    @Override
    public int getItemCount() {
        if (list == null) return 0;
        return list.size();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull FAQsCategoriesAdapter.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.bind();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull FAQsCategoriesAdapter.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.unbind();
    }

    private void clearSelection() {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setClicked(0);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CategoryCellBinding binding;

        ViewHolder(CategoryCellBinding binding) {
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

        @SuppressLint("UseCompatLoadingForDrawables")
        void setViewModel(ModelFAQs FAQ) {
            if (binding != null) {
                binding.name.setText(FAQ.getName());
                binding.Category.setOnClickListener(v -> {
                    clearSelection();
                    if (FAQ.getClicked() == 0) {
                        listener.OnCategoryClickListener(FAQ);
                    }
                    binding.Category.setBackground(context.getResources().getDrawable(R.drawable.cell_orange_b17));
                    binding.name.setTextColor(Color.parseColor("#FFFFFF"));
                    list.get(getAdapterPosition()).setClicked(1);
                    notifyDataSetChanged();
                });

                if (FAQ.getClicked() == 0) {
                    binding.Category.setBackground(context.getResources().getDrawable(R.drawable.cell_gray_b17));
                    binding.name.setTextColor(Color.parseColor("#020202"));
                } else {
                    binding.name.setTextColor(Color.parseColor("#FFFFFF"));
                    binding.Category.setBackground(context.getResources().getDrawable(R.drawable.cell_orange_b17));
                }
            }
        }
    }
}