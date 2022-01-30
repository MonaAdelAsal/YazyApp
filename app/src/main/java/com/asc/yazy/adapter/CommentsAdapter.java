package com.asc.yazy.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.asc.yazy.R;
import com.asc.yazy.api.model.CommentModel;
import com.asc.yazy.databinding.RvCommentBinding;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    private final boolean isLoading;
    private final List<CommentModel> offersList;

    public CommentsAdapter(List<CommentModel> offersList, boolean isLoading) {
        this.offersList = offersList;
        this.isLoading = isLoading;
    }

    @NonNull
    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RvCommentBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.rv_comment, parent, false);
        return new CommentsAdapter.ViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull CommentsAdapter.ViewHolder holder, int position) {
        if (position != RecyclerView.NO_POSITION)
            holder.setViewModel(offersList.get(position));

    }

    @Override
    public int getItemCount() {
        if (offersList == null) return 0;
        return offersList.size();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull CommentsAdapter.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.bind();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull CommentsAdapter.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.unbind();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private RvCommentBinding binding;

        ViewHolder(RvCommentBinding binding) {
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

        void setViewModel(CommentModel offerModel) {
            if (binding != null) {

                if (isLoading) {
                    binding.loaderName.startShimmerAnimation();
                    binding.loaderDate.startShimmerAnimation();
                    binding.loaderComment.startShimmerAnimation();
                    binding.loaderRate.startShimmerAnimation();

                } else {
                    binding.loaderName.stopShimmerAnimation();
                    binding.loaderDate.stopShimmerAnimation();
                    binding.loaderComment.stopShimmerAnimation();
                    binding.loaderRate.stopShimmerAnimation();
                    binding.tvName.setBackground(null);
                    binding.tvDate.setBackground(null);
                    binding.tvComment.setBackground(null);
                    binding.tvRate.setBackground(null);
                    binding.setComment(offerModel);
                    binding.rate.setRating(Float.parseFloat(offerModel.getRate()));


                }
            }
        }

    }

}




