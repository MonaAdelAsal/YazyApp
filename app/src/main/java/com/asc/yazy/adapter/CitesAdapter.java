package com.asc.yazy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.asc.yazy.R;
import com.asc.yazy.api.model.CityModel;
import com.asc.yazy.databinding.RvCitesBinding;
import com.asc.yazy.interfaces.OnCityListener;
import com.asc.yazy.utils.Constants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class CitesAdapter extends RecyclerView.Adapter<CitesAdapter.ViewHolder> {

    private final boolean isLoading;
    private final Context context;
    private final List<CityModel> offersList;
    private final OnCityListener listener;
    private final int open_package;

    public CitesAdapter(Context context, List<CityModel> offersList, boolean isLoading, OnCityListener listener, int open_package) {
        this.context = context;
        this.offersList = offersList;
        this.isLoading = isLoading;
        this.listener = listener;
        this.open_package = open_package;

    }

    @NonNull
    @Override
    public CitesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RvCitesBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.rv_cites, parent, false);
        binding.setListener(listener);
        return new CitesAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CitesAdapter.ViewHolder holder, int position) {
        if (position != RecyclerView.NO_POSITION)
            holder.setViewModel(offersList.get(position));
            holder.binding.setPosition(position);
    }

    @Override
    public int getItemCount() {
        if (offersList == null) return 0;
        return offersList.size();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull CitesAdapter.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.bind();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull CitesAdapter.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.unbind();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private RvCitesBinding binding;

        ViewHolder(RvCitesBinding binding) {
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

        void setViewModel(CityModel offerModel) {
            if (binding != null) {

                if (isLoading) {
                    binding.imgLoader.startShimmerAnimation();
                    binding.tvNameLoader.startShimmerAnimation();
                    binding.tvDateLoader.startShimmerAnimation();
                } else {
                    binding.imgLoader.stopShimmerAnimation();
                    binding.tvNameLoader.stopShimmerAnimation();
                    binding.tvDateLoader.stopShimmerAnimation();

                    binding.tvDate.setBackground(null);
                    binding.tvDestination.setBackground(null);

                    binding.setTrip(offerModel);

                    if (offerModel.getImage() != null && !offerModel.getImage().trim().isEmpty())
                        Glide.with(context).asBitmap().load(offerModel.getImage()).diskCacheStrategy(DiskCacheStrategy.DATA).placeholder(R.color.gray).into(binding.img);
                    else
                        binding.img.setImageResource(R.color.gray);

                    // binding.tvDate.setText(Constants.getFormattedDate(offerModel));
                    if (open_package == 0) {
                        binding.tvDate.setText(Constants.getFormattedDate(offerModel));
                    } else {
                        binding.tvDate.setText(offerModel.getDuration());
                    }

                }
            }
        }

    }

}




