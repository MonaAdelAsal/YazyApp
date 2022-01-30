package com.asc.yazy.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.asc.yazy.R;
import com.asc.yazy.animattion.CustomAnimation;
import com.asc.yazy.databinding.RvAdultsCountBinding;
import com.asc.yazy.interfaces.OnCountListener;

public class AdultsCountAdapter extends RecyclerView.Adapter<AdultsCountAdapter.ViewHolder> {

    private final Context context;
    private final OnCountListener listener;
    private int index = -1; // -1 is for no item selected -> RecyclerView.NO_POSITION
    private int offset;
    private boolean animate = true;


    public AdultsCountAdapter(Context context, OnCountListener listener, int offset) {
        this.context = context;
        this.listener = listener;
        this.offset = offset;
    }


    public AdultsCountAdapter(Context context, OnCountListener listener, int index, int offset) {
        this.context = context;
        this.listener = listener;
        this.index = index;
        this.offset = offset;
    }


    @NonNull
    @Override
    public AdultsCountAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RvAdultsCountBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.rv_adults_count, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdultsCountAdapter.ViewHolder holder, int position) {
        if (position != RecyclerView.NO_POSITION)
            holder.setViewModel((position + 1));
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    @Override
    public void onViewAttachedToWindow(@NonNull AdultsCountAdapter.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.bind();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull AdultsCountAdapter.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.unbind();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private RvAdultsCountBinding binding;

        ViewHolder(RvAdultsCountBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.tvCount.setOnClickListener(v -> {
                if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                    if (index == (getAdapterPosition() + 1)) {
                        index = -1;
                        if (listener == null)
                            return;
                        listener.onCountClicked(index, false);
                    } else {
                        index = getAdapterPosition() + 1;
                        if (listener == null)
                            return;
                        listener.onCountClicked(index, true);
                    }
                    animate = false;
                    notifyDataSetChanged();
                }

            });
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
        void setViewModel(int position) {
            if (binding != null) {
                binding.tvCount.setText(String.valueOf(position));
                binding.tvCount.setBackground(context.getResources().getDrawable(R.drawable.border_gray));
                binding.tvCount.setTextColor(context.getResources().getColor(R.color.black));
                if (position == index) {
                    binding.tvCount.setBackground(context.getResources().getDrawable(R.drawable.solid_blue));
                    binding.tvCount.setTextColor(context.getResources().getColor(R.color.white));
                }
                if (animate) {
                    CustomAnimation.scaleOutWithPulse(binding.tvCount, offset, 300);
                    offset += 200;
                }
            }
        }

    }

}




