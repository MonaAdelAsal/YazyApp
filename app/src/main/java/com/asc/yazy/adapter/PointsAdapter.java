package com.asc.yazy.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.asc.yazy.R;
import com.asc.yazy.api.model.ModelPoint;
import com.asc.yazy.databinding.RvPointBinding;
import com.asc.yazy.interfaces.OnPointListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class PointsAdapter extends RecyclerView.Adapter<PointsAdapter.ViewHolder> {

    private final Context context;
    private final boolean isLoading;
    private final List<ModelPoint> offersList;
    private final OnPointListener listener;

    public PointsAdapter(Context context, List<ModelPoint> offersList, boolean isLoading, OnPointListener listener) {
        this.context = context;
        this.offersList = offersList;
        this.isLoading = isLoading;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PointsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RvPointBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.rv_point, parent, false);
        binding.setListener(listener);
        return new PointsAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PointsAdapter.ViewHolder holder, int position) {
        if (position != RecyclerView.NO_POSITION)
            holder.setViewModel(offersList.get(position), position);
    }

    @Override
    public int getItemCount() {
        if (offersList == null) return 0;
        return offersList.size();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull PointsAdapter.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.bind();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull PointsAdapter.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.unbind();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private RvPointBinding binding;

        ViewHolder(RvPointBinding binding) {
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
        void setViewModel(ModelPoint offerModel, int position) {
            if (binding != null) {

                if (isLoading) {
                    binding.loaderImage.startShimmerAnimation();
                } else {
                    binding.setPoint(offerModel);
                    binding.loaderImage.stopShimmerAnimation();
                    if (offerModel.getDiscountImage() != null && !offerModel.getDiscountImage().trim().isEmpty())
                        Glide.with(context).asBitmap().load(offerModel.getDiscountImage()).diskCacheStrategy(DiskCacheStrategy.DATA).placeholder(R.color.gray).into(binding.img);
                    else
                        binding.img.setImageResource(R.color.gray);
                    binding.tvDetails.setText((offerModel.getDiscountPoints() + " " + context.getResources().getString(R.string.pts)));
                }

                switch (position) {
                    case 0:
                        binding.tvDetails.setBackground(context.getDrawable(R.drawable.bottom_one));
                        break;
                    case 1:
                        binding.tvDetails.setBackground(context.getDrawable(R.drawable.bottom_two));
                        break;
                    case 2:
                        binding.tvDetails.setBackground(context.getDrawable(R.drawable.bottom_three));
                        break;
                    case 3:
                        binding.tvDetails.setBackground(context.getDrawable(R.drawable.bottom_four));
                        break;
                    case 4:
                        binding.tvDetails.setBackground(context.getDrawable(R.drawable.bottom_five));
                        break;
                    case 5:
                        binding.tvDetails.setBackground(context.getDrawable(R.drawable.bottom_six));
                        break;
                }
            }
        }

    }

}




