package com.asc.yazy.core;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.asc.yazy.R;
import com.asc.yazy.databinding.DialogCongratulationBinding;

public class CongratulationDialog extends Dialog {

    private final Context context;
    private String title;
    private String message;


    private CongratulationDialog(@NonNull Context context) {
        super(context, R.style.DialogSlideAnim);
        this.context = context;

    }

    @NonNull
    public static CongratulationDialog getInstance(Context context) {
        return new CongratulationDialog(context);
    }

    public CongratulationDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public CongratulationDialog setMessage(String message) {
        this.message = message;
        return this;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CheckLanguage checkLanguage = new CheckLanguage(context);
        checkLanguage.UpdateLanguage();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getWindow() != null)
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        DialogCongratulationBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_congratulation, null, false);
        setContentView(binding.getRoot());
        binding.tvTitle.setText(title);
        String sourceString = (context.getString(R.string.you_got) + "<b> " + message + " </b> " + context.getString(R.string.won_points));
        binding.tvMessage.setText(Html.fromHtml(sourceString));
        binding.btnOk.setOnClickListener(v -> dismiss());
    }
}

