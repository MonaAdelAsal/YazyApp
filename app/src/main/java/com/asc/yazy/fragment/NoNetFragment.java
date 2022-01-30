package com.asc.yazy.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.asc.yazy.R;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.databinding.FragmentNoNetBinding;
import com.asc.yazy.utils.AnalyticsUtility;

import java.util.Objects;

public class NoNetFragment extends Fragment {

    public NoNetFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentNoNetBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_no_net, null, false);
        AnalyticsUtility.setScreen(this);
        CheckLanguage checkLanguage = new CheckLanguage(getActivity());
        checkLanguage.UpdateLanguage();
        binding.btnTryAgain.setOnClickListener(view -> Objects.requireNonNull(getActivity()).finish());
        return binding.getRoot();
    }
}
