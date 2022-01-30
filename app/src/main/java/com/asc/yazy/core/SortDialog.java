package com.asc.yazy.core;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.databinding.DataBindingUtil;

import com.asc.yazy.R;
import com.asc.yazy.databinding.LayoutFiltterBinding;
import com.asc.yazy.interfaces.OnSortChangeListener;
import com.asc.yazy.utils.AnalyticsUtility;

import java.util.Objects;

public class SortDialog extends Dialog implements View.OnClickListener {

    private final LayoutFiltterBinding binding;
    private final OnSortChangeListener listener;
    private final Dialog sorting;
    private String SortBy = "";
    private boolean isMostLiked = false;
    private boolean isLessLiked = false;
    private boolean isPLTHLiked = false;
    private boolean isPHTLLiked = false;

    public SortDialog(OnSortChangeListener listener, Context context) {
        super(Objects.requireNonNull(context));
        this.listener = listener;

        sorting = new Dialog(Objects.requireNonNull(context), R.style.DialogStyle);
        sorting.setCancelable(true);
        Objects.requireNonNull(sorting.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_filtter, null, false);
        Window window = sorting.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        sorting.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        sorting.setContentView(binding.getRoot());
        binding.CheckHigh.setOnClickListener(this);
        binding.CheckLess.setOnClickListener(this);
        binding.CheckLow.setOnClickListener(this);
        binding.CheckMost.setOnClickListener(this);
        binding.tvApply.setOnClickListener(this);
        binding.FlHighPrice.setOnClickListener(this);
        binding.FlLowPrice.setOnClickListener(this);
        binding.FlMostLike.setOnClickListener(this);
        binding.FlLessLike.setOnClickListener(this);


    }

    public void showSortingOptions() {
        sorting.show();
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "NonConstantResourceId"})
    @Override
    public void onClick(View v) {
        if (v == null)
            return;
        switch (v.getId()) {
            case R.id.CheckHigh:
            case R.id.FlHighPrice:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.SEARCH_FILTER_PRICE_HIGH_TO_LOW);

                if (isPHTLLiked) {
                    clearSelection();
                } else {
                    clearSelection();
                    binding.CheckHigh.setImageDrawable(Objects.requireNonNull(getContext()).getResources().getDrawable(R.drawable.ic_select_on));
                    SortBy = "HTL";
                    isPHTLLiked = true;
                }

                /*
                clearSelection();
                isPHTLLiked =! isPHTLLiked;
                if (isPHTLLiked) {
                    binding.CheckHigh.setImageDrawable(Objects.requireNonNull(getContext()).getResources().getDrawable(R.drawable.ic_select_on));
                    SortBy = "HTL";
                } else {
                    SortBy = "";
                    binding.CheckHigh.setImageDrawable(Objects.requireNonNull(getContext()).getResources().getDrawable(R.drawable.ic_select_off));
                }
                */
                break;
            case R.id.CheckLow:
            case R.id.FlLowPrice:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.SEARCH_FILTER_PRICE_LOW_TO_HIGH);

                if (isPLTHLiked) {
                    clearSelection();
                } else {
                    clearSelection();
                    binding.CheckLow.setImageDrawable(Objects.requireNonNull(getContext()).getResources().getDrawable(R.drawable.ic_select_on));
                    SortBy = "LTH";
                    isPLTHLiked = true;
                }
                /*
                clearSelection();
                isPLTHLiked =! isPLTHLiked ;
                if (isPLTHLiked) {
                    SortBy = "LTH";
                    binding.CheckLow.setImageDrawable(Objects.requireNonNull(getContext()).getResources().getDrawable(R.drawable.ic_select_on));
                } else {
                    SortBy = "";
                    binding.CheckLow.setImageDrawable(Objects.requireNonNull(getContext()).getResources().getDrawable(R.drawable.ic_select_off));
                }
                */
                break;
            case R.id.CheckMost:
            case R.id.FlMostLike:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.SEARCH_FILTER_PRICE_MOST_LIKE);

                if (isMostLiked) {
                    clearSelection();
                } else {
                    clearSelection();
                    binding.CheckMost.setImageDrawable(Objects.requireNonNull(getContext()).getResources().getDrawable(R.drawable.ic_select_on));
                    SortBy = "MLIKED";
                    isMostLiked = true;
                }
                /*
                clearSelection();
                isMostLiked = !isMostLiked;
                if (isMostLiked) {
                    binding.CheckMost.setImageDrawable(Objects.requireNonNull(getContext()).getResources().getDrawable(R.drawable.ic_select_on));
                    SortBy = "MLIKED";
                } else {
                    SortBy = "";
                    binding.CheckMost.setImageDrawable(Objects.requireNonNull(getContext()).getResources().getDrawable(R.drawable.ic_select_off));
                }
                */
                break;
            case R.id.CheckLess:
            case R.id.FlLessLike:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.SEARCH_FILTER_PRICE_LESS_LIKE);

                if (isLessLiked) {
                    clearSelection();

                } else {
                    clearSelection();
                    binding.CheckLess.setImageDrawable(Objects.requireNonNull(getContext()).getResources().getDrawable(R.drawable.ic_select_on));
                    SortBy = "LLIKED";
                    isLessLiked = true;
                }
                /*
                clearSelection();
                isLessLiked =! isLessLiked;
                if (isLessLiked) {
                    binding.CheckLess.setImageDrawable(Objects.requireNonNull(getContext()).getResources().getDrawable(R.drawable.ic_select_on));
                    SortBy = "LLIKED";
                } else {
                    SortBy = "";
                    binding.CheckLess.setImageDrawable(Objects.requireNonNull(getContext()).getResources().getDrawable(R.drawable.ic_select_off));
                }
                */


                break;
            case R.id.tvApply:
                listener.onSortChangeListener(SortBy);
                sorting.dismiss();
                break;
        }

    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private void clearSelection() {

        isMostLiked = false;
        isLessLiked = false;
        isPLTHLiked = false;
        isPHTLLiked = false;
        binding.CheckMost.setImageDrawable(Objects.requireNonNull(getContext()).getResources().getDrawable(R.drawable.ic_select_off));
        binding.CheckHigh.setImageDrawable(Objects.requireNonNull(getContext()).getResources().getDrawable(R.drawable.ic_select_off));
        binding.CheckLow.setImageDrawable(Objects.requireNonNull(getContext()).getResources().getDrawable(R.drawable.ic_select_off));
        binding.CheckLess.setImageDrawable(Objects.requireNonNull(getContext()).getResources().getDrawable(R.drawable.ic_select_off));
    }
}
