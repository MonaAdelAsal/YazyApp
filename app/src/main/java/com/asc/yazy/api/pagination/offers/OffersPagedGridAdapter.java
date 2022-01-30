package com.asc.yazy.api.pagination.offers;

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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.asc.yazy.R;
import com.asc.yazy.activity.AuthenticationActivity;
import com.asc.yazy.activity.MainActivity;
import com.asc.yazy.api.model.ModelOffer;
import com.asc.yazy.api.model.ModelUser;
import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.databinding.LoginDialogBinding;
import com.asc.yazy.databinding.OfferCellGridPagedBinding;
import com.asc.yazy.fragment.SearchResultFragment;
import com.asc.yazy.interfaces.OnOfferItemListener;
import com.asc.yazy.service.FavoriteTask;
import com.asc.yazy.utils.AnalyticsUtility;
import com.asc.yazy.utils.Constants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity;

public class OffersPagedGridAdapter extends PagedListAdapter<ModelOffer, OffersPagedGridAdapter.ItemViewHolder> {

    private static final DiffUtil.ItemCallback<ModelOffer> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<ModelOffer>() {
                @Override
                public boolean areItemsTheSame(ModelOffer oldItem, ModelOffer newItem) {
                    return oldItem.getId().equals(newItem.getId());
                }


                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(ModelOffer oldItem, @NonNull ModelOffer newItem) {
                    return oldItem.equals(newItem);
                }
            };
    public static boolean faveAction = false;
    private final Context context;
    private final OnOfferItemListener listener;
    private final boolean isSearchResult;
    private final Fragment parent;
    private ModelUser user;
    private List<ModelOffer> list;

    public OffersPagedGridAdapter(Context context, OnOfferItemListener listener, Fragment parent) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.isSearchResult = false;
        this.listener = listener;
        this.user = UtilsPreference.getInstance(context).getUser();
        this.parent = parent;
    }


    @Override
    public void submitList(PagedList<ModelOffer> pagedList) {

        this.list = new ArrayList<>();
        pagedList.addWeakCallback(null, new PagedList.Callback() {
            @Override
            public void onChanged(int position, int count) {
                list.clear();
                list.addAll(pagedList);
            }

            @Override
            public void onInserted(int position, int count) {
                list.clear();
                list.addAll(pagedList);
            }

            @Override
            public void onRemoved(int position, int count) {
                list.clear();
                list.addAll(pagedList);
            }
        });

        super.submitList(pagedList);
    }

    @NonNull
    @Override
    public OffersPagedGridAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OfferCellGridPagedBinding itemView = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.offer_cell_grid_paged, parent, false);
        itemView.setListener(listener);
        return new OffersPagedGridAdapter.ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OffersPagedGridAdapter.ItemViewHolder holder, int position) {
        if (position == RecyclerView.NO_POSITION)
            return;
        ModelOffer item = getItem(position);
        if (item != null) {
            holder.setViewModel(item);
        }
        if (position == 0) {
            introViewBehaviour(holder.itemView);
        }
    }

    private void introViewBehaviour(View view) {

        if (context == null)
            return;

        if (parent instanceof SearchResultFragment) {

            boolean firstRun = UtilsPreference.getInstance(context).getPreference(Constants.IS_TRIP_ADAPTER_FIRST_RUN, true);

            if (!firstRun)
                return;


            //optional
            //optional - default dismissible by TargetView
            GuideView caseView = new GuideView.Builder(context)
                    .setTitle(Objects.requireNonNull(context).getResources().getString(R.string.intro_trip))
                    .setContentText(Objects.requireNonNull(context).getResources().getString(R.string.intro_trip_details))
                    .setTargetView(view)
                    .setGravity(Gravity.center)//optional
                    .setDismissType(DismissType.anywhere) //optional - default dismissible by TargetView
                    .build();

            caseView.show();

            UtilsPreference.getInstance(context).savePreference(Constants.IS_TRIP_ADAPTER_FIRST_RUN, false);


        }
    }

    @Override
    public int getItemCount() {
        if (list == null) return 0;
        return list.size();
    }


    @Override
    public void onViewAttachedToWindow(@NonNull OffersPagedGridAdapter.ItemViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.bind();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull OffersPagedGridAdapter.ItemViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.unbind();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private OfferCellGridPagedBinding binding;

        ItemViewHolder(OfferCellGridPagedBinding binding) {
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

        void setViewModel(final ModelOffer offerModel) {
            if (binding != null) {
                binding.setOfferData(offerModel);

                if (getAbsoluteAdapterPosition() != RecyclerView.NO_POSITION)
                    offerModel.getLiveFav().observe((LifecycleOwner) context, integer -> {
                        Log.d("OfferDetailsFragment", "adapter onChanged: " + integer + " pos " + getAbsoluteAdapterPosition() + "Count " + offerModel.getFavourite_count() + " favAcition " + faveAction);
                        if (getAbsoluteAdapterPosition() == RecyclerView.NO_POSITION)
                            return;
                        if (!faveAction)
                            return;
                        Log.d("OfferDetailsFragment", "adapter to change: " + integer + " pos " + getAbsoluteAdapterPosition() + " favAcition " + faveAction);
                        if (integer == 1) {
                            binding.likeAnimation.setImageResource(R.drawable.ic_favorite_selected);
                            int count = Integer.parseInt(list.get(getAbsoluteAdapterPosition()).getFavourite_count()) + 1;
                            list.get(getAbsoluteAdapterPosition()).setFavourite_count(String.valueOf(count));
                            binding.tvFavCount.setText(String.valueOf(count));
                            if (count == 0)
                                binding.tvFavCount.setVisibility(View.GONE);
                            else
                                binding.tvFavCount.setVisibility(View.VISIBLE);

                        } else {
                            binding.likeAnimation.setImageResource(R.drawable.ic_favorite_unselected);
                            int curCount = Integer.parseInt(list.get(getAbsoluteAdapterPosition()).getFavourite_count());
                            if (curCount > 0) {
                                int count = curCount - 1;
                                list.get(getAbsoluteAdapterPosition()).setFavourite_count(String.valueOf(count));
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

                if (offerModel.getMulti_cities() == 1) {
                    binding.tvMulti.setVisibility(View.VISIBLE);
                }
                //   cashOffer(offerModel);

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
                    if (getAbsoluteAdapterPosition() == RecyclerView.NO_POSITION)
                        return;
                    ModelOffer offer = list.get(getAbsoluteAdapterPosition());
                    if (offer == null)
                        return;
                    int pre = offer.getIs_favorited();
                    faveAction = true;
                    new FavoriteTask(context, offer, offer.getIs_favorited(), count -> {
                        if (count == NetworkState.NO_NET) {
                            faveAction = true;
                            list.get(getAbsoluteAdapterPosition()).setIs_favorited(pre);

                        }
                    }).execute();
                    if (offer.getIs_favorited() == 1) {
                        list.get(getAbsoluteAdapterPosition()).setIs_favorited(0);
                    } else {
                        list.get(getAbsoluteAdapterPosition()).setIs_favorited(1);

                    }
                    if (isSearchResult) {
                        MainActivity.mainContainerViewPagerAdapter.notifyDataSetChanged();
                    }
                }

            }
        }
    }

/*
    private void startCountUpAnimation(final String count, final TextView textView) {

        try {
            ValueAnimator animator = new ValueAnimator();
            int value = (int) Float.parseFloat(count);
            animator.setObjectValues(0, value);
            animator.addUpdateListener(animation -> textView.setText(animation.getAnimatedValue().toString()));
            animator.setDuration(500);
            animator.start();

        } catch (Exception ignored) {
            textView.setText(count);
        }


    }

    private void cashOffer(ModelOffer offer) {
        OffersRepository repository = new OffersRepository(context);
        OfferRoomModel temp = new OfferRoomModel();
        temp.setLocalID(offer.getId());
        temp.setTitle(offer.getTitle());
        temp.setDate_from(offer.getDate_from());
        temp.setLike(false);
        temp.setCurrency(offer.getCurrency());
        temp.setPricy(offer.getPrice());
        temp.setDate_to(offer.getDate_to());
        temp.setDate_from(offer.getDate_from());
        temp.setImage(offer.getImage());
        repository.insertOrUpdate(temp);
    }
*/

}
