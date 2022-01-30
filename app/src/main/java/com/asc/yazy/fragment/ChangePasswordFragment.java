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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.asc.yazy.R;
import com.asc.yazy.activity.AuthenticationActivity;
import com.asc.yazy.activity.NoNetActivity;
import com.asc.yazy.api.ApiClient;
import com.asc.yazy.api.ApiInterface;
import com.asc.yazy.api.model.ModelStatic;
import com.asc.yazy.api.pagination.offers.NetworkState;
import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.core.GlobalInfoDialog;
import com.asc.yazy.core.PlaneDialog;
import com.asc.yazy.databinding.FragmentChangePasswordBinding;
import com.asc.yazy.interfaces.IFragment;
import com.asc.yazy.utils.AnalyticsUtility;
import com.asc.yazy.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChangePasswordFragment extends Fragment implements View.OnClickListener, IFragment {

    private FragmentChangePasswordBinding binding;

    public ChangePasswordFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_change_password, null, false);
        AnalyticsUtility.logEventOpen(AnalyticsUtility.Events.CHANGE_PASSWORD);
        AnalyticsUtility.setScreen(this);
        CheckLanguage checkLanguage = new CheckLanguage(getActivity());
        checkLanguage.UpdateLanguage();
        binding.imgBack.setOnClickListener(this);
        binding.btnChangePass.setOnClickListener(this);
        binding.imgCancelNew.setOnClickListener(this);
        binding.imgCancelCurrent.setOnClickListener(this);
        binding.imgCancelConfirm.setOnClickListener(this);

        binding.etCurrentPassword.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    binding.imgCancelCurrent.setVisibility(View.VISIBLE);
                } else {
                    binding.imgCancelCurrent.setVisibility(View.INVISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        binding.etCreatePassword.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    binding.imgCancelNew.setVisibility(View.VISIBLE);
                } else {
                    binding.imgCancelNew.setVisibility(View.INVISIBLE);
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
                    binding.imgCancelConfirm.setVisibility(View.VISIBLE);
                } else {
                    binding.imgCancelConfirm.setVisibility(View.INVISIBLE);
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
            case R.id.imgBack:
                if (getActivity() != null)
                    getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.btnChangePass:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.CHANGE_PASSWORD);
                changePassWord();
                break;
            case R.id.imgCancelCurrent:
                if (binding.imgCancelCurrent.getTag().equals("shown")) {
                    binding.etCurrentPassword.setTransformationMethod(null);
                    binding.imgCancelCurrent.setImageResource(R.drawable.ic_hide);
                    binding.imgCancelCurrent.setTag("notshown");
                } else {
                    binding.etCurrentPassword.setTransformationMethod(new PasswordTransformationMethod());
                    binding.imgCancelCurrent.setImageResource(R.drawable.ic_show);
                    binding.imgCancelCurrent.setTag("shown");
                }

                break;
            case R.id.imgCancelNew:
                if (binding.imgCancelNew.getTag().equals("shown")) {
                    binding.etCreatePassword.setTransformationMethod(null);
                    binding.imgCancelNew.setImageResource(R.drawable.ic_hide);
                    binding.imgCancelNew.setTag("notshown");
                } else {
                    binding.etCreatePassword.setTransformationMethod(new PasswordTransformationMethod());
                    binding.imgCancelNew.setImageResource(R.drawable.ic_show);
                    binding.imgCancelNew.setTag("shown");
                }

                break;
            case R.id.imgCancelConfirm:

                if (binding.imgCancelConfirm.getTag().equals("shown")) {
                    binding.etRepeatPassword.setTransformationMethod(null);
                    binding.imgCancelConfirm.setImageResource(R.drawable.ic_hide);
                    binding.imgCancelConfirm.setTag("notshown");
                } else {
                    binding.etRepeatPassword.setTransformationMethod(new PasswordTransformationMethod());
                    binding.imgCancelConfirm.setImageResource(R.drawable.ic_show);
                    binding.imgCancelConfirm.setTag("shown");
                }

                break;

        }
    }

    @Override
    public void onBack() {
        if (getActivity() != null)
            getActivity().getSupportFragmentManager().popBackStack();
    }


    private void changePassWord() {
        if (getActivity() == null)
            return;
        binding.etCurrentPassword.setError(null);
        binding.etCreatePassword.setError(null);
        binding.etRepeatPassword.setError(null);
        if (binding.etCurrentPassword.getText() == null || binding.etCurrentPassword.getText().toString().trim().isEmpty()) {
            binding.etCurrentPassword.setError(Objects.requireNonNull(ChangePasswordFragment.this.getContext()).getResources().getString(R.string.old_password_requ));
            return;
        }
        if (binding.etCreatePassword.getText() == null || binding.etCreatePassword.getText().toString().trim().isEmpty()) {
            binding.etCreatePassword.setError(Objects.requireNonNull(ChangePasswordFragment.this.getContext()).getResources().getString(R.string.password_requ));
            return;
        }

        if (binding.etCreatePassword.getText() == null || binding.etCreatePassword.getText().toString().trim().isEmpty() || !Constants.isValidPassword(binding.etCreatePassword.getText().toString().trim())) {
            binding.etCreatePassword.setError(Objects.requireNonNull(ChangePasswordFragment.this.getContext()).getResources().getString(R.string.valid_password));
            return;
        }


        if (binding.etRepeatPassword.getText() == null || binding.etRepeatPassword.getText().toString().trim().isEmpty()) {
            binding.etRepeatPassword.setError(Objects.requireNonNull(ChangePasswordFragment.this.getContext()).getResources().getString(R.string.password_requ));
            return;
        }

        if (binding.etRepeatPassword.getText() == null || binding.etRepeatPassword.getText().toString().trim().isEmpty() || !Constants.isValidPassword(binding.etRepeatPassword.getText().toString().trim())) {
            binding.etRepeatPassword.setError(Objects.requireNonNull(ChangePasswordFragment.this.getContext()).getResources().getString(R.string.valid_password));
            return;
        }

        if (!binding.etCreatePassword.getText().toString().equals(binding.etRepeatPassword.getText().toString())) {
            binding.etRepeatPassword.setError(Objects.requireNonNull(ChangePasswordFragment.this.getContext()).getResources().getString(R.string.passwords_not_match));
            binding.etCreatePassword.setError(Objects.requireNonNull(ChangePasswordFragment.this.getContext()).getResources().getString(R.string.passwords_not_match));
            return;
        }
        if (binding.etCreatePassword.getText().toString().equals(binding.etCurrentPassword.getText().toString())) {
            binding.etCurrentPassword.setError(Objects.requireNonNull(ChangePasswordFragment.this.getContext()).getResources().getString(R.string.passwords_same));
            binding.etCreatePassword.setError(Objects.requireNonNull(ChangePasswordFragment.this.getContext()).getResources().getString(R.string.passwords_same));
            return;
        }

        String token = UtilsPreference.getInstance(getActivity()).getUser().getAccess_token();
        if (token == null || token.isEmpty()) {
            getActivity().startActivity(new Intent(getActivity(), AuthenticationActivity.class));
            return;
        }

        PlaneDialog planeDialog = new PlaneDialog(Objects.requireNonNull(getActivity()));
        ApiInterface mApiService = ApiClient.getInterfaceService();
        Call<ModelStatic> call = mApiService.changePassword("Bearer " + token, Constants.getLANGUAGE(), binding.etCurrentPassword.getText().toString(), binding.etCreatePassword.getText().toString());
        call.enqueue(new Callback<ModelStatic>() {
            @Override
            public void onResponse(@NonNull Call<ModelStatic> call, @NonNull Response<ModelStatic> response) {

                if (getActivity() == null)
                    return;
                if (ChangePasswordFragment.this.getContext() == null)
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
                        GlobalInfoDialog.getInstance(ChangePasswordFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(message).show();
                        return;
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                ModelStatic body = response.body();

                if (body == null) {
                    GlobalInfoDialog.getInstance(getActivity()).setTitle(getString(R.string.error)).setMessage(getString(R.string.please_check_your_internet_connection)).show();
                    return;
                }

                if (body.getStatus() == 200) {
                    AnalyticsUtility.logEventAPISuccess(AnalyticsUtility.Events.CHANGE_PASSWORD);
                    Toast.makeText(getContext(), body.getMessage(), Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().popBackStack();
                } else {
                    GlobalInfoDialog.getInstance(ChangePasswordFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(body.getMessage()).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<ModelStatic> call, @NonNull Throwable t) {

                if (ChangePasswordFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;
                AnalyticsUtility.logEventAPIFail(AnalyticsUtility.Events.CHANGE_PASSWORD);
                planeDialog.Dismiss();
                //GlobalInfoDialog.getInstance(ChangePasswordFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(ChangePasswordFragment.this.getContext().getResources().getString(R.string.cant_connect)).show();
                requireActivity().startActivity(new Intent(getActivity(), NoNetActivity.class));


            }
        });
    }


}
