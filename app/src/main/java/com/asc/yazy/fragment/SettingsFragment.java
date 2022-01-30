package com.asc.yazy.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.asc.yazy.R;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.databinding.FragmentSettingsBinding;
import com.asc.yazy.utils.AnalyticsUtility;
import com.asc.yazy.utils.Constants;

import java.util.Objects;


public class SettingsFragment extends Fragment implements View.OnClickListener {

    private long mLastClickTime = System.currentTimeMillis();

    public SettingsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentSettingsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false);
        CheckLanguage checkLanguage = new CheckLanguage(getActivity());
        checkLanguage.UpdateLanguage();
        binding.linearAbout.setOnClickListener(this);
        binding.linearContact.setOnClickListener(this);
        binding.linearTerms.setOnClickListener(this);
        binding.linearPrivacy.setOnClickListener(this);
        binding.llChangeLangauge.setOnClickListener(this);
        binding.linearFAQs.setOnClickListener(this);
        // Letter D for development
        // Letter T for testing
        // Letter L for live
        String version = "Version L : 00.4.400";
        binding.tvVersion.setText(version);
        AnalyticsUtility.logEvent(AnalyticsUtility.Events.MENU, AnalyticsUtility.Events.MENU);
        AnalyticsUtility.setScreen(this);
        return binding.getRoot();
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
            case R.id.llChangeLangauge:
                Objects.requireNonNull(getActivity()).getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                        .add(R.id.fullContent, new ChangeLanguageFragment())
                        .addToBackStack("ChangeLanguageFragment")
                        .commit();
                break;
            case R.id.linearAbout:
                if (getActivity() != null)
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                            .add(R.id.fullContent, new AboutUsFragment())
                            .addToBackStack("AboutUsFragment")
                            .commit();
                break;
            case R.id.linearContact:
                if (getActivity() != null)
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                            .add(R.id.fullContent, new ContactUsFragment())
                            .addToBackStack("ContactUsFragment")
                            .commit();
                break;


            case R.id.linearTerms:
                if (getActivity() != null)
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                            .add(R.id.fullContent, new TermsFragment())
                            .addToBackStack("TermsUsFragment")
                            .commit();
                break;
            case R.id.linearPrivacy:
                if (getActivity() != null)
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                            .add(R.id.fullContent, new PrivacyFragment())
                            .addToBackStack("PrivacyFragment")
                            .commit();
                break;
            case R.id.linearFAQs:
                if (getActivity() != null)
                    AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_FAQS);
                Objects.requireNonNull(getActivity()).getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                        .add(R.id.fullContent, new FAQsFragment())
                        .addToBackStack("FAQsFragment")
                        .commit();
                break;
        }
    }
}
