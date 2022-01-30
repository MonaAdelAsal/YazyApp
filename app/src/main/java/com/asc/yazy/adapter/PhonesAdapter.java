package com.asc.yazy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.asc.yazy.R;
import com.asc.yazy.databinding.PhoneCellBinding;
import com.asc.yazy.utils.UtilsShare;

import java.util.List;
import java.util.Objects;

public class PhonesAdapter extends RecyclerView.Adapter<PhonesAdapter.ViewHolder> {

    private final boolean isLoading;
    private final List<String> phones;
    private final Context context;

    public PhonesAdapter(List<String> phones, boolean isLoading, Context context) {
        this.phones = phones;
        this.isLoading = isLoading;
        this.context = context;
    }

    @NonNull
    @Override
    public PhonesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PhoneCellBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.phone_cell, parent, false);
        return new PhonesAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PhonesAdapter.ViewHolder holder, int position) {
        if (position != RecyclerView.NO_POSITION)
            holder.setViewModel(phones.get(position));
    }

    @Override
    public int getItemCount() {
        if (phones == null) return 0;
        return phones.size();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull PhonesAdapter.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.bind();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull PhonesAdapter.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.unbind();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private PhoneCellBinding binding;

        ViewHolder(PhoneCellBinding binding) {
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

        void setViewModel(String mobile) {
            if (binding != null) {

                if (isLoading) {
                    binding.phone.setText(mobile);
                    binding.llPhone.setOnClickListener(v -> {
                        if (mobile != null && !mobile.isEmpty()) {
                            UtilsShare.dial(context, mobile);
                        } else {
                            Toast.makeText(context, Objects.requireNonNull(context).getResources().getString(R.string.can_not_perfrom), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        }

    }

}




