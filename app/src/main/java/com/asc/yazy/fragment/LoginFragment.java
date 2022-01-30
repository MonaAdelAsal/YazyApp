package com.asc.yazy.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.asc.yazy.R;
import com.asc.yazy.activity.NoNetActivity;
import com.asc.yazy.activity.SplashActivity;
import com.asc.yazy.api.ApiClient;
import com.asc.yazy.api.ApiInterface;
import com.asc.yazy.api.model.ModelAuthenticate;
import com.asc.yazy.api.model.ModelUser;
import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.core.GlobalInfoDialog;
import com.asc.yazy.core.PlaneDialog;
import com.asc.yazy.databinding.LoginFragmentBinding;
import com.asc.yazy.utils.AnalyticsUtility;
import com.asc.yazy.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private LoginFragmentBinding binding;


    public LoginFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.login_fragment, null, false);
        AnalyticsUtility.logEventOpen(AnalyticsUtility.Events.LOGIN);
        AnalyticsUtility.setScreen(this);
        CheckLanguage checkLanguage = new CheckLanguage(getActivity());
        checkLanguage.UpdateLanguage();
        binding.llSkip.setOnClickListener(this);
        binding.llRegister.setOnClickListener(this);
        binding.tvForgetPass.setOnClickListener(this);
        binding.tvForgetPass.setPaintFlags(binding.tvForgetPass.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        binding.tvRegister.setPaintFlags(binding.tvRegister.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        binding.show.setOnClickListener(this);
        binding.btnLogin.setOnClickListener(this);
        binding.countryCode.setExcludedCountries("IL");
        binding.etPassword.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    binding.show.setVisibility(View.VISIBLE);
                } else {
                    binding.show.setVisibility(View.INVISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }
        });
        binding.countryCode.registerCarrierNumberEditText(binding.etMobile);
        binding.etMobile.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    if (binding.countryCode.isValidFullNumber()) {
                        boolean isEnglish = getResources().getBoolean(R.bool.english_lan);
                        if (isEnglish)
                            binding.etMobile.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_correct, 0);
                        else
                            binding.etMobile.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_correct, 0, 0, 0);
                    } else {
                        boolean isEnglish = getResources().getBoolean(R.bool.english_lan);
                        if (isEnglish)
                            binding.etMobile.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error, 0);
                        else
                            binding.etMobile.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_error, 0, 0, 0);
                    }
                }
            }
        });
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindDate();
    }

    private void bindDate() {

        if (getActivity() == null)
            return;

        ModelUser user = UtilsPreference.getInstance(getActivity()).getUser();
        if (user == null || !user.isRememberMe())
            return;
        binding.etMobile.setText(user.getMobile());
        binding.etPassword.setText(user.getPassword());
        binding.rememberMe.setChecked(user.isRememberMe());
        AnalyticsUtility.logEventLoadDate(AnalyticsUtility.Events.LOGIN);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (v == null)
            return;

        switch (v.getId()) {

            case R.id.btnLogin:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.LOGIN);
                login();
                break;

            case R.id.llSkip:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.SKIP);
                binding.etMobile.setError(null);
                if (getActivity() == null)
                    return;
                /*
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                */
                getActivity().finish();
                break;

            case R.id.llRegister:
                binding.etMobile.setError(null);
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_REGISTER);
                if (getActivity() == null)
                    return;

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                        .add(R.id.container, new VerificationFragment())
                        .addToBackStack("VerificationFragment")
                        .commit();
                break;

            case R.id.tvForgetPass:
                binding.etMobile.setError(null);
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_FORGET_PASSWORD);
                if (getActivity() == null)
                    return;

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                        .add(R.id.container, new ForgetPassFragment())
                        .addToBackStack("ForgetPassFragment")
                        .commit();
                break;
            case R.id.show:
                binding.etMobile.setError(null);
                if (binding.show.getTag().equals("shown")) {
                    binding.etPassword.setTransformationMethod(null);
                    binding.show.setImageResource(R.drawable.ic_hide);
                    binding.show.setTag("notshown");
                } else {
                    binding.etPassword.setTransformationMethod(new PasswordTransformationMethod());
                    binding.show.setImageResource(R.drawable.ic_show);
                    binding.show.setTag("shown");
                }/*
                if (isCurrentNormal) {
                    binding.etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                    isCurrentNormal = !isCurrentNormal;
                    binding.show.setImageDrawable(getResources().getDrawable(R.drawable.hide_pw));
                } else {
                    binding.etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    isCurrentNormal = !isCurrentNormal;
                    binding.show.setImageDrawable(getResources().getDrawable(R.drawable.show_pw_1));
                }*/
                break;
        }
    }

    private void login() {

        if (binding.countryCode.getSelectedCountryCode() == null) {
            binding.etMobile.setError(Objects.requireNonNull(LoginFragment.this.getContext()).getResources().getString(R.string.mobileNumberNotValid));
            return;
        }

        if (!binding.countryCode.isValidFullNumber()) {
            binding.etMobile.setError(Objects.requireNonNull(LoginFragment.this.getContext()).getResources().getString(R.string.mobileNumberNotValid));
            return;
        }

        if (!binding.countryCode.isSearchAllowed()) {
            binding.etMobile.setError(Objects.requireNonNull(LoginFragment.this.getContext()).getResources().getString(R.string.mobileNumberNotValid));
            return;
        }

        if (binding.etMobile.getText() == null || binding.etMobile.getText().toString().trim().isEmpty()) {
            binding.etMobile.setError(Objects.requireNonNull(LoginFragment.this.getContext()).getResources().getString(R.string.mobile_requ));
            return;
        }
        if (binding.etPassword.getText() == null || binding.etPassword.getText().toString().trim().isEmpty()) {
            binding.etPassword.setError(Objects.requireNonNull(LoginFragment.this.getContext()).getResources().getString(R.string.password_requ));
            return;
        }


        PlaneDialog planeDialog = new PlaneDialog(Objects.requireNonNull(getActivity()));

        ApiInterface mApiService = ApiClient.getInterfaceService();
        Call<ModelAuthenticate> call = mApiService.login(Constants.getLANGUAGE(), binding.countryCode.getSelectedCountryCodeWithPlus(), Constants.getNormalizedPhoneNumber(binding.countryCode.getFullNumber(), binding.countryCode.getSelectedCountryCode()), binding.etPassword.getText().toString());
        call.enqueue(new Callback<ModelAuthenticate>() {
            @Override
            public void onResponse(@NonNull Call<ModelAuthenticate> call, @NonNull Response<ModelAuthenticate> response) {

                if (getActivity() == null)
                    return;
                if (LoginFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;
                planeDialog.Dismiss();


                if (response.code() != 200) {
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String message = jObjError.getString("message");
                        GlobalInfoDialog.getInstance(LoginFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(message).show();
                        return;
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                ModelAuthenticate body = response.body();

                if (body == null) {
                    GlobalInfoDialog.getInstance(LoginFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(getString(R.string.please_check_your_internet_connection)).show();
                    return;
                }

                if (body.getStatus() == 200 && body.getData() != null && body.getData() != null) {
                    AnalyticsUtility.logEventAPISuccess(AnalyticsUtility.Events.LOGIN);
                    ModelUser user = body.getData();
                    user.setRememberMe(binding.rememberMe.isChecked());
                    if (binding.rememberMe.isChecked()) {
                        user.setPassword(binding.etPassword.getText().toString());
                    }
                    UtilsPreference.getInstance(getActivity()).saveUser(user);
                    getActivity().startActivity(new Intent(getActivity(), SplashActivity.class));
                    getActivity().finish();
                } else {
                    GlobalInfoDialog.getInstance(LoginFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(body.getMessage()).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<ModelAuthenticate> call, @NonNull Throwable t) {

                if (LoginFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;
                planeDialog.Dismiss();
                AnalyticsUtility.logEventAPIFail(AnalyticsUtility.Events.LOGIN);
                // GlobalInfoDialog.getInstance(LoginFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(LoginFragment.this.getContext().getResources().getString(R.string.cant_connect)).show();
                requireActivity().startActivity(new Intent(getActivity(), NoNetActivity.class));


            }
        });
    }

}
