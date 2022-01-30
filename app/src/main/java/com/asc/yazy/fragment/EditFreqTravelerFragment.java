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
import com.asc.yazy.api.model.ModelTravellerDetails;
import com.asc.yazy.api.model.ModelUser;
import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.core.DatePickerDialog;
import com.asc.yazy.core.GlobalInfoDialog;
import com.asc.yazy.core.PlaneDialog;
import com.asc.yazy.databinding.FragmentEditTravellerBinding;
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


public class EditFreqTravelerFragment extends Fragment implements View.OnClickListener, IFragment {


    private FragmentEditTravellerBinding binding;
    private int type = 1;
    private FrequentTravellersFragment.OnNewItem onNewItem;
    private ModelTravellerDetails modelTravellerDetails;
    private String expiry, birth = "";


    public EditFreqTravelerFragment() {
    }


    public EditFreqTravelerFragment(ModelTravellerDetails modelTravellerDetails, FrequentTravellersFragment.OnNewItem onNewItem) {
        this.modelTravellerDetails = modelTravellerDetails;
        this.type = modelTravellerDetails.getType();
        this.expiry = modelTravellerDetails.getTravellerPassportExpiry();
        this.birth = modelTravellerDetails.getTravellerBirthDate();
        this.onNewItem = onNewItem;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_edit_traveller, null, false);
        AnalyticsUtility.logEvent(AnalyticsUtility.Events.ABOUT, AnalyticsUtility.Events.ABOUT);
        AnalyticsUtility.setScreen(this);
        binding.imgBack.setOnClickListener(this);
        binding.btnSave.setOnClickListener(this);
        binding.layoutChild.setOnClickListener(this);
        binding.layoutAdult.setOnClickListener(this);
        binding.BD.setOnClickListener(this);
        binding.checkCivilID.setOnClickListener(this);
        binding.checkPassPort.setOnClickListener(this);
        binding.ex.setOnClickListener(this);
        CheckLanguage checkLanguage = new CheckLanguage(getActivity());
        checkLanguage.UpdateLanguage();
        binding.setData(modelTravellerDetails);
        binding.cpNationality.setCountryForNameCode(String.valueOf(modelTravellerDetails.getNationality()));
        if (modelTravellerDetails.getType() == 1) {
            type = 1;
            binding.tvAdult.setTextColor(EditFreqTravelerFragment.this.getResources().getColor(R.color.colorAccent));
            binding.imgAdult.setColorFilter(ContextCompat.getColor(Objects.requireNonNull(EditFreqTravelerFragment.this.getContext()), R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
            binding.layoutAdult.setBackground(EditFreqTravelerFragment.this.getContext().getResources().getDrawable(R.drawable.border_accent));

            binding.tvChild.setTextColor(EditFreqTravelerFragment.this.getResources().getColor(R.color.black));
            binding.imgChild.setColorFilter(ContextCompat.getColor(Objects.requireNonNull(EditFreqTravelerFragment.this.getContext()), R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
            binding.layoutChild.setBackground(EditFreqTravelerFragment.this.getContext().getResources().getDrawable(R.drawable.border));

            binding.tvAge.setText(EditFreqTravelerFragment.this.requireContext().getResources().getString(R.string._12_years));
        } else {
            type = 2;
            binding.tvChild.setTextColor(EditFreqTravelerFragment.this.getResources().getColor(R.color.colorAccent));
            binding.imgChild.setColorFilter(ContextCompat.getColor(Objects.requireNonNull(EditFreqTravelerFragment.this.getContext()), R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
            binding.layoutChild.setBackground(EditFreqTravelerFragment.this.getContext().getResources().getDrawable(R.drawable.border_accent));
            binding.BDLayout.setVisibility(View.VISIBLE);
            binding.tvAdult.setTextColor(EditFreqTravelerFragment.this.getResources().getColor(R.color.black));
            binding.imgAdult.setColorFilter(ContextCompat.getColor(Objects.requireNonNull(EditFreqTravelerFragment.this.getContext()), R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
            binding.layoutAdult.setBackground(EditFreqTravelerFragment.this.getContext().getResources().getDrawable(R.drawable.border));

            binding.tvAge.setText(EditFreqTravelerFragment.this.requireContext().getResources().getString(R.string._2_12_years));
        }
        if (!modelTravellerDetails.getPassport_no().isEmpty()) {
            binding.checkPassPort.setChecked(true);
            binding.checkCivilID.setChecked(false);
            binding.layoutPassPort.setVisibility(View.VISIBLE);
            binding.llExpiryDate.setVisibility(View.VISIBLE);

            binding.layoutCivilID.setVisibility(View.GONE);
        }
        if (!modelTravellerDetails.getCivil_id().isEmpty()) {
            binding.checkPassPort.setChecked(false);
            binding.checkCivilID.setChecked(true);
            binding.llExpiryDate.setVisibility(View.GONE);
            binding.layoutPassPort.setVisibility(View.GONE);
            binding.layoutCivilID.setVisibility(View.VISIBLE);
        }
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
                saveTraveller();
                break;
            case R.id.layoutAdult:
                type = 1;
                binding.tvAdult.setTextColor(EditFreqTravelerFragment.this.getResources().getColor(R.color.colorAccent));
                binding.imgAdult.setColorFilter(ContextCompat.getColor(Objects.requireNonNull(EditFreqTravelerFragment.this.getContext()), R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
                binding.layoutAdult.setBackground(EditFreqTravelerFragment.this.getContext().getResources().getDrawable(R.drawable.border_accent));
                binding.BDLayout.setVisibility(View.GONE);
                binding.tvChild.setTextColor(EditFreqTravelerFragment.this.getResources().getColor(R.color.black));
                binding.imgChild.setColorFilter(ContextCompat.getColor(Objects.requireNonNull(EditFreqTravelerFragment.this.getContext()), R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
                binding.layoutChild.setBackground(EditFreqTravelerFragment.this.getContext().getResources().getDrawable(R.drawable.border));
                binding.tvAge.setText(EditFreqTravelerFragment.this.requireContext().getResources().getString(R.string._12_years));
                break;

            case R.id.layoutChild:
                type = 2;
                binding.tvChild.setTextColor(EditFreqTravelerFragment.this.getResources().getColor(R.color.colorAccent));
                binding.imgChild.setColorFilter(ContextCompat.getColor(Objects.requireNonNull(EditFreqTravelerFragment.this.getContext()), R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
                binding.layoutChild.setBackground(EditFreqTravelerFragment.this.getContext().getResources().getDrawable(R.drawable.border_accent));
                binding.BDLayout.setVisibility(View.VISIBLE);
                binding.tvAdult.setTextColor(EditFreqTravelerFragment.this.getResources().getColor(R.color.black));
                binding.imgAdult.setColorFilter(ContextCompat.getColor(Objects.requireNonNull(EditFreqTravelerFragment.this.getContext()), R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
                binding.layoutAdult.setBackground(EditFreqTravelerFragment.this.getContext().getResources().getDrawable(R.drawable.border));
                binding.tvAge.setText(EditFreqTravelerFragment.this.requireContext().getResources().getString(R.string._2_12_years));
                break;

            case R.id.checkCivilID:
                binding.layoutPassPort.setVisibility(View.GONE);
                binding.llExpiryDate.setVisibility(View.GONE);
                binding.layoutCivilID.setVisibility(View.VISIBLE);
                binding.checkPassPort.setChecked(false);
                binding.checkCivilID.setChecked(true);
                break;
            case R.id.checkPassPort:
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

        ModelUser user = UtilsPreference.getInstance(EditFreqTravelerFragment.this.getContext()).getUser();
        if (user == null || user.getAccess_token() == null || user.getAccess_token().isEmpty())
            return;


        if (binding.etName.getText() == null || binding.etName.getText().toString().trim().isEmpty()) {
            binding.etName.setError(Objects.requireNonNull(EditFreqTravelerFragment.this.getContext()).getResources().getString(R.string.user_name_requ));
            return;
        }

        String[] SeparatedName = binding.etName.getText().toString().split(" ");

        if (SeparatedName.length < 4) {
            Toast.makeText(EditFreqTravelerFragment.this.getContext(), getResources().getString(R.string.valid_name), Toast.LENGTH_SHORT).show();
            return;
        }

        if (binding.cpNationality.getSelectedCountryNameCode() == null) {
            Toast.makeText(EditFreqTravelerFragment.this.getContext(), R.string.nationality_missing, Toast.LENGTH_SHORT).show();
            return;
        }

        if (binding.checkPassPort.isChecked()) {

            if (binding.etPassPortNumber.getText() == null || binding.etPassPortNumber.getText().toString().trim().isEmpty()) {
                binding.etPassPortNumber.setError(Objects.requireNonNull(EditFreqTravelerFragment.this.getContext()).getResources().getString(R.string.missing_data));
                return;
            }
            if (binding.etExpiryDate.getText() == null || binding.etExpiryDate.getText().toString().trim().isEmpty()) {
                binding.etExpiryDate.setError(Objects.requireNonNull(EditFreqTravelerFragment.this.getContext()).getResources().getString(R.string.missing_data));
                return;
            }
        }


        if (binding.checkCivilID.isChecked()) {

            if (binding.etCivilID.getText() == null || binding.etCivilID.getText().toString().trim().isEmpty()) {
                binding.etCivilID.setError(Objects.requireNonNull(EditFreqTravelerFragment.this.getContext()).getResources().getString(R.string.missing_data));
                return;
            }
        }

        if (expiry == null)
            expiry = "";
        if (birth == null)
            birth = "";

        if (binding.etName.getText().toString().equals(modelTravellerDetails.getName()) &&
                binding.cpNationality.getSelectedCountryNameCode().equals(modelTravellerDetails.getNationality()) &&
                binding.etPassPortNumber.getText().toString().equals(modelTravellerDetails.getPassport_no()) &&
                expiry.equals(modelTravellerDetails.getTravellerPassportExpiry()) &&
                birth.equals(modelTravellerDetails.getTravellerBirthDate()) &&
                binding.etCivilID.getText().toString().equals(modelTravellerDetails.getCivil_id()) &&
                type == modelTravellerDetails.getType()) {
            Toast.makeText(EditFreqTravelerFragment.this.requireContext(), getResources().getString(R.string.no_ch), Toast.LENGTH_SHORT).show();
            return;
        }

        PlaneDialog planeDialog = new PlaneDialog(Objects.requireNonNull(getActivity()));

        ApiInterface mApiService = ApiClient.getInterfaceService();
        Call<ModelStatic> call = mApiService.updateTraveller(
                "Bearer " + user.getAccess_token(), Constants.getLANGUAGE(),
                modelTravellerDetails.getId(),
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
                if (EditFreqTravelerFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;
                planeDialog.Dismiss();

                if (response.code() != 200) {
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String message = jObjError.getString("message");
                        GlobalInfoDialog.getInstance(EditFreqTravelerFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(message).show();
                        return;
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                ModelStatic body = response.body();

                if (body == null) {
                    GlobalInfoDialog.getInstance(EditFreqTravelerFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(getString(R.string.please_check_your_internet_connection)).show();
                    return;
                }
                if (body.getStatus() == 200) {
                    GlobalInfoDialog.getInstance(EditFreqTravelerFragment.this.getContext()).setTitle(getString(R.string.app_name)).setMessage(body.getMessage()).show();
                    if (onNewItem != null)
                        onNewItem.newData();
                    if (getActivity() != null)
                        getActivity().getSupportFragmentManager().popBackStack();
                } else
                    GlobalInfoDialog.getInstance(EditFreqTravelerFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(body.getMessage()).show();


            }

            @Override
            public void onFailure(@NonNull Call<ModelStatic> call, @NonNull Throwable t) {

                if (EditFreqTravelerFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;
                planeDialog.Dismiss();
                // GlobalInfoDialog.getInstance(EditFreqTravelerFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(EditFreqTravelerFragment.this.getContext().getResources().getString(R.string.cant_connect)).show();
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
