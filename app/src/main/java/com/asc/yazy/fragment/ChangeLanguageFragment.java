package com.asc.yazy.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.asc.yazy.R;
import com.asc.yazy.activity.SplashActivity;
import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.databinding.FragmentChangeLanguageBinding;
import com.asc.yazy.interfaces.IFragment;
import com.asc.yazy.utils.AnalyticsUtility;
import com.asc.yazy.utils.Constants;

import java.util.Locale;

public class ChangeLanguageFragment extends Fragment implements View.OnClickListener, IFragment {

    private FragmentChangeLanguageBinding binding;
    private String lan;

    public ChangeLanguageFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_change_language, null, false);
        AnalyticsUtility.logEventOpen(AnalyticsUtility.Events.CHANGE_LANGUAGE);
        AnalyticsUtility.setScreen(this);
        CheckLanguage checkLanguage = new CheckLanguage(getActivity());
        checkLanguage.UpdateLanguage();

        boolean isEnglish = getResources().getBoolean(R.bool.english_lan);
        if (isEnglish) {
            lan = "en";
            binding.imgEnglish.setVisibility(View.VISIBLE);
            binding.imgArabic.setVisibility(View.GONE);
        } else {
            lan = "ar";
            binding.imgEnglish.setVisibility(View.GONE);
            binding.imgArabic.setVisibility(View.VISIBLE);
        }
        Constants.setLanguage(lan);
        binding.imgBack.setOnClickListener(this);
        binding.btnChange.setOnClickListener(this);
        binding.layoutArabic.setOnClickListener(this);
        binding.layoutEnglish.setOnClickListener(this);
        return binding.getRoot();
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (v == null)
            return;
        switch (v.getId()) {
            case R.id.imgBack:
                if (getActivity() != null)
                    getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.btnChange:
                updateLanguage();
                AnalyticsUtility.logEventOpen(AnalyticsUtility.Actions.CHANGE_LANGUAGE_CONFIRM);
                break;
            case R.id.layoutArabic:
                lan = "ar";

                binding.imgEnglish.setVisibility(View.GONE);
                binding.imgArabic.setVisibility(View.VISIBLE);
                AnalyticsUtility.logEventOpen(AnalyticsUtility.Actions.CHANGE_LANGUAGE_ARABIC);
                break;
            case R.id.layoutEnglish:
                lan = "en";
                binding.imgEnglish.setVisibility(View.VISIBLE);
                binding.imgArabic.setVisibility(View.GONE);
                AnalyticsUtility.logEventOpen(AnalyticsUtility.Actions.CHANGE_LANGUAGE_ENGLISH);
                break;
        }
    }

    private void updateLanguage() {
        if (getActivity() == null)
            return;
        UtilsPreference.getInstance(getActivity()).savePreference(Constants.LANGUAGE, lan);
        Locale locale = new Locale(lan);
        Constants.setLanguage(lan);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
        getActivity().finish();
        Intent refresh = new Intent(getActivity(), SplashActivity.class);
        startActivity(refresh);

      /*  UtilsPreference.getInstance(getActivity()).savePreference(Constants.LANGUAGE, lan);
        Locale locale = new Locale(lan);
        Locale.setDefault(locale);
        Resources resources = this.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        configuration.setLayoutDirection(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        getActivity().startActivity(new Intent(getActivity(), SplashActivity.class));
        getActivity().finish();*/
    }

    @Override
    public void onBack() {
        if (getActivity() != null)
            getActivity().getSupportFragmentManager().popBackStack();
    }
}
