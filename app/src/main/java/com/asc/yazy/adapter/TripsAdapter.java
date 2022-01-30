package com.asc.yazy.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.asc.yazy.R;
import com.asc.yazy.api.model.ModelBooking;
import com.asc.yazy.databinding.RvComingTripLoadingBinding;

import java.util.List;

public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.ViewHolder> {

    private final boolean isLoading;
    private final List<ModelBooking> offersList;

    public TripsAdapter(List<ModelBooking> offersList, boolean isLoading) {
        this.offersList = offersList;
        this.isLoading = isLoading;
    }

    @NonNull
    @Override
    public TripsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RvComingTripLoadingBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.rv_coming_trip_loading, parent, false);
        return new TripsAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TripsAdapter.ViewHolder holder, int position) {
        if (position != RecyclerView.NO_POSITION)
            holder.setViewModel(offersList.get(position));
    }

    @Override
    public int getItemCount() {
        if (offersList == null) return 0;
        return offersList.size();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull TripsAdapter.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.bind();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull TripsAdapter.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.unbind();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private RvComingTripLoadingBinding binding;

        ViewHolder(RvComingTripLoadingBinding binding) {
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

        void setViewModel(ModelBooking offerModel) {
            if (binding != null) {

                if (isLoading) {
                    binding.loaderImage.startShimmerAnimation();
                    binding.loaderDate.startShimmerAnimation();
                    binding.loaderName.startShimmerAnimation();

                }
            }
        }

    }

}




