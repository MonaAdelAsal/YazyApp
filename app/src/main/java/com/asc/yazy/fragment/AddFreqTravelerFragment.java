package com.asc.yazy.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.asc.yazy.R;
import com.asc.yazy.activity.NoNetActivity;
import com.asc.yazy.api.ApiClient;
import com.asc.yazy.api.ApiInterface;
import com.asc.yazy.api.model.ModelStatic;
import com.asc.yazy.api.model.ModelUser;
import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.core.DatePickerDialog;
import com.asc.yazy.core.GlobalInfoDialog;
import com.asc.yazy.core.PlaneDialog;
import com.asc.yazy.databinding.FragmentAddTravellerBinding;
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


public class AddFreqTravelerFragment extends Fragment implements View.OnClickListener, IFragment {


    private FragmentAddTravellerBinding binding;
    private int type = 1;
    private FrequentTravellersFragment.OnNewItem onNewItem;
    private String expiry, birth;

    public AddFreqTravelerFragment() {
    }

    public AddFreqTravelerFragment(FrequentTravellersFragment.OnNewItem onNewItem) {
        this.onNewItem = onNewItem;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_add_traveller, null, false);
        AnalyticsUtility.logEventOpen(AnalyticsUtility.Events.ADD_FREQUENT_TRAVELLER);
        AnalyticsUtility.setScreen(this);
        binding.cpNationality.setDefaultCountryUsingNameCode("KW");
        binding.imgBack.setOnClickListener(this);
        binding.btnSave.setOnClickListener(this);
        binding.layoutChild.setOnClickListener(this);
        binding.BD.setOnClickListener(this);
        binding.layoutAdult.setOnClickListener(this);
        binding.checkCivilID.setOnClickListener(this);
        binding.checkPassPort.setOnClickListener(this);
        binding.ex.setOnClickListener(this);
        CheckLanguage checkLanguage = new CheckLanguage(getActivity());
        checkLanguage.UpdateLanguage();
        return binding.getRoot();
    }


    @SuppressLint({"UseCompatLoadingForDrawables", "NonConstantResourceId"})
    @Override
    public void onClick(View v) {
        if (v == null)
            return;
        switch (v.getId()) {

            case R.id.imgBack:
                if (getActivity() != null)
                    getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.btnSave:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.ADD_FREQUENT_TRAVELLER);
                saveTraveller();
                break;
            case R.id.layoutAdult:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.ADD_ADULT_FREQUENT_TRAVELLER);
                type = 1;
                binding.tvAdult.setTextColor(AddFreqTravelerFragment.this.getResources().getColor(R.color.colorAccent));
                binding.imgAdult.setColorFilter(ContextCompat.getColor(Objects.requireNonNull(AddFreqTravelerFragment.this.getContext()), R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
                binding.layoutAdult.setBackground(AddFreqTravelerFragment.this.getContext().getResources().getDrawable(R.drawable.border_accent));
                binding.BDLayout.setVisibility(View.GONE);
                binding.tvChild.setTextColor(AddFreqTravelerFragment.this.getResources().getColor(R.color.black));
                binding.imgChild.setColorFilter(ContextCompat.getColor(Objects.requireNonNull(AddFreqTravelerFragment.this.getContext()), R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
                binding.layoutChild.setBackground(AddFreqTravelerFragment.this.getContext().getResources().getDrawable(R.drawable.border));
                binding.tvAge.setText(AddFreqTravelerFragment.this.requireContext().getResources().getString(R.string._12_years));
                break;

            case R.id.layoutChild:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.ADD_CHILD_FREQUENT_TRAVELLER);
                type = 2;
                binding.tvChild.setTextColor(AddFreqTravelerFragment.this.getResources().getColor(R.color.colorAccent));
                binding.imgChild.setColorFilter(ContextCompat.getColor(Objects.requireNonNull(AddFreqTravelerFragment.this.getContext()), R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
                binding.layoutChild.setBackground(AddFreqTravelerFragment.this.getContext().getResources().getDrawable(R.drawable.border_accent));
                binding.BDLayout.setVisibility(View.VISIBLE);
                binding.tvAdult.setTextColor(AddFreqTravelerFragment.this.getResources().getColor(R.color.black));
                binding.imgAdult.setColorFilter(ContextCompat.getColor(Objects.requireNonNull(AddFreqTravelerFragment.this.getContext()), R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
                binding.layoutAdult.setBackground(AddFreqTravelerFragment.this.getContext().getResources().getDrawable(R.drawable.border));
                binding.tvAge.setText(AddFreqTravelerFragment.this.requireContext().getResources().getString(R.string._2_12_years));
                break;

            case R.id.checkCivilID:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.ADD_FREQUENT_TRAVELLER_CIVIL_ID);
                binding.layoutPassPort.setVisibility(View.GONE);
                binding.llExpiryDate.setVisibility(View.GONE);

                binding.layoutCivilID.setVisibility(View.VISIBLE);
                binding.checkPassPort.setChecked(false);
                binding.checkCivilID.setChecked(true);
                break;
            case R.id.checkPassPort:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.ADD_FREQUENT_TRAVELLER_PASSPORT);
                binding.layoutPassPort.setVisibility(View.VISIBLE);
                binding.llExpiryDate.setVisibility(View.VISIBLE);
                binding.layoutCivilID.setVisibility(View.GONE);
                binding.checkPassPort.setChecked(true);
                binding.checkCivilID.setChecked(false);
                break;
            case R.id.ex:
                new DatePickerDialog(requireContext(), (selectedDate, formattedDate) -> {
                    binding.etExpiryDate.setText(selectedDate);
                    expiry = formattedDate;

                });
                break;
            case R.id.BD:
                new DatePickerDialog(requireContext(), (selectedDate, formattedDate) -> {
                    binding.etBirthDate.setText(selectedDate);
                    birth = formattedDate;
                });
                break;

        }
    }

    private void saveTraveller() {

        ModelUser user = UtilsPreference.getInstance(AddFreqTravelerFragment.this.getContext()).getUser();
        if (user == null || user.getAccess_token() == null || user.getAccess_token().isEmpty())
            return;


        if (binding.etName.getText() == null || binding.etName.getText().toString().trim().isEmpty()) {
            binding.etName.setError(Objects.requireNonNull(AddFreqTravelerFragment.this.getContext()).getResources().getString(R.string.user_name_requ));
            return;
        }

        String[] SeparatedName = binding.etName.getText().toString().split(" ");

        if (SeparatedName.length < 4) {
            Toast.makeText(AddFreqTravelerFragment.this.getContext(), getResources().getString(R.string.valid_name), Toast.LENGTH_SHORT).show();
            return;
        }

        if (binding.cpNationality.getSelectedCountryNameCode() == null) {
            Toast.makeText(AddFreqTravelerFragment.this.getContext(), R.string.nationality_missing, Toast.LENGTH_SHORT).show();
            return;
        }

        if (binding.checkPassPort.isChecked()) {

            if (binding.etPassPortNumber.getText() == null || binding.etPassPortNumber.getText().toString().trim().isEmpty()) {
                binding.etPassPortNumber.setError(Objects.requireNonNull(AddFreqTravelerFragment.this.getContext()).getResources().getString(R.string.missing_data));
                return;
            }
            if (binding.etExpiryDate.getText() == null || binding.etExpiryDate.getText().toString().trim().isEmpty()) {
                binding.etExpiryDate.setError(Objects.requireNonNull(AddFreqTravelerFragment.this.getContext()).getResources().getString(R.string.missing_data));
                return;
            }
        }

        if (binding.checkCivilID.isChecked()) {

            if (binding.etCivilID.getText() == null || binding.etCivilID.getText().toString().trim().isEmpty()) {
                binding.etCivilID.setError(Objects.requireNonNull(AddFreqTravelerFragment.this.getContext()).getResources().getString(R.string.missing_data));
                return;
            }
        }

        PlaneDialog planeDialog = new PlaneDialog(Objects.requireNonNull(getActivity()));

        ApiInterface mApiService = ApiClient.getInterfaceService();
        Call<ModelStatic> call = mApiService.addTraveller(
                "Bearer " + user.getAccess_token(), Constants.getLANGUAGE(),
                type,
                binding.etName.getText().toString(),
                binding.cpNationality.getSelectedCountryNameCode(),
                binding.etPassPortNumber.getText().toString(),
                binding.etCivilID.getText().toString(),
                expiry,
                birth);
        call.enqueue(new Callback<ModelStatic>() {
            @Override
            public void onResponse(@NonNull Call<ModelStatic> call, @NonNull Response<ModelStatic> response) {

                if (getActivity() == null)
                    return;
                if (AddFreqTravelerFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;
                planeDialog.Dismiss();

                if (response.code() != 200) {
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String message = jObjError.getString("message");
                        GlobalInfoDialog.getInstance(AddFreqTravelerFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(message).show();
                        return;
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                ModelStatic body = response.body();

                if (body == null) {
                    GlobalInfoDialog.getInstance(AddFreqTravelerFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(getString(R.string.please_check_your_internet_connection)).show();
                    return;
                }
                if (body.getStatus() == 200) {
                    AnalyticsUtility.logEventAPISuccess(AnalyticsUtility.Events.ADD_FREQUENT_TRAVELLER);
                    GlobalInfoDialog.getInstance(AddFreqTravelerFragment.this.getContext()).setTitle(getString(R.string.app_name)).setMessage(body.getMessage()).show();
                    if (onNewItem != null)
                        onNewItem.newData();
                    if (getActivity() != null)
                        getActivity().getSupportFragmentManager().popBackStack();
                } else
                    GlobalInfoDialog.getInstance(AddFreqTravelerFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(body.getMessage()).show();


            }

            @Override
            public void onFailure(@NonNull Call<ModelStatic> call, @NonNull Throwable t) {

                if (AddFreqTravelerFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;
                planeDialog.Dismiss();
                AnalyticsUtility.logEventAPIFail(AnalyticsUtility.Events.ADD_FREQUENT_TRAVELLER);
                // GlobalInfoDialog.getInstance(AddFreqTravelerFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(AddFreqTravelerFragment.this.getContext().getResources().getString(R.string.cant_connect)).show();
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
