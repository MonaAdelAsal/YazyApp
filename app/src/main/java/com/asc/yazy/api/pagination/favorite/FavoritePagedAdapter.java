package com.asc.yazy.api.pagination.favorite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.asc.yazy.R;
import com.asc.yazy.activity.MainActivity;
import com.asc.yazy.api.model.ModelFav;
import com.asc.yazy.api.model.ModelUser;
import com.asc.yazy.api.pagination.offers.NetworkState;
import com.asc.yazy.api.pagination.onLoadData;
import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.databinding.FaveCellPagedBinding;
import com.asc.yazy.interfaces.OnFavoriteItemListener;
import com.asc.yazy.service.FavoriteTask;
import com.asc.yazy.utils.AnalyticsUtility;
import com.asc.yazy.utils.Constants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.Objects;

public class FavoritePagedAdapter extends PagedListAdapter<ModelFav, FavoritePagedAdapter.ItemViewHolder> {


    private static final DiffUtil.ItemCallback<ModelFav> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<ModelFav>() {
                @Override
                public boolean areItemsTheSame(ModelFav oldItem, ModelFav newItem) {
                    return oldItem.getId().equals(newItem.getId());
                }
                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(ModelFav oldItem, @NonNull ModelFav newItem) {
                    return oldItem.equals(newItem);
                }
            };
    private final Context context;
    private final OnFavoriteItemListener listener;

    private final ModelUser user;
    private final onLoadData updateData;

    public FavoritePagedAdapter(Context context, OnFavoriteItemListener listener, onLoadData updateData) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.listener = listener;
        this.user = UtilsPreference.getInstance(context).getUser();
        this.updateData = updateData;
    }

    @NonNull
    @Override
    public FavoritePagedAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FaveCellPagedBinding itemView = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.fave_cell_paged, parent, false);
        itemView.setListener(listener);
        return new FavoritePagedAdapter.ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritePagedAdapter.ItemViewHolder holder, int position) {
        if (position == RecyclerView.NO_POSITION)
            return;
        ModelFav item = getItem(position);
        if (item != null) {
            holder.setViewModel(item);
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull FavoritePagedAdapter.ItemViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.bind();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull FavoritePagedAdapter.ItemViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.unbind();
    }


    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private FaveCellPagedBinding binding;

        ItemViewHolder(FaveCellPagedBinding binding) {
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

        void setViewModel(final ModelFav offerModel) {
            if (binding != null) {

                binding.setFave(offerModel);

                if (offerModel.getFavourite_count() != null && !offerModel.getFavourite_count().isEmpty())
                    // startCountUpAnimation(offerModel.getFavourite_count(), binding.tvFavCount);
                    binding.tvFavCount.setText(offerModel.getFavourite_count());
                if (user == null || user.getAccess_token() == null || user.getAccess_token().isEmpty()) {
                    binding.likeAnimation.setVisibility(View.GONE);
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

                if (offerModel.getOfferOpenPackage().equals("1") && offerModel.getOfferDuration() != null) {
                    binding.tvDate.setText(offerModel.getOfferDuration());
                } else {
                    binding.tvDate.setText(Constants.getFormattedDate(offerModel));
                }

            }
        }

        @Override
        public void onClick(View v) {
            if (v == null)
                return;
            if (v.getId() == R.id.layoutLike) {
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.LIKE);
                new FavoriteTask(context, Objects.requireNonNull(getItem(getAbsoluteAdapterPosition())), 1).execute();
                binding.likeAnimation.setImageResource(R.drawable.ic_favorite_unselected);
                //(getAdapterPosition());
                //   notifyItemRemoved(getAdapterPosition());
                updateData.onDataLoaded(NetworkState.LOADED);
                MainActivity.mainContainerViewPagerAdapter.notifyDataSetChanged();
            }
        }
    }
}
