package com.asc.yazy.core;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.asc.yazy.R;
import com.asc.yazy.databinding.DeleteTravelerBinding;

public class DeleteTravellerDialog extends Dialog {

    private static boolean isAlive = false;
    private final Context context;
    private final DeleteFT listener;
    private String title;
    private String message;


    public DeleteTravellerDialog(@NonNull Context context, DeleteFT listener) {
        super(context, R.style.DialogSlideAnim);
        this.context = context;
        this.listener = listener;
        if (isAlive) {
            dismiss();
        }
    }


    @Override
    public void show() {
        super.show();
        if (isAlive) {
            dismiss();
        } else {
            isAlive = true;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        isAlive = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CheckLanguage checkLanguage = new CheckLanguage(context);
        checkLanguage.UpdateLanguage();
        setCancelable(true);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getWindow() != null)
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        DeleteTravelerBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.delete_traveler, null, false);
        setContentView(binding.getRoot());

        binding.tvCancel.setOnClickListener(v -> {
            isAlive = false;
            dismiss();

        });

        binding.tvConfirm.setOnClickListener(view -> {
            if (listener != null)
                listener.deleteIndex();
            dismiss();
        });


    }


    public interface DeleteFT {
        void deleteIndex();
    }
}

