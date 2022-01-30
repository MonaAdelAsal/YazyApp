package com.asc.yazy.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.asc.yazy.R;
import com.asc.yazy.activity.AuthenticationActivity;
import com.asc.yazy.activity.MainActivity;
import com.asc.yazy.activity.NoNetActivity;
import com.asc.yazy.api.ApiClient;
import com.asc.yazy.api.ApiInterface;
import com.asc.yazy.api.model.ModelAuthenticate;
import com.asc.yazy.api.model.ModelUser;
import com.asc.yazy.api.pagination.offers.NetworkState;
import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.core.GlobalInfoDialog;
import com.asc.yazy.core.PlaneDialog;
import com.asc.yazy.databinding.FragmentEditProfileBinding;
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


public class EditProfileFragment extends Fragment implements View.OnClickListener, IFragment {


    private long mLastClickTime = System.currentTimeMillis();
    private ModelUser user;
    private FragmentEditProfileBinding binding;

    public EditProfileFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_edit_profile, null, false);
        AnalyticsUtility.logEventOpen(AnalyticsUtility.Events.EDIT_PROFILE);
        AnalyticsUtility.setScreen(this);
        CheckLanguage checkLanguage = new CheckLanguage(getActivity());
        checkLanguage.UpdateLanguage();
        binding.imgBack.setOnClickListener(this);
        binding.imgCancelName.setOnClickListener(this);
        binding.imgCancelEmail.setOnClickListener(this);
        binding.imgCancelExpiryDate.setOnClickListener(this);
        binding.cpNationality.setExcludedCountries("IL");
        binding.imgCancelPassport.setOnClickListener(this);
        binding.imgCancelCivil.setOnClickListener(this);
        binding.btnUpdate.setOnClickListener(this);
        binding.etUsername.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    binding.imgCancelName.setVisibility(View.VISIBLE);
                } else {
                    binding.imgCancelName.setVisibility(View.INVISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
        binding.etCivil.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    binding.imgCancelCivil.setVisibility(View.VISIBLE);
                } else {
                    binding.imgCancelCivil.setVisibility(View.INVISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        binding.etEmail.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    binding.imgCancelEmail.setVisibility(View.VISIBLE);
                } else {
                    binding.imgCancelEmail.setVisibility(View.INVISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
        binding.etExpiryDate.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    binding.imgCancelExpiryDate.setVisibility(View.VISIBLE);
                } else {
                    binding.imgCancelExpiryDate.setVisibility(View.INVISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
        binding.etPassport.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    binding.imgCancelPassport.setVisibility(View.VISIBLE);
                    binding.llExpiryDate.setVisibility(View.VISIBLE);
                } else {
                    binding.imgCancelPassport.setVisibility(View.INVISIBLE);
                    binding.llExpiryDate.setVisibility(View.GONE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });


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
        binding.cpNationality.setCountryForNameCode(String.valueOf(user.getNationality()));
        AnalyticsUtility.logEventLoadDate(AnalyticsUtility.Events.EDIT_PROFILE);
    }

    @SuppressLint("NonConstantResourceId")
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
            case R.id.imgCancelEmail:
                binding.etEmail.setText(null);
                break;
            case R.id.imgCancelExpiryDate:
                binding.etExpiryDate.setText(null);
                break;
            case R.id.imgCancelName:
                binding.etUsername.setText(null);
                break;
            case R.id.imgCancelPassport:
                binding.etPassport.setText(null);
                break;
            case R.id.imgCancelCivil:
                binding.etCivil.setText(null);
                break;
            case R.id.btnUpdate:
                update();
                break;
        }
    }

    private boolean isDataChanged() {
        return !user.getName().equals(binding.etUsername.getText().toString()) ||
                !user.getEmail().equals(binding.etEmail.getText().toString()) ||
                !user.getCivil_id().equals(binding.etCivil.getText().toString()) ||
                !user.getPassport_no().equals(binding.etPassport.getText().toString()) ||
                !user.getPassport_expiry().equals(binding.etExpiryDate.getText().toString()) ||
                !user.getNationality().equals(binding.cpNationality.getSelectedCountryNameCode());

    }

    private void update() {

        if (getActivity() == null)
            return;

        if (!isDataChanged()) {
            Toast.makeText(EditProfileFragment.this.requireContext(), EditProfileFragment.this.requireContext().getResources().getString(R.string.no_data_changed), Toast.LENGTH_SHORT).show();
            return;
        }

        if (binding.etUsername.getText() == null || binding.etUsername.getText().toString().trim().isEmpty() || !binding.etUsername.getText().toString().contains(" ")) {
            binding.etUsername.setError(Objects.requireNonNull(EditProfileFragment.this.getContext()).getResources().getString(R.string.user_name_requ));
            return;
        }

        if (binding.cpNationality.getSelectedCountryNameCode() == null) {
            Toast.makeText(EditProfileFragment.this.getContext(), R.string.nationality_missing, Toast.LENGTH_SHORT).show();
            return;
        }


        String token = UtilsPreference.getInstance(getActivity()).getUser().getAccess_token();
        if (token == null || token.isEmpty()) {
            getActivity().startActivity(new Intent(getActivity(), AuthenticationActivity.class));
            return;
        }
        PlaneDialog planeDialog = new PlaneDialog(Objects.requireNonNull(getActivity()));
        ApiInterface mApiService = ApiClient.getInterfaceService();
        Call<ModelAuthenticate> call = mApiService.updateProfile("Bearer " + token, Constants.getLANGUAGE(), binding.etUsername.getText().toString(), binding.etEmail.getText().toString(), binding.cpNationality.getSelectedCountryNameCode(), binding.etPassport.getText().toString(), binding.etCivil.getText().toString(), binding.etExpiryDate.getText().toString());
        call.enqueue(new Callback<ModelAuthenticate>() {
            @Override
            public void onResponse(@NonNull Call<ModelAuthenticate> call, @NonNull Response<ModelAuthenticate> response) {

                if (getActivity() == null)
                    return;
                if (EditProfileFragment.this.getContext() == null)
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
                        GlobalInfoDialog.getInstance(EditProfileFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(message).show();
                        return;
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                ModelAuthenticate body = response.body();

                if (body == null) {
                    GlobalInfoDialog.getInstance(EditProfileFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(getString(R.string.please_check_your_internet_connection)).show();
                    return;
                }
                if (body.getStatus() == 200) {
                    AnalyticsUtility.logEventAPISuccess(AnalyticsUtility.Events.EDIT_PROFILE);
                    user.setName(body.getData().getName());
                    user.setEmail(body.getData().getEmail());
                    user.setNationality(body.getData().getNationality());
                    user.setCivil_id(body.getData().getCivil_id());
                    user.setPassport_no(body.getData().getPassport_no());
                    //user.setCivil_id("");
                    // user.setPassport_no("");
                    Toast.makeText(EditProfileFragment.this.requireContext(), getString(R.string.successfully), Toast.LENGTH_SHORT).show();
                    UtilsPreference.getInstance(getActivity()).saveUser(user);
                    getActivity().getSupportFragmentManager().popBackStack();
                    MainActivity.mainContainerViewPagerAdapter.notifyDataSetChanged();

                } else {
                    GlobalInfoDialog.getInstance(EditProfileFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(body.getMessage()).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ModelAuthenticate> call, @NonNull Throwable t) {

                if (EditProfileFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;

                planeDialog.Dismiss();
                AnalyticsUtility.logEventAPIFail(AnalyticsUtility.Events.EDIT_PROFILE);
                //GlobalInfoDialog.getInstance(EditProfileFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(EditProfileFragment.this.getContext().getResources().getString(R.string.cant_connect)).show();
                requireActivity().startActivity(new Intent(getActivity(), NoNetActivity.class));

            }
        });
    }

    @Override
    public void onBack() {
        if (getActivity() != null)
            getActivity().getSupportFragmentManager().popBackStack();
    }
}
