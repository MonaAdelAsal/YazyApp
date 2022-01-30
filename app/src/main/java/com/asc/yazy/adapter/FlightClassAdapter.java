package com.asc.yazy.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.asc.yazy.R;
import com.asc.yazy.api.model.ModelFlightClass;
import com.asc.yazy.databinding.RvFlightClassBinding;
import com.asc.yazy.interfaces.FlightClassListener;

import java.util.List;

public class FlightClassAdapter extends RecyclerView.Adapter<FlightClassAdapter.ViewHolder> {

    private final List<ModelFlightClass> offersList;
    private final Context context;
    private final FlightClassListener listener;

    public FlightClassAdapter(Context context, List<ModelFlightClass> offersList, FlightClassListener listener) {
        this.context = context;
        this.offersList = offersList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FlightClassAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RvFlightClassBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.rv_flight_class, parent, false);
        return new FlightClassAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FlightClassAdapter.ViewHolder holder, int position) {
        if (position != RecyclerView.NO_POSITION)
            holder.setViewModel(offersList.get(position));
    }

    @Override
    public int getItemCount() {
        if (offersList == null) return 0;
        return offersList.size();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull FlightClassAdapter.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.bind();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull FlightClassAdapter.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.unbind();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private RvFlightClassBinding binding;

        ViewHolder(RvFlightClassBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.itemView.setOnClickListener(v -> {
                if (getAdapterPosition() == RecyclerView.NO_POSITION)
                    return;
                int indexMain = getAdapterPosition();
                boolean value = offersList.get(indexMain).isChecked();
               /* for (int index = 0; index < offersList.size(); index++) {
                    offersList.get(index).setChecked(false);
                }*/
                offersList.get(getAdapterPosition()).setChecked(value);
                offersList.get(getAdapterPosition()).toggle();
                notifyDataSetChanged();
                if (listener == null)
                    return;
                listener.onFlightClassClicked(offersList.get(indexMain).getId(), offersList.get(indexMain).isChecked());
            });
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
        void setViewModel(ModelFlightClass offerModel) {
            if (binding != null) {
                binding.tvFlightClass.setText(offerModel.getValue());
                if (offerModel.isChecked()) {
                    binding.tvFlightClass.setBackground(context.getResources().getDrawable(R.drawable.solid_blue));
                    binding.tvFlightClass.setTextColor(context.getResources().getColor(R.color.white));
                } else {
                    binding.tvFlightClass.setBackground(context.getResources().getDrawable(R.drawable.border_gray));
                    binding.tvFlightClass.setTextColor(context.getResources().getColor(R.color.black));
                }
            }
        }

    }

}




