package com.asc.yazy.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.asc.yazy.R;
import com.asc.yazy.api.model.ModelUser;
import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.databinding.FragmentAccountInfoBinding;
import com.asc.yazy.interfaces.IFragment;
import com.asc.yazy.interfaces.IUpdatableFragment;
import com.asc.yazy.utils.AnalyticsUtility;
import com.asc.yazy.utils.Constants;

import java.util.Objects;

public class AccountInfoFragment extends Fragment implements View.OnClickListener, IFragment, IUpdatableFragment {


    private long mLastClickTime = System.currentTimeMillis();
    private FragmentAccountInfoBinding binding;
    private ModelUser user;

    public AccountInfoFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_account_info, null, false);
        AnalyticsUtility.logEventOpen(AnalyticsUtility.Events.ACCOUNT_INFO);
        AnalyticsUtility.setScreen(this);
        binding.imgBack.setOnClickListener(this);
        binding.btnUpdate.setOnClickListener(this);
        CheckLanguage checkLanguage = new CheckLanguage(getActivity());
        checkLanguage.UpdateLanguage();
        binding.view.setOnClickListener(null);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoadData();
    }

    private void LoadData() {
        user = UtilsPreference.getInstance(getActivity()).getUser();
        binding.setProfileData(user);
        if (UtilsPreference.getInstance(getActivity()).getPreference(Constants.LANGUAGE, "ar").equals("en")) {
            binding.tvNumber.setText((("(" + user.getCountry_code() + ") ") + user.getMobile()));
        } else {
            binding.tvNumber.setText(((user.getMobile() + "(" + user.getCountry_code() + ") ")));
        }
        binding.cpNationality.setCountryForNameCode(String.valueOf(user.getNationality()));
        AnalyticsUtility.logEventLoadDate(AnalyticsUtility.Events.ACCOUNT_INFO);
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

        switch (v.getId()) {
            case R.id.imgBack:
                if (getActivity() != null)
                    getActivity().getSupportFragmentManager().popBackStack();
                break;

            case R.id.btnUpdate:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_EDIT_PROFILE);
                Objects.requireNonNull(getActivity()).getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                        .add(R.id.fullContent, new EditProfileFragment())
                        .addToBackStack("EditProfileFragment")
                        .commit();
                break;
        }
    }


    @Override
    public void onBack() {
        if (getActivity() != null)
            getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onFragmentUpdate() {

        user = UtilsPreference.getInstance(getActivity()).getUser();
        if (user != null && user.getAccess_token() != null && !user.getAccess_token().isEmpty()) {
            binding.setProfileData(user);
            binding.cpNationality.setCountryForNameCode(String.valueOf(user.getNationality()));
        }
    }
}
