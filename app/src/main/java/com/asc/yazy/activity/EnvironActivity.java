package com.asc.yazy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.asc.yazy.R;
import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.databinding.ActivityEnvBinding;
import com.asc.yazy.utils.Constants;


public class EnvironActivity extends AppCompatActivity implements View.OnClickListener {


    private ActivityEnvBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   Constants.changeStatusBarColor(this, R.color.colorAccent);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_env);
        binding.btnDevelopment.setOnClickListener(this);
        binding.btnTesting.setOnClickListener(this);
        binding.btnLive.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        if (v == binding.btnDevelopment) {
            UtilsPreference.getInstance(this).savePreference(Constants.ENVIRONMENT, Constants.DEVELOPMENT);
        }
        if (v == binding.btnTesting) {
            UtilsPreference.getInstance(this).savePreference(Constants.ENVIRONMENT, Constants.TESTING);
        }
        if (v == binding.btnLive) {
            UtilsPreference.getInstance(this).savePreference(Constants.ENVIRONMENT, Constants.LIVE);
        }


        startActivity(new Intent(this, SplashActivity.class));
        finish();


    }
}
