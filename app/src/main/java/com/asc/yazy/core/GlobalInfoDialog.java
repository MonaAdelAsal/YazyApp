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
import com.asc.yazy.databinding.DialogGlobalBinding;

public class GlobalInfoDialog extends Dialog {

    private static boolean isAlive = false;
    private final Context context;
    private String title;
    private String message;


    private GlobalInfoDialog(@NonNull Context context) {
        super(context, R.style.DialogSlideAnim);
        this.context = context;
        if (isAlive) {
            dismiss();
        }
    }

    @NonNull
    public static GlobalInfoDialog getInstance(Context context) {
        return new GlobalInfoDialog(context);
    }

    public GlobalInfoDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public GlobalInfoDialog setMessage(String message) {
        this.message = message;
        return this;
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
        setCancelable(false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getWindow() != null)
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        DialogGlobalBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_global, null, false);
        setContentView(binding.getRoot());
        binding.tvTitle.setText(title);
        binding.tvMessage.setText(message);
        binding.btnOk.setOnClickListener(v -> {
            isAlive = false;
            dismiss();

        });

    }
}

