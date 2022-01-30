package com.asc.yazy.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.asc.yazy.R;
import com.asc.yazy.activity.AuthenticationActivity;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.databinding.FragmentUnAuthorizedBinding;
import com.asc.yazy.interfaces.IFragment;
import com.asc.yazy.utils.AnalyticsUtility;
import com.asc.yazy.utils.Constants;


public class UnAuthorizedFragment extends Fragment implements View.OnClickListener, IFragment {


    public static boolean isAlive = false;
    private long mLastClickTime = System.currentTimeMillis();

    public UnAuthorizedFragment() {
        isAlive = true;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("UNAUTHORIZED", "onCreateView: " + isAlive);
        FragmentUnAuthorizedBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_un_authorized, null, false);
        binding.btnLogin.setOnClickListener(this);
        binding.llRegister.setOnClickListener(this);
        CheckLanguage checkLanguage = new CheckLanguage(getActivity());
        checkLanguage.UpdateLanguage();
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isAlive = false;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (v == null)
            return;
        long now = System.currentTimeMillis();
        if (now - mLastClickTime < Constants.CLICK_TIME_INTERVAL) {
            return;
        }
        mLastClickTime = now;
        switch (v.getId()) {
            case R.id.btnLogin:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.PROFILE_LOGIN);
                startActivity(new Intent(getActivity(), AuthenticationActivity.class));
                break;
            case R.id.llRegister:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_REGISTER);
                Intent intent = new Intent(getActivity(), AuthenticationActivity.class);
                intent.putExtra(Constants.TRANSACTION, Constants.REGISTRATION);
                startActivity(intent);
                break;
        }

    }

    @Override
    public void onBack() {
        if (getActivity() != null)
            getActivity().getSupportFragmentManager().popBackStack();
    }

}
