package com.asc.yazy.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.asc.yazy.R;
import com.asc.yazy.activity.AuthenticationActivity;
import com.asc.yazy.api.model.ModelOffer;
import com.asc.yazy.api.model.ModelUser;
import com.asc.yazy.api.pagination.offers.NetworkState;
import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.databinding.LoginDialogBinding;
import com.asc.yazy.databinding.OfferCellPagedBinding;
import com.asc.yazy.interfaces.OnOfferItemListener;
import com.asc.yazy.service.FavoriteTask;
import com.asc.yazy.utils.AnalyticsUtility;
import com.asc.yazy.utils.Constants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;
import java.util.Objects;

public class AllOffersAdapter extends RecyclerView.Adapter<AllOffersAdapter.ViewHolder> {

    public static boolean faveAction = false;
    private final Context context;
    private final List<ModelOffer> offersList;
    private final OnOfferItemListener listener;
    private final boolean isLoading;
    private ModelUser user;

    public AllOffersAdapter(Context context, List<ModelOffer> offersList, OnOfferItemListener listener, boolean isLoading) {
        this.context = context;
        this.offersList = offersList;
        this.isLoading = isLoading;
        this.listener = listener;
        this.user = UtilsPreference.getInstance(context).getUser();
    }

    @NonNull
    @Override
    public AllOffersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OfferCellPagedBinding itemView = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.offer_cell_paged, parent, false);
        itemView.setListener(listener);
        return new AllOffersAdapter.ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull AllOffersAdapter.ViewHolder holder, int position) {
        if (position != RecyclerView.NO_POSITION)
            holder.setViewModel(offersList.get(position));
    }

    @Override
    public int getItemCount() {
        if (offersList == null) return 0;
        return offersList.size();
    }


    @Override
    public void onViewAttachedToWindow(@NonNull AllOffersAdapter.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.bind();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull AllOffersAdapter.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.unbind();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private OfferCellPagedBinding binding;

        ViewHolder(OfferCellPagedBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.layoutLike.setOnClickListener(this);
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
                    binding.likeAnimation.setVisibility(View.INVISIBLE);

                } else {

                    binding.loaderImage.stopShimmerAnimation();
                    binding.loaderDestination.stopShimmerAnimation();
                    binding.loaderPrice.stopShimmerAnimation();
                    binding.loaderDate.stopShimmerAnimation();
                    binding.loaderFavorite.stopShimmerAnimation();
                    binding.likeAnimation.setVisibility(View.VISIBLE);
                    binding.loaderFavorite.setBackground(null);
                    binding.tvDestination.setBackground(null);
                    //   binding.tvCurrency.setBackground(null);
                    binding.tvPrice.setBackground(null);
                    binding.tvFavCount.setBackground(null);
                    binding.tvDate.setBackground(null);
                    binding.setOfferData(offerModel);

                    binding.tvPrice.setText((offerModel.getPrice() + " " + offerModel.getCurrency()));

                    if (offerModel.getMulti_cities() == 1) {
                        binding.tvMulti.setVisibility(View.VISIBLE);
                    }

                    if (getAdapterPosition() != RecyclerView.NO_POSITION)
                        offerModel.getLiveFav().observe((LifecycleOwner) context, integer -> {
                            Log.d("OfferDetailsFragment", "adapter onChanged: " + integer + " pos " + getAdapterPosition() + "Count " + offerModel.getFavourite_count() + " favAcition " + faveAction);
                            if (getAdapterPosition() == RecyclerView.NO_POSITION)
                                return;
                            if (!faveAction)
                                return;
                            Log.d("OfferDetailsFragment", "adapter to change: " + integer + " pos " + getAdapterPosition() + " favAcition " + faveAction);
                            if (integer == 1) {
                                binding.likeAnimation.setImageResource(R.drawable.ic_favorite_selected);
                                int count = Integer.parseInt(offersList.get(getAdapterPosition()).getFavourite_count()) + 1;
                                offersList.get(getAdapterPosition()).setFavourite_count(String.valueOf(count));
                                binding.tvFavCount.setText(String.valueOf(count));
                                if (count == 0)
                                    binding.tvFavCount.setVisibility(View.GONE);
                                else
                                    binding.tvFavCount.setVisibility(View.VISIBLE);

                            } else {
                                binding.likeAnimation.setImageResource(R.drawable.ic_favorite_unselected);
                                int curCount = Integer.parseInt(offersList.get(getAdapterPosition()).getFavourite_count());
                                if (curCount > 0) {
                                    int count = curCount - 1;
                                    offersList.get(getAdapterPosition()).setFavourite_count(String.valueOf(count));
                                    binding.tvFavCount.setText(String.valueOf(count));
                                    if (count == 0)
                                        binding.tvFavCount.setVisibility(View.GONE);
                                    else
                                        binding.tvFavCount.setVisibility(View.VISIBLE);

                                }
                            }
                            faveAction = false;
                        });

                    if (offerModel.getFavorites_count() != null && !offerModel.getFavorites_count().isEmpty()) {
                        binding.tvFavCount.setText(offerModel.getFavorites_count());
                        if (offerModel.getFavorites_count().equals("0"))
                            binding.tvFavCount.setVisibility(View.GONE);
                        else
                            binding.tvFavCount.setVisibility(View.VISIBLE);
                    }
                    if (user == null || user.getAccess_token() == null || user.getAccess_token().isEmpty()) {
                        binding.likeAnimation.setVisibility(View.VISIBLE);
                        binding.layoutLike.setOnClickListener(this);
                    } else {
                        binding.likeAnimation.setVisibility(View.VISIBLE);
                        if (offerModel.getIs_favorited() == 1)
                            binding.likeAnimation.setImageResource(R.drawable.ic_favorite_selected);
                        else
                            binding.likeAnimation.setImageResource(R.drawable.ic_favorite_unselected);
                    }

                    if (offerModel.getImage() != null && !offerModel.getImage().trim().isEmpty())
                        Glide.with(context).asBitmap().load(offerModel.getImage()).diskCacheStrategy(DiskCacheStrategy.DATA).placeholder(R.color.gray).into(binding.OfferImg);
                    else
                        binding.OfferImg.setImageResource(R.color.gray);
                    if (offerModel.getOpen_package() == 0) {
                        binding.tvDate.setText(Constants.getFormattedDate(offerModel));
                    } else {
                        binding.tvDate.setText(offerModel.getDuration());
                    }


                    //if trip is multi city trip
                    /*
                    if (offerModel.getMulti_cities() == 1) {
                        binding.tvMulti.setVisibility(View.VISIBLE);
                    }
                    */

                }

            }
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onClick(View v) {
            if (v == null)
                return;
            if (v.getId() == R.id.layoutLike) {
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.LIKE);
                user = UtilsPreference.getInstance(context).getUser();
                System.out.println(" Userrrr    " + user.getAccess_token());
                if (user == null || user.getAccess_token() == null || user.getAccess_token().isEmpty()) {
                    Dialog LoginToAddFav = new Dialog(Objects.requireNonNull(context), android.R.style.Theme_DeviceDefault_Dialog);
                    LoginToAddFav.setCancelable(true);
                    Objects.requireNonNull(LoginToAddFav.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    LoginDialogBinding DialogBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.login_dialog, null, false);
                    LoginToAddFav.setContentView(DialogBinding.getRoot());
                    DialogBinding.btnCancel.setOnClickListener(v1 -> LoginToAddFav.dismiss());
                    DialogBinding.btnLogin.setOnClickListener(v12 -> context.startActivity(new Intent(context, AuthenticationActivity.class)));
                    LoginToAddFav.show();
                } else {
                    if (getAdapterPosition() == RecyclerView.NO_POSITION)
                        return;
                    ModelOffer offer = offersList.get(getAdapterPosition());
                    if (offer == null)
                        return;
                    int pre = offer.getIs_favorited();
                    faveAction = true;
                    new FavoriteTask(context, offer, offer.getIs_favorited(), count -> {
                        if (count == NetworkState.NO_NET) {
                            faveAction = true;
                            offersList.get(getAdapterPosition()).setIs_favorited(pre);

                        }
                    }).execute();
                    if (offer.getIs_favorited() == 1) {
                        offersList.get(getAdapterPosition()).setIs_favorited(0);
                    } else {
                        offersList.get(getAdapterPosition()).setIs_favorited(1);

                    }

                }

            }
        }
    }

}



