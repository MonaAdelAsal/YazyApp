package com.asc.yazy.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.asc.yazy.R;
import com.asc.yazy.core.AdultsCountObserver;
import com.asc.yazy.databinding.RvTravellerCheckOutBinding;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdultsCheckOutAdapter extends RecyclerView.Adapter<AdultsCheckOutAdapter.ViewHolder> {

    private final List<AdultsCountObserver> offersList;

    public AdultsCheckOutAdapter(List<AdultsCountObserver> offersList) {
        this.offersList = offersList;
    }

    @NonNull
    @Override
    public AdultsCheckOutAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RvTravellerCheckOutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.rv_traveller_check_out, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdultsCheckOutAdapter.ViewHolder holder, int position) {
        if (position != RecyclerView.NO_POSITION)
            holder.setViewModel(offersList.get(position));
    }

    @Override
    public int getItemCount() {
        if (offersList == null) return 0;
        return offersList.size();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull AdultsCheckOutAdapter.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.bind();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull AdultsCheckOutAdapter.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.unbind();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        private RvTravellerCheckOutBinding binding;

        ViewHolder(RvTravellerCheckOutBinding binding) {
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

        void setViewModel(AdultsCountObserver offerModel) {
            if (binding != null) {
                try {
                    String regex = "\\s+";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(offerModel.getName());
                    String result = matcher.replaceAll(" ");
                    binding.tvName.setText((result.split(" ")[0] + " " + result.split(" ")[1]));
                } catch (Exception e) {
                    binding.tvName.setText(offerModel.getName());
                }
                if (offerModel.isCivil())
                    binding.tvNumber.setText(("" + offerModel.getCivilID()));
                else
                    binding.tvNumber.setText(("" + offerModel.getPassPortNumber()));


            }
        }

    }

}




