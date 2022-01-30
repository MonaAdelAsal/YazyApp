package com.asc.yazy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.asc.yazy.R;
import com.asc.yazy.activity.NoNetActivity;
import com.asc.yazy.adapter.BookingTravellersAdapter;
import com.asc.yazy.adapter.TravellersAdapter;
import com.asc.yazy.api.ApiClient;
import com.asc.yazy.api.ApiInterface;
import com.asc.yazy.api.model.ModelTravellerAPI;
import com.asc.yazy.api.model.ModelTravellerDetails;
import com.asc.yazy.api.model.ModelUser;
import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.core.GlobalInfoDialog;
import com.asc.yazy.databinding.FragmentSelectTravellersBinding;
import com.asc.yazy.utils.AnalyticsUtility;
import com.asc.yazy.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BookingTravellersFragment extends Fragment implements View.OnClickListener {


    public static List<ModelTravellerDetails> selectedListAdult;
    public static List<ModelTravellerDetails> selectedListChild;
    private FragmentSelectTravellersBinding binding;
    private ModelUser user;
    private List<ModelTravellerDetails> list;
    private BookingTravellersAdapter adult;

    public BookingTravellersFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_select_travellers, null, false);
        AnalyticsUtility.logEventOpen(AnalyticsUtility.Events.BOOKING_TRAVELLERS);
        AnalyticsUtility.setScreen(this);
        user = UtilsPreference.getInstance(BookingTravellersFragment.this.getContext()).getUser();
        CheckLanguage checkLanguage = new CheckLanguage(getActivity());
        checkLanguage.UpdateLanguage();
        selectedListAdult = new ArrayList<>();
        selectedListChild = new ArrayList<>();
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadingDate();
        loadData();
    }

    private void loadData() {


        if (user == null || user.getAccess_token() == null || user.getAccess_token().isEmpty())
            return;

        ApiInterface mApiService = ApiClient.getInterfaceService();
        Call<ModelTravellerAPI> call = mApiService.getTravellers("Bearer " + user.getAccess_token(), Constants.getLANGUAGE());
        call.enqueue(new Callback<ModelTravellerAPI>() {
            @Override
            public void onResponse(@NonNull Call<ModelTravellerAPI> call, @NonNull Response<ModelTravellerAPI> response) {
                if (getActivity() == null)
                    return;
                if (BookingTravellersFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;
                if (response.code() != 200) {
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String message = jObjError.getString("message");
                        GlobalInfoDialog.getInstance(BookingTravellersFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(message).show();
                        return;
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                ModelTravellerAPI body = response.body();

                if (body == null) {
                    GlobalInfoDialog.getInstance(BookingTravellersFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(getString(R.string.please_check_your_internet_connection)).show();
                    return;
                }
                if (body.getStatus() == 200 && body.getData() != null && body.getData() != null) {
                    AnalyticsUtility.logEventAPISuccess(AnalyticsUtility.Events.BOOKING_TRAVELLERS);
                    list = new ArrayList<>();
                    ModelTravellerDetails myAccount = new ModelTravellerDetails();
                    myAccount.setSelected(false);
                    myAccount.setType(1);
                    myAccount.setName(user.getName());
                    myAccount.setNationality(user.getNationality());
                    myAccount.setCivil_id(user.getCivil_id());
                    myAccount.setPassport_no(user.getPassport_no());
                    myAccount.setId("myself");
                    list.add(myAccount);
                    list.addAll(body.getData().getAdults());
                    list.addAll(body.getData().getChildren());

                    adult = new BookingTravellersAdapter(BookingTravellersFragment.this.getContext(), list, false);
                    binding.rvTravellers.setLayoutManager(new LinearLayoutManager(BookingTravellersFragment.this.getContext()));
                    binding.rvTravellers.setHasFixedSize(true);
                    binding.rvTravellers.setAdapter(adult);

                    binding.next.setOnClickListener(BookingTravellersFragment.this);


                } else {
                    list = new ArrayList<>();
                    ModelTravellerDetails myAccount = new ModelTravellerDetails();
                    myAccount.setSelected(false);
                    myAccount.setType(1);
                    myAccount.setName(user.getName());
                    myAccount.setNationality(user.getNationality());
                    myAccount.setCivil_id(user.getCivil_id());
                    myAccount.setPassport_no(user.getPassport_no());
                    myAccount.setId("myself");
                    list.add(myAccount);
                    adult = new BookingTravellersAdapter(BookingTravellersFragment.this.getContext(), list, false);
                    binding.rvTravellers.setLayoutManager(new LinearLayoutManager(BookingTravellersFragment.this.getContext()));
                    binding.rvTravellers.setHasFixedSize(true);
                    binding.rvTravellers.setAdapter(adult);

                    binding.next.setOnClickListener(BookingTravellersFragment.this);

                }
            }

            @Override
            public void onFailure(@NonNull Call<ModelTravellerAPI> call, @NonNull Throwable t) {

                list = new ArrayList<>();
                ModelTravellerDetails myAccount = new ModelTravellerDetails();
                myAccount.setSelected(false);
                myAccount.setType(1);
                myAccount.setName(user.getName());
                myAccount.setNationality(user.getNationality());
                myAccount.setCivil_id(user.getCivil_id());
                myAccount.setPassport_no(user.getPassport_no());
                myAccount.setId("myself");
                list.add(myAccount);
                adult = new BookingTravellersAdapter(BookingTravellersFragment.this.getContext(), list, false);
                binding.rvTravellers.setLayoutManager(new LinearLayoutManager(BookingTravellersFragment.this.getContext()));
                binding.rvTravellers.setHasFixedSize(true);
                binding.rvTravellers.setAdapter(adult);

                binding.next.setOnClickListener(BookingTravellersFragment.this);


                if (BookingTravellersFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;
                AnalyticsUtility.logEventAPIFail(AnalyticsUtility.Events.BOOKING_TRAVELLERS);
                //  GlobalInfoDialog.getInstance(BookingTravellersFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(BookingTravellersFragment.this.getContext().getResources().getString(R.string.cant_connect)).show();
                requireActivity().startActivity(new Intent(getActivity(), NoNetActivity.class));


            }
        });

    }

    private void loadingDate() {
        List<ModelTravellerDetails> offersList = new ArrayList<>();
        ModelTravellerDetails offer = new ModelTravellerDetails();
        offersList.add(offer);
        offersList.add(offer);
        offersList.add(offer);
        offersList.add(offer);
        offersList.add(offer);
        TravellersAdapter offersAdapter = new TravellersAdapter(BookingTravellersFragment.this.requireActivity(), offersList, null, true);
        binding.rvTravellers.setLayoutManager(new LinearLayoutManager(BookingTravellersFragment.this.getContext()));
        binding.rvTravellers.setHasFixedSize(true);
        binding.rvTravellers.setAdapter(offersAdapter);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && adult != null && BookingControllerFragment.paymentModel != null) {
            adult.setLimit(BookingControllerFragment.paymentModel.getAdults() + BookingControllerFragment.paymentModel.getChild());
        }
    }

    @Override
    public void onClick(View v) {

        if (v == null)
            return;
        if (v.getId() == R.id.next) {
            selectedListAdult = new ArrayList<>();
            selectedListChild = new ArrayList<>();
            for (ModelTravellerDetails traveller : list) {
                if (traveller.isSelected() && traveller.getType() == 1)
                    selectedListAdult.add(traveller);
                if (traveller.isSelected() && traveller.getType() == 2)
                    selectedListChild.add(traveller);
            }
            AnalyticsUtility.logAction(AnalyticsUtility.Actions.BOOKING_FREQUENT_TRAVELLERS_COUNT);
            BookingControllerFragment.bookingContent.setCurrentItem(2, true);
        }

    }
}
