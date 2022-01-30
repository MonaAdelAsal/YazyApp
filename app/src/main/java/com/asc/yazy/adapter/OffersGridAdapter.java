package com.asc.yazy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.asc.yazy.R;
import com.asc.yazy.api.model.ModelOffer;
import com.asc.yazy.databinding.OfferCellBinding;
import com.asc.yazy.interfaces.OnOfferItemListener;

import java.util.List;

public class OffersGridAdapter extends RecyclerView.Adapter<OffersGridAdapter.ViewHolder> {

    private final Context context;
    private final List<ModelOffer> offersList;
    private final OnOfferItemListener listener;
    private final boolean isLoading;

    public OffersGridAdapter(Context context, List<ModelOffer> offersList, OnOfferItemListener listener, boolean isLoading) {
        this.context = context;
        this.offersList = offersList;
        this.isLoading = isLoading;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OffersGridAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OfferCellBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.offer_cell, parent, false);
        binding.setListener(listener);
        return new OffersGridAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OffersGridAdapter.ViewHolder holder, int position) {
        if (position != RecyclerView.NO_POSITION)
            holder.setViewModel(offersList.get(position));
    }

    @Override
    public int getItemCount() {
        if (offersList == null) return 0;
        return offersList.size();
    }


    @Override
    public void onViewAttachedToWindow(@NonNull OffersGridAdapter.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.bind();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull OffersGridAdapter.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.unbind();
    }



    class ViewHolder extends RecyclerView.ViewHolder {
        private OfferCellBinding binding;

        ViewHolder(OfferCellBinding binding) {
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

        void setViewModel(ModelOffer offerModel) {
            if (binding != null) {

                if (isLoading) {
                    binding.loaderImage.startShimmerAnimation();
                    binding.loaderDestination.startShimmerAnimation();
                    binding.loaderPrice.startShimmerAnimation();
                    binding.loaderDate.startShimmerAnimation();
                    binding.loaderFavorite.startShimmerAnimation();

                } else {
                    /*
                    binding.loaderImage.stopShimmerAnimation();
                    binding.loaderDestination.stopShimmerAnimation();
                    binding.loaderPrice.stopShimmerAnimation();
                    binding.loaderDate.stopShimmerAnimation();
                    binding.loaderFavorite.stopShimmerAnimation();

                    binding.setOfferData(offerModel);

                    binding.tvDestination.setBackground(null);
                    binding.tvCurrency.setBackground(null);
                    binding.tvPrice.setBackground(null);
                    binding.tvFavCount.setBackground(null);
                    binding.tvDate.setBackground(null);


                    if (offerModel.getFavorites_count() == null || offerModel.getFavorites_count().isEmpty())
                        binding.loaderFavorite.setVisibility(View.GONE);
                    else {
                        binding.loaderFavorite.setVisibility(View.VISIBLE);
                        //  startCountUpAnimation(offerModel.getFavorites_count(), binding.tvFavCount);
                        binding.tvFavCount.setText(offerModel.getFavorites_count());
                        binding.likeAnimation.playAnimation();
                    }

                    binding.tvDate.setText(Constants.getFormattedDate(offerModel));

                    if (offerModel.getImage() != null && !offerModel.getImage().trim().isEmpty()) {
                        RequestOptions requestOptions = new RequestOptions();
                        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(8));
                        Glide.with(context).asBitmap().load(offerModel.getImage()).diskCacheStrategy(DiskCacheStrategy.DATA).apply(requestOptions).placeholder(R.color.gray).into(binding.OfferImg);
                    }
                    else
                        binding.OfferImg.setImageResource(R.color.gray);

                    cashOffer(offerModel);
                    */
                }

            }
        }

    }

}



