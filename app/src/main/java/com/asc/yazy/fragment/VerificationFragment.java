package com.asc.yazy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.asc.yazy.R;
import com.asc.yazy.activity.AuthenticationActivity;
import com.asc.yazy.api.ApiClient;
import com.asc.yazy.api.ApiInterface;
import com.asc.yazy.api.model.ModelVerify;
import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.core.GlobalInfoDialog;
import com.asc.yazy.core.PlaneDialog;
import com.asc.yazy.databinding.VerficationFragmentBinding;
import com.asc.yazy.utils.AnalyticsUtility;
import com.asc.yazy.utils.Constants;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerificationFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "VerificationFragment";
    private VerficationFragmentBinding binding;

    public VerificationFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.verfication_fragment, null, false);
        AnalyticsUtility.logEventOpen(AnalyticsUtility.Events.VERIFICATION);
        AnalyticsUtility.setScreen(this);
        CheckLanguage checkLanguage = new CheckLanguage(getActivity());
        checkLanguage.UpdateLanguage();
        binding.btnGetCode.setOnClickListener(this);
        binding.countryCode.registerCarrierNumberEditText(binding.etMobile);
        binding.countryCode.setExcludedCountries("IL");
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
    public void onClick(View v) {
        if (v == null)
            return;

        if (v.getId() == R.id.btnGetCode) {
            AnalyticsUtility.logAction(AnalyticsUtility.Actions.GET_PEN_CODE);
            verify();
        }
    }

    private void authenticatePhoneNumber() {

        if (getActivity() == null)
            return;

        if (binding.countryCode.getSelectedCountryCodeWithPlus() == null) {
            binding.etMobile.setError(Objects.requireNonNull(VerificationFragment.this.getContext()).getResources().getString(R.string.mobileNumberNotValid));
            return;
        }

        if (!binding.countryCode.isValidFullNumber()) {
            binding.etMobile.setError(Objects.requireNonNull(VerificationFragment.this.getContext()).getResources().getString(R.string.mobileNumberNotValid));
            return;
        }

        if (binding.etMobile.getText() == null || binding.etMobile.getText().toString().trim().isEmpty()) {
            binding.etMobile.setError(Objects.requireNonNull(VerificationFragment.this.getContext()).getResources().getString(R.string.mobile_requ));
            return;
        }

        PlaneDialog planeDialog = new PlaneDialog(Objects.requireNonNull(getActivity()));

        String phoneNumber = binding.countryCode.getSelectedCountryCodeWithPlus() + Constants.getNormalizedPhoneNumber(binding.countryCode.getFullNumber(), binding.countryCode.getSelectedCountryCode());
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.setLanguageCode(UtilsPreference.getInstance(getActivity()).getPreference(Constants.LANGUAGE, "ar"));

        //noinspection deprecation
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                        Log.d(TAG, "onVerificationCompleted: ");

                        if (getActivity() == null)
                            return;
                        if (!isAdded())
                            return;
                        planeDialog.Dismiss();

                        AnalyticsUtility.logEvent(AnalyticsUtility.Events.EVENT_VERIFICATION_SUCCESS, AnalyticsUtility.Events.EVENT_VERIFICATION_SUCCESS);
                        getActivity()
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                                .add(R.id.container, new RegisterFragment(binding.countryCode.getSelectedCountryCodeWithPlus(), Constants.getNormalizedPhoneNumber(binding.countryCode.getFullNumber(), binding.countryCode.getSelectedCountryCode())))
                                .addToBackStack("RegisterFragment")
                                .commitAllowingStateLoss();


                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {

                        Log.d(TAG, "onVerificationFailed: " + e.getLocalizedMessage());

                        if (getActivity() == null)
                            return;
                        if (!isAdded())
                            return;
                        planeDialog.Dismiss();

                        AnalyticsUtility.logEvent(AnalyticsUtility.Events.EVENT_VERIFICATION_FAIL, AnalyticsUtility.Events.EVENT_VERIFICATION_FAIL);
                        GlobalInfoDialog.getInstance(getActivity()).setTitle(getString(R.string.error)).setMessage(e.getLocalizedMessage()).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                        Log.d(TAG, "onCodeSent: " + verificationId);

                        if (getActivity() == null)
                            return;
                        if (!isAdded())
                            return;
                        planeDialog.Dismiss();

                        AnalyticsUtility.logEvent(AnalyticsUtility.Events.EVENT_VERIFICATION_ON_CODE, AnalyticsUtility.Events.EVENT_VERIFICATION_ON_CODE);
                        getActivity()
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                                .add(R.id.container, new ConfirmCodeFragment(false, binding.countryCode.getSelectedCountryCodeWithPlus(), Constants.getNormalizedPhoneNumber(binding.countryCode.getFullNumber(), binding.countryCode.getSelectedCountryCode()), verificationId))
                                .addToBackStack("ConfirmCodeFragment")
                                .commit();

                    }
                });

    }


    private void verify() {


        if (binding.countryCode.getSelectedCountryCode() == null) {
            binding.etMobile.setError(Objects.requireNonNull(VerificationFragment.this.getContext()).getResources().getString(R.string.mobileNumberNotValid));
            return;
        }

        if (!binding.countryCode.isValidFullNumber()) {
            binding.etMobile.setError(Objects.requireNonNull(VerificationFragment.this.getContext()).getResources().getString(R.string.mobileNumberNotValid));
            return;
        }

        if (!binding.countryCode.isSearchAllowed()) {
            binding.etMobile.setError(Objects.requireNonNull(VerificationFragment.this.getContext()).getResources().getString(R.string.mobileNumberNotValid));
            return;
        }

        if (binding.etMobile.getText() == null || binding.etMobile.getText().toString().trim().isEmpty()) {
            binding.etMobile.setError(Objects.requireNonNull(VerificationFragment.this.getContext()).getResources().getString(R.string.mobile_requ));
            return;
        }


        PlaneDialog planeDialog = new PlaneDialog(Objects.requireNonNull(getActivity()));
        ApiInterface mApiService = ApiClient.getInterfaceService();
        Call<ModelVerify> call = mApiService.verify(Constants.getLANGUAGE(), binding.countryCode.getSelectedCountryCodeWithPlus(), Constants.getNormalizedPhoneNumber(binding.countryCode.getFullNumber(), binding.countryCode.getSelectedCountryCode()));
        call.enqueue(new Callback<ModelVerify>() {
            @Override
            public void onResponse(@NonNull Call<ModelVerify> call, @NonNull Response<ModelVerify> response) {

                if (getActivity() == null)
                    return;
                if (!isAdded())
                    return;
                if (VerificationFragment.this.getContext() == null)
                    return;

                planeDialog.Dismiss();


                if (response.code() == 401) {
                    getActivity().startActivity(new Intent(getActivity(), AuthenticationActivity.class));
                    return;
                }

                if (response.code() != 200) {
                    assert response.errorBody() != null;
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("message");
                        GlobalInfoDialog.getInstance(getActivity()).setTitle(getString(R.string.error)).setMessage(message).show();
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                ModelVerify body = response.body();

                if (body == null) {
                    AnalyticsUtility.logEventAPISuccess(AnalyticsUtility.Events.VERIFICATION);
                    GlobalInfoDialog.getInstance(VerificationFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(getString(R.string.please_check_your_internet_connection)).show();
                    return;
                }

                if (body.getStatus() == 200) {
                    authenticatePhoneNumber();
                    /*
                    getActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                            .add(R.id.container, new RegisterFragment(binding.countryCode.getSelectedCountryCodeWithPlus(), Constants.getNormalizedPhoneNumber(binding.countryCode.getFullNumber(), binding.countryCode.getSelectedCountryCode())))
                            .addToBackStack("RegisterFragment")
                            .commitAllowingStateLoss();

                     */

                } else {
                    GlobalInfoDialog.getInstance(VerificationFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(body.getMessage()).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<ModelVerify> call, @NonNull Throwable t) {

                if (getActivity() == null)
                    return;
                if (VerificationFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;
                planeDialog.Dismiss();
                AnalyticsUtility.logEventAPIFail(AnalyticsUtility.Events.VERIFICATION);
                GlobalInfoDialog.getInstance(VerificationFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(t.getMessage()).show();


            }
        });

    }

}
