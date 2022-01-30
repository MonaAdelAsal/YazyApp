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
import com.asc.yazy.api.model.ModelStatic;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.core.GlobalInfoDialog;
import com.asc.yazy.databinding.FragmentTermsBinding;
import com.asc.yazy.interfaces.IFragment;
import com.asc.yazy.utils.AnalyticsUtility;
import com.asc.yazy.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TermsFragment extends Fragment implements View.OnClickListener, IFragment {


    private long mLastClickTime = System.currentTimeMillis();
    private FragmentTermsBinding binding;

    public TermsFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_terms, null, false);
        binding.imgBack.setOnClickListener(this);
        //binding.loader.startShimmerAnimation();
        AnalyticsUtility.logEventOpen(AnalyticsUtility.Events.TERMS_AND_CONDITIONS);
        AnalyticsUtility.setScreen(this);
        CheckLanguage checkLanguage = new CheckLanguage(getActivity());
        checkLanguage.UpdateLanguage();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

        ApiInterface mApiService = ApiClient.getInterfaceService();
        Call<ModelStatic> call = mApiService.termsAndConditions(Constants.getLANGUAGE());
        call.enqueue(new Callback<ModelStatic>() {
            @Override
            public void onResponse(@NonNull Call<ModelStatic> call, @NonNull Response<ModelStatic> response) {

                if (getActivity() == null)
                    return;
                if (TermsFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;


                if (response.code() != 200) {
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String message = jObjError.getString("message");
                        GlobalInfoDialog.getInstance(TermsFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(message).show();
                        return;
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                ModelStatic body = response.body();

                if (body == null) {
                    GlobalInfoDialog.getInstance(TermsFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(getString(R.string.please_check_your_internet_connection)).show();
                    return;
                }

                if (body.getStatus() == 200) {
                    AnalyticsUtility.logEventAPISuccess(AnalyticsUtility.Events.TERMS_AND_CONDITIONS);
                    binding.loader.stopShimmerAnimation();
                    binding.wvTerms.setBackground(null);
                    binding.title.setText(body.getData().getPageName());
                    binding.wvTerms.loadDataWithBaseURL("", body.getData().getPageContent(), "text/html", "UTF-8", "");
                } else {
                    GlobalInfoDialog.getInstance(TermsFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(body.getMessage()).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ModelStatic> call, @NonNull Throwable t) {

                if (TermsFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;
                AnalyticsUtility.logEventAPIFail(AnalyticsUtility.Events.TERMS_AND_CONDITIONS);
                // GlobalInfoDialog.getInstance(TermsFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(TermsFragment.this.getContext().getResources().getString(R.string.cant_connect)).show();
                requireActivity().startActivity(new Intent(getActivity(), NoNetActivity.class));

            }
        });
    }


}
