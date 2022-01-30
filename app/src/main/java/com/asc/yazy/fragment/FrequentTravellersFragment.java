package com.asc.yazy.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.asc.yazy.R;
import com.asc.yazy.activity.NoNetActivity;
import com.asc.yazy.adapter.TravellersAdapter;
import com.asc.yazy.api.ApiClient;
import com.asc.yazy.api.ApiInterface;
import com.asc.yazy.api.model.ModelTravellerAPI;
import com.asc.yazy.api.model.ModelTravellerDetails;
import com.asc.yazy.api.model.ModelUser;
import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.core.GlobalInfoDialog;
import com.asc.yazy.databinding.FragmentRequentTravellersBinding;
import com.asc.yazy.interfaces.IFragment;
import com.asc.yazy.utils.AnalyticsUtility;
import com.asc.yazy.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FrequentTravellersFragment extends Fragment implements View.OnClickListener, IFragment {


    public static boolean newItem = false;
    private FragmentRequentTravellersBinding binding;
    private final OnNewItem onNewItem = () -> {
        loadingDate();
        loadData();
    };


    public FrequentTravellersFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_requent_travellers, null, false);
        AnalyticsUtility.logEventOpen(AnalyticsUtility.Events.FREQUENT_TRAVELLERS);
        AnalyticsUtility.setScreen(this);
        binding.imgBack.setOnClickListener(this);
        binding.btnSave.setOnClickListener(this);
        CheckLanguage checkLanguage = new CheckLanguage(getActivity());
        checkLanguage.UpdateLanguage();
        loadingDate();
        loadData();
        return binding.getRoot();
    }


    private void loadData() {

        ModelUser user = UtilsPreference.getInstance(FrequentTravellersFragment.this.getContext()).getUser();
        if (user == null || user.getAccess_token() == null || user.getAccess_token().isEmpty())
            return;

        ApiInterface mApiService = ApiClient.getInterfaceService();
        Call<ModelTravellerAPI> call = mApiService.getTravellers("Bearer " + user.getAccess_token(), Constants.getLANGUAGE());
        call.enqueue(new Callback<ModelTravellerAPI>() {
            @Override
            public void onResponse(@NonNull Call<ModelTravellerAPI> call, @NonNull Response<ModelTravellerAPI> response) {
                if (getActivity() == null)
                    return;
                if (FrequentTravellersFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;
                if (response.code() != 200) {
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String message = jObjError.getString("message");
                        GlobalInfoDialog.getInstance(FrequentTravellersFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(message).show();
                        return;
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                ModelTravellerAPI body = response.body();

                if (body == null) {
                    GlobalInfoDialog.getInstance(FrequentTravellersFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(getString(R.string.please_check_your_internet_connection)).show();
                    return;
                }
                if (body.getData() != null && body.getData() != null) {
                    AnalyticsUtility.logEventAPISuccess(AnalyticsUtility.Events.FREQUENT_TRAVELLERS);
                    List<ModelTravellerDetails> list = new ArrayList<>();
                    list.addAll(body.getData().getAdults());
                    list.addAll(body.getData().getChildren());
                    if (list.size() == 0) {
                        binding.layoutNoData.getRoot().setVisibility(View.VISIBLE);
                        binding.layoutNoData.btnAddTraveller.setOnClickListener(FrequentTravellersFragment.this);
                        binding.rvTravellers.setVisibility(View.GONE);
                        binding.btnSave.setVisibility(View.GONE);
                    } else {
                        TravellersAdapter adult = new TravellersAdapter(FrequentTravellersFragment.this.requireActivity(), list, onNewItem, false);
                        binding.rvTravellers.setLayoutManager(new LinearLayoutManager(FrequentTravellersFragment.this.getContext()));
                        binding.rvTravellers.setHasFixedSize(true);
                        binding.rvTravellers.setAdapter(adult);

                        binding.layoutNoData.getRoot().setVisibility(View.GONE);
                        binding.rvTravellers.setVisibility(View.VISIBLE);
                        binding.btnSave.setVisibility(View.VISIBLE);
                    }


                }
            }

            @Override
            public void onFailure(@NonNull Call<ModelTravellerAPI> call, @NonNull Throwable t) {

                if (FrequentTravellersFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;
                AnalyticsUtility.logEventAPIFail(AnalyticsUtility.Events.FREQUENT_TRAVELLERS);
                // GlobalInfoDialog.getInstance(FrequentTravellersFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(FrequentTravellersFragment.this.getContext().getResources().getString(R.string.cant_connect)).show();
                requireActivity().startActivity(new Intent(getActivity(), NoNetActivity.class));

            }
        });
        newItem = false;
    }

    private void loadingDate() {

        List<ModelTravellerDetails> offersList = new ArrayList<>();
        ModelTravellerDetails offer = new ModelTravellerDetails();
        offersList.add(offer);
        offersList.add(offer);
        offersList.add(offer);
        offersList.add(offer);
        offersList.add(offer);
        TravellersAdapter offersAdapter = new TravellersAdapter(FrequentTravellersFragment.this.requireActivity(), offersList, null, true);
        binding.rvTravellers.setLayoutManager(new LinearLayoutManager(FrequentTravellersFragment.this.getContext()));
        binding.rvTravellers.setHasFixedSize(true);
        binding.rvTravellers.setAdapter(offersAdapter);

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
            case R.id.btnSave:
            case R.id.btnAddTraveller:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_ADD_FREQUENT_TRAVELLERS);
                Objects.requireNonNull(getActivity()).getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                        .add(R.id.fullContent, new AddFreqTravelerFragment(onNewItem))
                        .addToBackStack("ChangePasswordFragment")
                        .commit();
                break;
        }


    }

    @Override
    public void onBack() {
        if (getActivity() != null)
            getActivity().getSupportFragmentManager().popBackStack();
    }


    public interface OnNewItem {
        void newData();
    }


}
