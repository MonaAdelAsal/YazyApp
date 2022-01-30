package com.asc.yazy.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.asc.yazy.R;
import com.asc.yazy.api.model.ModelTravellerDetails;
import com.asc.yazy.core.GlobalInfoDialog;
import com.asc.yazy.databinding.RvSelectTravellerBinding;

import java.util.List;

public class BookingTravellersAdapter extends RecyclerView.Adapter<BookingTravellersAdapter.ViewHolder> {

    private final Context context;
    private final boolean isLoading;
    private final List<ModelTravellerDetails> offersList;
    private int limit = 1;


    public BookingTravellersAdapter(Context context, List<ModelTravellerDetails> offersList, boolean isLoading) {
        this.context = context;
        this.offersList = offersList;
        this.isLoading = isLoading;

    }

    @NonNull
    @Override
    public BookingTravellersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RvSelectTravellerBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.rv_select_traveller, parent, false);
        return new BookingTravellersAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingTravellersAdapter.ViewHolder holder, int position) {
        if (position != RecyclerView.NO_POSITION)
            holder.setViewModel(offersList.get(position));
    }

    @Override
    public int getItemCount() {
        if (offersList == null) return 0;
        return offersList.size();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull BookingTravellersAdapter.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.bind();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull BookingTravellersAdapter.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.unbind();
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RvSelectTravellerBinding binding;

        ViewHolder(RvSelectTravellerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.layout.setOnClickListener(this);
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
        void setViewModel(ModelTravellerDetails offerModel) {
            if (binding != null) {

                if (isLoading) {
                    binding.loaderName.startShimmerAnimation();
                } else {
                    binding.loaderName.stopShimmerAnimation();
                    binding.tvName.setBackground(null);
                    binding.setData(offerModel);

                    if (offerModel.isSelected())
                        binding.img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_select_on));
                    else
                        binding.img.setImageDrawable(context.getResources().getDrawable(R.drawable.select_off_gray));

                    if (getAdapterPosition() != RecyclerView.NO_POSITION && getAdapterPosition() == offersList.size() - 1)
                        binding.line.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onClick(View v) {
            if (v == null)
                return;
            if (getAdapterPosition() == RecyclerView.NO_POSITION)
                return;

            if (v.getId() == R.id.layout) {

                if (getAdapterPosition() == RecyclerView.NO_POSITION)
                    return;
                if (limit == 0)
                    return;
                boolean current = offersList.get(getAdapterPosition()).isSelected();
                if (current) {
                    offersList.get(getAdapterPosition()).toggle();
                    notifyItemChanged(getAdapterPosition());
                } else {
                    int selectedCount = 0;
                    for (ModelTravellerDetails model : offersList) {
                        if (model.isSelected())
                            selectedCount++;
                    }
                    if (selectedCount < limit) {
                        offersList.get(getAdapterPosition()).toggle();
                        notifyItemChanged(getAdapterPosition());
                    } else {
                        GlobalInfoDialog.getInstance(context).setTitle(context.getString(R.string.error)).setMessage(context.getString(R.string.no_more)).show();

                    }
                }


            }
        }
    }

}




