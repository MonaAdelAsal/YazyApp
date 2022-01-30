package com.asc.yazy.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.asc.yazy.R;
import com.asc.yazy.activity.AuthenticationActivity;
import com.asc.yazy.activity.NoNetActivity;
import com.asc.yazy.api.ApiClient;
import com.asc.yazy.api.ApiInterface;
import com.asc.yazy.api.model.ModelStatic;
import com.asc.yazy.api.model.ModelUser;
import com.asc.yazy.api.pagination.offers.NetworkState;
import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.core.GlobalInfoDialog;
import com.asc.yazy.core.PlaneDialog;
import com.asc.yazy.databinding.FragmentResetPasswordBinding;
import com.asc.yazy.utils.AnalyticsUtility;
import com.asc.yazy.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ResetPasswordFragment extends Fragment implements View.OnClickListener {

    private FragmentResetPasswordBinding binding;
    private String countryCode;
    private String mobile;

    public ResetPasswordFragment() {
    }

    ResetPasswordFragment(String countryCode, String mobile) {
        this.countryCode = countryCode;
        this.mobile = mobile;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_reset_password, null, false);
        AnalyticsUtility.logEvent(AnalyticsUtility.Events.NEW_PASSWORD, AnalyticsUtility.Events.NEW_PASSWORD);
        AnalyticsUtility.setScreen(this);
        CheckLanguage checkLanguage = new CheckLanguage(getActivity());
        checkLanguage.UpdateLanguage();
        binding.btnChangePass.setOnClickListener(this);
        binding.imgShowCurrent.setOnClickListener(this);
        binding.imgShowRepeat.setOnClickListener(this);

        binding.etCreatePassword.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    binding.imgShowCurrent.setVisibility(View.VISIBLE);
                } else {
                    binding.imgShowCurrent.setVisibility(View.INVISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        binding.etRepeatPassword.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    binding.imgShowRepeat.setVisibility(View.VISIBLE);
                } else {
                    binding.imgShowRepeat.setVisibility(View.INVISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        return binding.getRoot();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (v == null)
            return;
        switch (v.getId()) {
            case R.id.btnChangePass:
                changePassWord();
                break;
            case R.id.imgShowCurrent:
                //shown
                if (binding.imgShowCurrent.getTag().equals("shown")) {
                    binding.etCreatePassword.setTransformationMethod(null);
                    binding.imgShowCurrent.setImageResource(R.drawable.ic_hide);
                    binding.imgShowCurrent.setTag("notshown");
                } else {
                    binding.etCreatePassword.setTransformationMethod(new PasswordTransformationMethod());
                    binding.imgShowCurrent.setImageResource(R.drawable.ic_show);
                    binding.imgShowCurrent.setTag("shown");
                }

                break;
            case R.id.imgShowRepeat:
                if (binding.imgShowRepeat.getTag().equals("shown")) {
                    binding.etRepeatPassword.setTransformationMethod(null);
                    binding.imgShowRepeat.setImageResource(R.drawable.ic_hide);
                    binding.imgShowRepeat.setTag("notshown");
                } else {
                    binding.etRepeatPassword.setTransformationMethod(new PasswordTransformationMethod());
                    binding.imgShowRepeat.setImageResource(R.drawable.ic_show);
                    binding.imgShowRepeat.setTag("shown");
                }

                break;
        }
    }

    private void changePassWord() {

        if (binding.etCreatePassword.getText() == null || binding.etCreatePassword.getText().toString().isEmpty()) {
            binding.etCreatePassword.setError(Objects.requireNonNull(ResetPasswordFragment.this.getContext()).getResources().getString(R.string.password_requ));
            return;
        }

        if (binding.etRepeatPassword.getText() == null || binding.etRepeatPassword.getText().toString().isEmpty()) {
            binding.etRepeatPassword.setError(Objects.requireNonNull(ResetPasswordFragment.this.getContext()).getResources().getString(R.string.password_requ));
            return;
        }

        if (!binding.etCreatePassword.getText().toString().equals(binding.etRepeatPassword.getText().toString())) {
            binding.etRepeatPassword.setError(Objects.requireNonNull(ResetPasswordFragment.this.getContext()).getResources().getString(R.string.passwords_not_match));
            binding.etCreatePassword.setError(Objects.requireNonNull(ResetPasswordFragment.this.getContext()).getResources().getString(R.string.passwords_not_match));
            return;
        }

        PlaneDialog planeDialog = new PlaneDialog(Objects.requireNonNull(getActivity()));

        ApiInterface mApiService = ApiClient.getInterfaceService();
        Call<ModelStatic> call = mApiService.change(Constants.getLANGUAGE(), countryCode, mobile, binding.etCreatePassword.getText().toString());
        call.enqueue(new Callback<ModelStatic>() {
            @Override
            public void onResponse(@NonNull Call<ModelStatic> call, @NonNull Response<ModelStatic> response) {

                if (getActivity() == null)
                    return;
                if (ResetPasswordFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;
                planeDialog.Dismiss();
                if (response.code() == 401) {
                    Intent intent = new Intent(getActivity(), AuthenticationActivity.class);
                    intent.putExtra(Constants.TRANSACTION, NetworkState.UN_AUTHORISE);
                    getActivity().startActivity(intent);
                    return;
                }

                if (response.code() != 200) {
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String message = jObjError.getString("message");
                        GlobalInfoDialog.getInstance(ResetPasswordFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(message).show();
                        return;
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                ModelStatic body = response.body();

                if (body == null) {
                    GlobalInfoDialog.getInstance(ResetPasswordFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(getString(R.string.please_check_your_internet_connection)).show();
                    return;
                }

                if (body.getStatus() == 200) {
                    ModelUser user = UtilsPreference.getInstance(ResetPasswordFragment.this.requireActivity()).getUser();
                    if (user.isRememberMe())
                        user.setPassword(binding.etCreatePassword.getText().toString());
                    else
                        user.setPassword(null);

                    getActivity().startActivity(new Intent(getActivity(), AuthenticationActivity.class));
                    getActivity().finish();
                } else {
                    GlobalInfoDialog.getInstance(ResetPasswordFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(body.getMessage()).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<ModelStatic> call, @NonNull Throwable t) {

                if (ResetPasswordFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;

                planeDialog.Dismiss();
                //  GlobalInfoDialog.getInstance(ResetPasswordFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(ResetPasswordFragment.this.getContext().getResources().getString(R.string.cant_connect)).show();
                requireActivity().startActivity(new Intent(getActivity(), NoNetActivity.class));


            }
        });
    }
}
