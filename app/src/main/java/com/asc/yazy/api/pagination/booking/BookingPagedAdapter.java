package com.asc.yazy.api.pagination.booking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.asc.yazy.R;
import com.asc.yazy.api.model.ModelBooking;
import com.asc.yazy.databinding.RvComingTripBinding;
import com.asc.yazy.interfaces.OnTripListener;
import com.asc.yazy.utils.Constants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class BookingPagedAdapter extends PagedListAdapter<ModelBooking, BookingPagedAdapter.ItemViewHolder> {

    private static final DiffUtil.ItemCallback<ModelBooking> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<ModelBooking>() {
                @Override
                public boolean areItemsTheSame(ModelBooking oldItem, ModelBooking newItem) {
                    return oldItem.getBooking_id().equals(newItem.getBooking_id());
                }


                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(ModelBooking oldItem, @NonNull ModelBooking newItem) {
                    return oldItem.equals(newItem);
                }
            };
    private final Context context;
    private final OnTripListener listener;


    public BookingPagedAdapter(Context context, OnTripListener listener) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookingPagedAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RvComingTripBinding itemView = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.rv_coming_trip, parent, false);
        itemView.setListener(listener);
        return new BookingPagedAdapter.ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingPagedAdapter.ItemViewHolder holder, int position) {
        ModelBooking item = getItem(position);
        if (item != null) {
            holder.setViewModel(item);
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull BookingPagedAdapter.ItemViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.bind();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull BookingPagedAdapter.ItemViewHolder holder) {
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

                binding.tvDate.setText(Constants.getFormattedDate(offerModel, 0));


            }
        }
    }


}
