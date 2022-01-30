package com.asc.yazy.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.asc.yazy.R;
import com.asc.yazy.core.ModelDetails;
import com.asc.yazy.databinding.RvItemBinding;

import java.util.List;

public class TripCityDetailsAdapter extends RecyclerView.Adapter<TripCityDetailsAdapter.ViewHolder> {

    private final boolean isLoading;
    private final List<ModelDetails> list;

    public TripCityDetailsAdapter(List<ModelDetails> offersList, boolean isLoading) {
        this.list = offersList;
        this.isLoading = isLoading;
    }

    @NonNull
    @Override
    public TripCityDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RvItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.rv_item, parent, false);
        return new TripCityDetailsAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TripCityDetailsAdapter.ViewHolder holder, int position) {
        if (position != RecyclerView.NO_POSITION)
            holder.setViewModel(list.get(position));
    }

    @Override
    public int getItemCount() {
        if (list == null) return 0;
        return list.size();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull TripCityDetailsAdapter.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.bind();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull TripCityDetailsAdapter.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.unbind();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private RvItemBinding binding;

        ViewHolder(RvItemBinding binding) {
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

        void setViewModel(ModelDetails offerModel) {
            if (binding != null) {

                if (isLoading) {
                    binding.loaderKey.startShimmerAnimation();
                    binding.loaderValue.startShimmerAnimation();
                } else {
                    binding.loaderKey.stopShimmerAnimation();
                    binding.loaderValue.stopShimmerAnimation();

                    binding.tvKey.setBackground(null);
                    binding.tvValue.setBackground(null);

                    binding.tvKey.setText(offerModel.getKey());
                    binding.tvValue.setText(offerModel.getValue());

                }
            }
        }

    }

}




