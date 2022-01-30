package com.asc.yazy.fragment;

import static com.google.firebase.auth.FirebaseAuth.getInstance;

import android.annotation.SuppressLint;
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
import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.core.GlobalInfoDialog;
import com.asc.yazy.databinding.FragmentConfirmCodeBinding;
import com.asc.yazy.utils.AnalyticsUtility;
import com.asc.yazy.utils.Constants;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;


public class ConfirmCodeFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "ConfirmCodeFragment";
    private FragmentConfirmCodeBinding binding;
    private final TextWatcher fiveWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (binding.etVerifyCodeFive.getText().toString().length() == 1) {
                binding.etVerifyCodeFive.clearFocus();
                binding.etVerifyCodeSix.requestFocus();
            }
        }
    };
    private final TextWatcher forthWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (binding.etVerifyCodeForth.getText().toString().length() == 1) {
                binding.etVerifyCodeForth.clearFocus();
                binding.etVerifyCodeFive.requestFocus();
            }
        }
    };
    private final TextWatcher thirdWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (binding.etVerifyCodeThird.getText().toString().length() == 1) {
                binding.etVerifyCodeThird.clearFocus();
                binding.etVerifyCodeForth.requestFocus();
            }
        }
    };
    private final TextWatcher secondWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {


        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (binding.etVerifyCodeSecond.getText().toString().length() == 1) {
                binding.etVerifyCodeSecond.clearFocus();
                binding.etVerifyCodeThird.requestFocus();
            }
        }
    };
    private final TextWatcher firstWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (binding.etVerifyCodeFirst.getText().toString().length() == 1) {
                binding.etVerifyCodeFirst.clearFocus();
                binding.etVerifyCodeSecond.requestFocus();
            }
        }
    };
    private final View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
        @SuppressLint({"UseCompatLoadingForDrawables", "NonConstantResourceId"})
        @Override
        public void onFocusChange(View view, boolean b) {

            if (view == null)
                return;
            switch (view.getId()) {
                case R.id.etVerifyCodeSecond:
                    if (binding.etVerifyCodeFirst.getText().toString().trim().isEmpty()) {
                        binding.etVerifyCodeFirst.requestFocus();
                        binding.etVerifyCodeSecond.setBackground(getResources().getDrawable(R.drawable.border));
                    } else {
                        binding.etVerifyCodeFirst.setBackground(getResources().getDrawable(R.drawable.border_blue));
                        binding.etVerifyCodeSecond.setBackground(getResources().getDrawable(R.drawable.border_blue));
                    }
                    break;

                case R.id.etVerifyCodeThird:
                    if (binding.etVerifyCodeSecond.getText().toString().trim().isEmpty()) {
                        binding.etVerifyCodeSecond.requestFocus();
                        binding.etVerifyCodeThird.setBackground(getResources().getDrawable(R.drawable.border));
                    } else {
                        binding.etVerifyCodeSecond.setBackground(getResources().getDrawable(R.drawable.border_blue));
                        binding.etVerifyCodeThird.setBackground(getResources().getDrawable(R.drawable.border_blue));
                    }
                    break;

                case R.id.etVerifyCodeForth:
                    if (binding.etVerifyCodeThird.getText().toString().trim().isEmpty()) {
                        binding.etVerifyCodeThird.requestFocus();
                        binding.etVerifyCodeForth.setBackground(getResources().getDrawable(R.drawable.border));
                    } else {
                        binding.etVerifyCodeThird.setBackground(getResources().getDrawable(R.drawable.border_blue));
                        binding.etVerifyCodeForth.setBackground(getResources().getDrawable(R.drawable.border_blue));
                    }
                    break;

                case R.id.etVerifyCodeFive:
                    if (binding.etVerifyCodeForth.getText().toString().trim().isEmpty()) {
                        binding.etVerifyCodeForth.requestFocus();
                        binding.etVerifyCodeFive.setBackground(getResources().getDrawable(R.drawable.border));
                    } else {
                        binding.etVerifyCodeForth.setBackground(getResources().getDrawable(R.drawable.border_blue));
                        binding.etVerifyCodeFive.setBackground(getResources().getDrawable(R.drawable.border_blue));
                    }
                    break;

                case R.id.etVerifyCodeSix:
                    if (binding.etVerifyCodeFive.getText().toString().trim().isEmpty()) {
                        binding.etVerifyCodeFive.requestFocus();
                        binding.etVerifyCodeSix.setBackground(getResources().getDrawable(R.drawable.border));
                    } else {
                        binding.etVerifyCodeFive.setBackground(getResources().getDrawable(R.drawable.border_blue));
                        binding.etVerifyCodeSix.setBackground(getResources().getDrawable(R.drawable.border_blue));
                    }
                    break;
            }

        }
    };
    private String verificationId;
    private String countryCode;
    private String mobile;

    /*
    private TextWatcher forthWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (binding.etVerifyCodeFirst.getText().toString().length() == 1 &&
                    binding.etVerifyCodeSecond.getText().toString().length() == 1 &&
                    binding.etVerifyCodeThird.getText().toString().length() == 1 &&
                    binding.etVerifyCodeForth.getText().toString().length() == 1) {

            }
        }
    };
    */
    private boolean isForgetPassword;

    public ConfirmCodeFragment() {
    }

    ConfirmCodeFragment(boolean isForgetPassword, String countryCode, String mobile, String verificationId) {
        this.verificationId = verificationId;
        this.countryCode = countryCode;
        this.mobile = mobile;
        this.isForgetPassword = isForgetPassword;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_confirm_code, null, false);
        AnalyticsUtility.logEventOpen(AnalyticsUtility.Events.CONFIRM_CODE);
        AnalyticsUtility.setScreen(this);
        CheckLanguage checkLanguage = new CheckLanguage(getActivity());
        checkLanguage.UpdateLanguage();
        if (UtilsPreference.getInstance(getActivity()).getPreference(Constants.LANGUAGE, "ar").equals("en")) {
            binding.tvNumber.setText((("(" + countryCode + ") ") + mobile));
        } else {
            binding.tvNumber.setText(((mobile + "(" + countryCode + ") ")));
        }
        binding.btnVerify.setOnClickListener(this);
        binding.tvResend.setOnClickListener(this);
        binding.etVerifyCodeFirst.addTextChangedListener(firstWatcher);
        binding.etVerifyCodeSecond.addTextChangedListener(secondWatcher);
        binding.etVerifyCodeThird.addTextChangedListener(thirdWatcher);
        binding.etVerifyCodeForth.addTextChangedListener(forthWatcher);
        binding.etVerifyCodeFive.addTextChangedListener(fiveWatcher);
        //binding.etVerifyCodeForth.addTextChangedListener(sixWatcher);

        binding.etVerifyCodeFirst.requestFocus();
        binding.etVerifyCodeForth.setOnFocusChangeListener(onFocusChangeListener);
        binding.etVerifyCodeThird.setOnFocusChangeListener(onFocusChangeListener);
        binding.etVerifyCodeSecond.setOnFocusChangeListener(onFocusChangeListener);
        binding.etVerifyCodeFive.setOnFocusChangeListener(onFocusChangeListener);
        binding.etVerifyCodeSix.setOnFocusChangeListener(onFocusChangeListener);
        return binding.getRoot();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (v == null)
            return;
        switch (v.getId()) {
            case R.id.btnVerify:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.GET_PEN_CODE);
                checkCodeAndVerify();
                break;
            case R.id.tvResend:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.RESEND_PEN_CODE);
                verify();
                break;
        }
    }

    private void checkCodeAndVerify() {

        if (getActivity() == null)
            return;

        if (verificationId == null || verificationId.trim().isEmpty())
            return;

        if (binding.etVerifyCodeFirst.getText() == null || binding.etVerifyCodeFirst.getText().toString().trim().isEmpty())
            return;

        if (binding.etVerifyCodeSecond.getText() == null || binding.etVerifyCodeSecond.getText().toString().trim().isEmpty())
            return;

        if (binding.etVerifyCodeThird.getText() == null || binding.etVerifyCodeThird.getText().toString().trim().isEmpty())
            return;

        if (binding.etVerifyCodeForth.getText() == null || binding.etVerifyCodeForth.getText().toString().trim().isEmpty())
            return;

        if (binding.etVerifyCodeFive.getText() == null || binding.etVerifyCodeFive.getText().toString().trim().isEmpty())
            return;


        if (binding.etVerifyCodeSix.getText() == null || binding.etVerifyCodeSix.getText().toString().trim().isEmpty())
            return;


        String code = binding.etVerifyCodeFirst.getText().toString() + binding.etVerifyCodeSecond.getText().toString() +
                binding.etVerifyCodeThird.getText().toString() + binding.etVerifyCodeForth.getText().toString() +
                binding.etVerifyCodeFive.getText().toString() + binding.etVerifyCodeSix.getText().toString();


        signInWithPhoneAuthCredential(PhoneAuthProvider.getCredential(verificationId, code));

        /*
        if (isForgetPassword && code.equals(verificationId)) {
            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                    .add(R.id.container, new ResetPasswordFragment(countryCode, mobile))
                    .addToBackStack("NewPasswordFragment")
                    .commit();

        } else if (!isForgetPassword && code.equals(verificationId)) {
            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                    .add(R.id.container, new RegisterFragment(countryCode, mobile))
                    .addToBackStack("RegisterFragment")
                    .commit();

        } else {
            Toast.makeText(ConfirmCodeFragment.this.getContext(), "wrong code ", Toast.LENGTH_SHORT).show();
        }
        */

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        if (getActivity() == null)
            return;
        FirebaseAuth auth = getInstance();
        auth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithCredential:success");

                        if (isForgetPassword) {
                            AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_RESET_PASSWORD);
                            getActivity()
                                    .getSupportFragmentManager()
                                    .beginTransaction()
                                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                                    .add(R.id.container, new ResetPasswordFragment(countryCode, mobile))
                                    .addToBackStack("NewPasswordFragment")
                                    .commit();

                        } else {
                            AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_REGISTER);
                            getActivity()
                                    .getSupportFragmentManager()
                                    .beginTransaction()
                                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                                    .add(R.id.container, new RegisterFragment(countryCode, mobile))
                                    .addToBackStack("RegisterFragment")
                                    .commit();


                        }

                    } else {

                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            GlobalInfoDialog.getInstance(getActivity()).setTitle(getString(R.string.error)).setMessage(task.getException().getMessage()).show();
                        }
                    }
                });
    }

    private void verify() {

        /*
        ProgressDialog progressBar = new ProgressDialog(ConfirmCodeFragment.this.getContext());
        progressBar.show();
        ApiInterface mApiService = ApiClient.getInterfaceService();
        Call<ModelVerify> call = mApiService.verify(countryCode, mobile);
        call.enqueue(new Callback<ModelVerify>() {
            @Override
            public void onResponse(@NonNull Call<ModelVerify> call, @NonNull Response<ModelVerify> response) {

                if (getActivity() == null)
                    return;
                if (!isAdded())
                    return;
                progressBar.dismiss();
                if (response.code() == 401) {
                    getActivity().startActivity(new Intent(getActivity(), AuthenticationActivity.class));
                    return;
                }

                if (response.code() != 200) {
                    assert response.errorBody() != null;
                    GlobalInfoDialog.getInstance(getActivity()).setTitle(getString(R.string.error)).setMessage(response.errorBody().toString()).show();
                    return;
                }
                ModelVerify body = response.body();

                if (body == null) {
                    GlobalInfoDialog.getInstance(getActivity()).setTitle(getString(R.string.error)).setMessage(getString(R.string.please_check_your_internet_connection)).show();
                    return;
                }

                if (body.getStatus() == 200 && body.getData() != null && body.getData() != null) {
                    confirmationCode = body.getData();
                } else {
                    GlobalInfoDialog.getInstance(getActivity()).setTitle(getString(R.string.error)).setMessage(body.getMessage()).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<ModelVerify> call, @NonNull Throwable t) {

                if (getActivity() == null)
                    return;
                if (!isAdded())
                    return;

                progressBar.dismiss();

                GlobalInfoDialog.getInstance(getActivity()).setTitle(getString(R.string.error)).setMessage(t.getMessage()).show();


            }
        });
        */
        if (getActivity() == null)
            return;
        String phoneNumber = "+" + countryCode + mobile;
        FirebaseAuth auth = getInstance();
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
                        AnalyticsUtility.logEvent(AnalyticsUtility.Events.EVENT_VERIFICATION_SUCCESS, AnalyticsUtility.Events.EVENT_VERIFICATION_SUCCESS);

                        if (isForgetPassword) {
                            AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_RESET_PASSWORD);
                            getActivity()
                                    .getSupportFragmentManager()
                                    .beginTransaction()
                                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                                    .add(R.id.container, new ResetPasswordFragment(countryCode, mobile))
                                    .addToBackStack("NewPasswordFragment")
                                    .commit();

                        } else {
                            AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_REGISTER);
                            getActivity()
                                    .getSupportFragmentManager()
                                    .beginTransaction()
                                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                                    .add(R.id.container, new RegisterFragment(countryCode, mobile))
                                    .addToBackStack("RegisterFragment")
                                    .commit();

                        }

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {

                        Log.d(TAG, "onVerificationFailed: " + e.getLocalizedMessage());

                        if (getActivity() == null)
                            return;
                        if (!isAdded())
                            return;

                        AnalyticsUtility.logEvent(AnalyticsUtility.Events.EVENT_VERIFICATION_FAIL, AnalyticsUtility.Events.EVENT_VERIFICATION_FAIL);

                        GlobalInfoDialog.getInstance(getActivity()).setTitle(getString(R.string.error)).setMessage(e.getLocalizedMessage()).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                        Log.d(TAG, "onCodeSent: " + verificationId);
                        AnalyticsUtility.logEvent(AnalyticsUtility.Events.EVENT_VERIFICATION_ON_CODE, AnalyticsUtility.Events.EVENT_VERIFICATION_ON_CODE);

                        ConfirmCodeFragment.this.verificationId = verificationId;

                    }
                });
    }
}
