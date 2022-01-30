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
import com.asc.yazy.databinding.FragmentOfferPolicyBinding;
import com.asc.yazy.interfaces.IFragment;
import com.asc.yazy.utils.AnalyticsUtility;
import com.asc.yazy.utils.Constants;

public class OfferPolicyFragment extends Fragment implements View.OnClickListener, IFragment {

    private final String data;
    private long mLastClickTime = System.currentTimeMillis();
    private FragmentOfferPolicyBinding binding;

    public OfferPolicyFragment(String data) {
        this.data = data;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_offer_policy, null, false);
        binding.imgBack.setOnClickListener(this);
        binding.loader.startShimmerAnimation();
        AnalyticsUtility.logEventOpen(AnalyticsUtility.Events.TERMS_AND_CONDITIONS);
        AnalyticsUtility.setScreen(this);
        CheckLanguage checkLanguage = new CheckLanguage(getActivity());
        checkLanguage.UpdateLanguage();
        loadDate();
        return binding.getRoot();
    }


    @Override
    public void onClick(View v) {
        if (v == null)
            return;
        long now = System.currentTimeMillis();
        if (now - mLastClickTime < Constants.CLICK_TIME_INTERVAL) {
            return;
        }
        mLastClickTime = now;

        if (v.getId() == R.id.imgBack) {
            if (getActivity() != null)
                getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onBack() {
        if (getActivity() != null)
            getActivity().getSupportFragmentManager().popBackStack();
    }

    private void loadDate() {
        AnalyticsUtility.logEventAPISuccess(AnalyticsUtility.Events.TERMS_AND_CONDITIONS);
        binding.loader.stopShimmerAnimation();
        binding.wvTerms.setBackground(null);
        binding.wvTerms.loadDataWithBaseURL("", data, "text/html", "UTF-8", "");

    }

}