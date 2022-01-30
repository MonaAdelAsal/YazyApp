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
import com.asc.yazy.activity.MainActivity;
import com.asc.yazy.activity.NoNetActivity;
import com.asc.yazy.api.ApiClient;
import com.asc.yazy.api.ApiInterface;
import com.asc.yazy.api.model.ModelAuthenticate;
import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.core.GlobalInfoDialog;
import com.asc.yazy.core.PlaneDialog;
import com.asc.yazy.databinding.FragmentRegisterBinding;
import com.asc.yazy.utils.AnalyticsUtility;
import com.asc.yazy.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterFragment extends Fragment implements View.OnClickListener {

    private FragmentRegisterBinding binding;
    private String countryCode;
    private String mobile;

    public RegisterFragment() {

    }

    public RegisterFragment(String countryCode, String mobile) {
        this.countryCode = countryCode;
        this.mobile = mobile;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_register, null, false);
        AnalyticsUtility.logEventOpen(AnalyticsUtility.Events.REGISTER);
        AnalyticsUtility.setScreen(this);
        CheckLanguage checkLanguage = new CheckLanguage(getActivity());
        checkLanguage.UpdateLanguage();
        binding.btnRegister.setOnClickListener(this);
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
            case R.id.btnRegister:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_REGISTER);
                register();
                break;
            case R.id.imgShowCurrent:
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

    private void register() {

        if (binding.etUsername.getText() == null || binding.etUsername.getText().toString().trim().isEmpty() || !binding.etUsername.getText().toString().contains(" ")) {
            binding.etUsername.setError(Objects.requireNonNull(RegisterFragment.this.getContext()).getResources().getString(R.string.user_name_requ));
            return;
        }


        if (binding.etCreatePassword.getText() == null || binding.etCreatePassword.getText().toString().trim().isEmpty()) {
            binding.etCreatePassword.setError(Objects.requireNonNull(RegisterFragment.this.getContext()).getResources().getString(R.string.password_requ));
            return;
        }

        if (binding.etCreatePassword.getText() == null || binding.etCreatePassword.getText().toString().trim().isEmpty() || !Constants.isValidPassword(binding.etCreatePassword.getText().toString().trim())) {
            binding.etCreatePassword.setError(Objects.requireNonNull(RegisterFragment.this.getContext()).getResources().getString(R.string.valid_password));
            return;
        }


        if (binding.etRepeatPassword.getText() == null || binding.etRepeatPassword.getText().toString().trim().isEmpty()) {
            binding.etRepeatPassword.setError(Objects.requireNonNull(RegisterFragment.this.getContext()).getResources().getString(R.string.password_requ));
            return;
        }

        if (binding.etRepeatPassword.getText() == null || binding.etRepeatPassword.getText().toString().trim().isEmpty() || !Constants.isValidPassword(binding.etRepeatPassword.getText().toString().trim())) {
            binding.etRepeatPassword.setError(Objects.requireNonNull(RegisterFragment.this.getContext()).getResources().getString(R.string.valid_password));
            return;
        }


        if (!binding.etCreatePassword.getText().toString().equals(binding.etRepeatPassword.getText().toString())) {
            binding.etRepeatPassword.setError(Objects.requireNonNull(RegisterFragment.this.getContext()).getResources().getString(R.string.passwords_not_match));
            binding.etCreatePassword.setError(Objects.requireNonNull(RegisterFragment.this.getContext()).getResources().getString(R.string.passwords_not_match));
            return;
        }

        PlaneDialog planeDialog = new PlaneDialog(Objects.requireNonNull(getActivity()));

        ApiInterface mApiService = ApiClient.getInterfaceService();
        Call<ModelAuthenticate> call = mApiService.register(Constants.getLANGUAGE(), binding.etUsername.getText().toString(), countryCode, mobile, binding.etCreatePassword.getText().toString(), binding.etEmail.getText().toString());
        call.enqueue(new Callback<ModelAuthenticate>() {
            @Override
            public void onResponse(@NonNull Call<ModelAuthenticate> call, @NonNull Response<ModelAuthenticate> response) {

                if (getActivity() == null)
                    return;
                if (RegisterFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;
                planeDialog.Dismiss();

                if (response.code() == 401) {
                    getActivity().startActivity(new Intent(getActivity(), AuthenticationActivity.class));
                    return;
                }

                if (response.code() != 200) {
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String message = jObjError.getString("message");
                        GlobalInfoDialog.getInstance(RegisterFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(message).show();
                        return;
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                ModelAuthenticate body = response.body();

                if (body == null) {
                    GlobalInfoDialog.getInstance(RegisterFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(getString(R.string.please_check_your_internet_connection)).show();
                    return;
                }

                if (body.getStatus() == 200 && body.getData() != null && body.getData() != null) {
                    AnalyticsUtility.logEventAPISuccess(AnalyticsUtility.Events.REGISTER);
                    UtilsPreference.getInstance(getActivity()).savePreference(Constants.REGISTRATION_POINTS, body.getData().getPoints());
                    UtilsPreference.getInstance(getActivity()).saveUser(body.getData());
                    UtilsPreference.getInstance(getActivity()).UpdateStatue("true");
                    getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
                    getActivity().finish();
                } else {
                    GlobalInfoDialog.getInstance(RegisterFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(body.getMessage()).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<ModelAuthenticate> call, @NonNull Throwable t) {

                if (RegisterFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;

                planeDialog.Dismiss();
                AnalyticsUtility.logEventAPIFail(AnalyticsUtility.Events.REGISTER);
                //GlobalInfoDialog.getInstance(RegisterFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(RegisterFragment.this.getContext().getResources().getString(R.string.cant_connect)).show();
                requireActivity().startActivity(new Intent(getActivity(), NoNetActivity.class));

            }
        });
    }
}
