package com.asc.yazy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.asc.yazy.R;
import com.asc.yazy.api.model.ModelBooking;
import com.asc.yazy.databinding.RvComingTripBinding;
import com.asc.yazy.interfaces.OnTripListener;
import com.asc.yazy.utils.Constants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ItemViewHolder> {

    private final Context context;
    private final OnTripListener listener;
    private final List<ModelBooking> list;
    private final boolean isPending;


    public BookingAdapter(Context context, List<ModelBooking> list, OnTripListener listener, boolean isPending) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        this.isPending = isPending;
    }

    @NonNull
    @Override
    public BookingAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RvComingTripBinding itemView = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.rv_coming_trip, parent, false);
        itemView.setListener(listener);
        return new BookingAdapter.ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingAdapter.ItemViewHolder holder, int position) {
        if (position != RecyclerView.NO_POSITION)
            holder.setViewModel(list.get(position));
    }

    @Override
    public int getItemCount() {
        if (list == null) return 0;
        return list.size();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull BookingAdapter.ItemViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.bind();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull BookingAdapter.ItemViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.unbind();
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {

        private RvComingTripBinding binding;

        ItemViewHolder(RvComingTripBinding binding) {
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

        void setViewModel(final ModelBooking offerModel) {
            if (binding != null) {

                binding.setTrip(offerModel);

                if (offerModel.getImage() != null && !offerModel.getImage().trim().isEmpty())
                    Glide.with(context).asBitmap().load(offerModel.getImage()).diskCacheStrategy(DiskCacheStrategy.DATA).placeholder(R.color.gray).into(binding.img);
                else
                    binding.img.setImageResource(R.color.gray);
                if (offerModel.getOpen_package() == 1 && offerModel.getStart_date() != null) {
                    binding.tvDate.setText(Constants.getFormattedDate(offerModel, 1));
                } else
                    binding.tvDate.setText(Constants.getFormattedDate(offerModel, 0));

                if (isPending) {
                    switch (offerModel.getStatus_mobile()) {
                        case 1:
                            binding.tvStatus.setText(context.getResources().getString(R.string.wait_payment));
                            break;
                        case 2:
                            binding.tvStatus.setText(context.getResources().getString(R.string.wait_date_selection));
                            break;
                        case 3:
                            binding.tvStatus.setText(R.string.reservation_in_progress);
                            break;
                        case 6:
                            binding.tvStatus.setText(R.string.paid_wating_reservation);
                            break;

                    }
                    binding.tvStatus.setVisibility(View.VISIBLE);
                } else if (offerModel.getStatus_mobile() == 7) {
                    binding.tvStatus.setVisibility(View.VISIBLE);
                    binding.tvStatus.setText(R.string.reservation_conffirmed);
                } else {
                    binding.tvStatus.setVisibility(View.GONE);
                }


            }
        }
    }


}
