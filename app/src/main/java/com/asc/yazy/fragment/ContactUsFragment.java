package com.asc.yazy.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.asc.yazy.R;
import com.asc.yazy.activity.NoNetActivity;
import com.asc.yazy.api.ApiClient;
import com.asc.yazy.api.ApiInterface;
import com.asc.yazy.api.model.ModelContactApi;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.core.GlobalInfoDialog;
import com.asc.yazy.databinding.FragmentContactUsBinding;
import com.asc.yazy.interfaces.IFragment;
import com.asc.yazy.utils.AnalyticsUtility;
import com.asc.yazy.utils.Constants;
import com.asc.yazy.utils.UtilsShare;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUsFragment extends Fragment implements View.OnClickListener, IFragment {

    private long mLastClickTime = System.currentTimeMillis();
    private FragmentContactUsBinding binding;
    public ContactUsFragment() {
    }

    @SuppressLint("FragmentLiveDataObserve")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contact_us, container, false);
        binding.layoutEmail.setOnClickListener(this);
        binding.layoutLocation.setOnClickListener(this);
        binding.layoutPhone.setOnClickListener(this);
        binding.imgBack.setOnClickListener(this);
        AnalyticsUtility.logEventOpen(AnalyticsUtility.Events.CONTACT_US);
        AnalyticsUtility.setScreen(this);
        CheckLanguage checkLanguage = new CheckLanguage(getActivity());
        checkLanguage.UpdateLanguage();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.slEmail.startShimmerAnimation();
        binding.slIntro.startShimmerAnimation();
        binding.slLocation.startShimmerAnimation();
        binding.slMobile.startShimmerAnimation();
        loadDate();
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
            case R.id.layoutLocation:
                //  Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.co.in/maps?q=" + "Cairo"));
                //    startActivity(intent);
                break;
            case R.id.layoutEmail:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.EMAIL_TRAVEL_AGENCY);
                UtilsShare.sendEmail(getActivity(), getResources().getString(R.string.app_name), binding.tvMail.getText().toString());
                break;
            case R.id.layoutPhone:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.DIAL_TRAVEL_AGENCY);
                UtilsShare.dial(getActivity(), binding.tvMobile.getText().toString());
                break;
        }
    }

    @Override
    public void onBack() {
        if (getActivity() != null)
            getActivity().getSupportFragmentManager().popBackStack();
    }

    private void loadDate() {

        ApiInterface mApiService = ApiClient.getInterfaceService();
        Call<ModelContactApi> call = mApiService.contactUs(Constants.getLANGUAGE());
        call.enqueue(new Callback<ModelContactApi>() {
            @Override
            public void onResponse(@NonNull Call<ModelContactApi> call, @NonNull Response<ModelContactApi> response) {

                if (getActivity() == null)
                    return;
                if (ContactUsFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;
                if (response.code() != 200) {
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String message = jObjError.getString("message");
                        GlobalInfoDialog.getInstance(ContactUsFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(message).show();
                        return;
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                ModelContactApi body = response.body();

                if (body == null) {
                    GlobalInfoDialog.getInstance(ContactUsFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(getString(R.string.please_check_your_internet_connection)).show();
                    return;
                }
                if (body.getStatus() == 200) {
                    AnalyticsUtility.logEventAPISuccess(AnalyticsUtility.Events.CONTACT_US);
                    binding.slEmail.stopShimmerAnimation();
                    binding.slIntro.stopShimmerAnimation();
                    binding.slLocation.stopShimmerAnimation();
                    binding.slMobile.stopShimmerAnimation();
                    binding.tvIntroduction.setBackground(null);
                    binding.tvMobile.setBackground(null);
                    binding.tvMail.setBackground(null);
                    binding.tvAddress.setBackground(null);
                    binding.setContactData(body.getData());
                } else {
                    GlobalInfoDialog.getInstance(ContactUsFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(body.getMessage()).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ModelContactApi> call, @NonNull Throwable t) {

                if (ContactUsFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;
                AnalyticsUtility.logEventAPIFail(AnalyticsUtility.Events.CONTACT_US);
                // GlobalInfoDialog.getInstance(ContactUsFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(ContactUsFragment.this.getContext().getResources().getString(R.string.cant_connect)).show();
                requireActivity().startActivity(new Intent(getActivity(), NoNetActivity.class));


            }
        });
    }

}

