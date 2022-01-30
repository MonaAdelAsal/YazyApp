package com.asc.yazy.fragment;


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
import com.asc.yazy.api.model.ModelBooking;
import com.asc.yazy.api.model.ModelStatic;
import com.asc.yazy.core.CancelDialog;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.core.GlobalInfoDialog;
import com.asc.yazy.databinding.FragmentCancellationPolicyBinding;
import com.asc.yazy.interfaces.IFragment;
import com.asc.yazy.interfaces.OnCancelCompleted;
import com.asc.yazy.utils.AnalyticsUtility;
import com.asc.yazy.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CancellationPolicyFragment extends Fragment implements View.OnClickListener, IFragment {


    private long mLastClickTime = System.currentTimeMillis();
    private FragmentCancellationPolicyBinding binding;
    private ModelBooking modelBooking;
    private OnCancelCompleted tripsOnCancelCompleted;

    private final OnCancelCompleted onCancelCompleted = new OnCancelCompleted() {
        @Override
        public void nnCancelCompleted() {
            if (tripsOnCancelCompleted != null)
                tripsOnCancelCompleted.nnCancelCompleted();
            if (getActivity() != null)
                getActivity().getSupportFragmentManager().popBackStack();

        }
    };

    public CancellationPolicyFragment() {
    }

    public CancellationPolicyFragment(ModelBooking modelBooking, OnCancelCompleted onCancelCompleted) {
        this.modelBooking = modelBooking;
        this.tripsOnCancelCompleted = onCancelCompleted;

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_cancellation_policy, null, false);
        AnalyticsUtility.setScreen(this);
        AnalyticsUtility.logEventOpen(AnalyticsUtility.Events.ABOUT);
        binding.imgBack.setOnClickListener(this);
        binding.loader.startShimmerAnimation();
        CheckLanguage checkLanguage = new CheckLanguage(getActivity());
        checkLanguage.UpdateLanguage();
        binding.btnCancel.setOnClickListener(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadDate();
    }

    private void loadDate() {

        ApiInterface mApiService = ApiClient.getInterfaceService();
        Call<ModelStatic> call = mApiService.cancellationPolicy(Constants.getLANGUAGE());
        call.enqueue(new Callback<ModelStatic>() {
            @Override
            public void onResponse(@NonNull Call<ModelStatic> call, @NonNull Response<ModelStatic> response) {

                if (getActivity() == null)
                    return;
                if (CancellationPolicyFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;
                binding.loader.stopShimmerAnimation();

                if (response.code() != 200) {
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String message = jObjError.getString("message");
                        GlobalInfoDialog.getInstance(CancellationPolicyFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(message).show();
                        return;
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                ModelStatic body = response.body();

                if (body == null) {
                    GlobalInfoDialog.getInstance(CancellationPolicyFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(getString(R.string.please_check_your_internet_connection)).show();
                    return;
                }
                if (body.getStatus() == 200) {
                    AnalyticsUtility.logEventAPISuccess(AnalyticsUtility.Events.ABOUT);
                    binding.tvDate.setBackground(null);
                    binding.tvDate.setText(body.getData().getPageContent());
                } else {
                    GlobalInfoDialog.getInstance(CancellationPolicyFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(body.getMessage()).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<ModelStatic> call, @NonNull Throwable t) {

                if (CancellationPolicyFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;
                AnalyticsUtility.logEventAPIFail(AnalyticsUtility.Events.ABOUT);
                // GlobalInfoDialog.getInstance(CancellationPolicyFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(CancellationPolicyFragment.this.getContext().getResources().getString(R.string.cant_connect)).show();
                requireActivity().startActivity(new Intent(getActivity(), NoNetActivity.class));

            }
        });
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
        if (v.getId() == R.id.btnCancel) {
            CancelDialog.getInstance(CancellationPolicyFragment.this.getContext(), modelBooking.getBooking_id(), onCancelCompleted).show();
        }
    }

    @Override
    public void onBack() {
        if (getActivity() != null)
            getActivity().getSupportFragmentManager().popBackStack();
    }

}
