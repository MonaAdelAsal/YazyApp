package com.asc.yazy.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.asc.yazy.R;
import com.asc.yazy.fragment.CountryFragment;
import com.asc.yazy.utils.Constants;

public class WalkThroughActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constants.changeStatusBarColor(this, R.color.white);
        DataBindingUtil.setContentView(this, R.layout.activity_walk);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainContent, new CountryFragment())
                .addToBackStack("FragmentRegister")
                .commit();
    }
}
