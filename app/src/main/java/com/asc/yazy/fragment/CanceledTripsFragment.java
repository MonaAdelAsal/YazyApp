package com.asc.yazy.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.asc.yazy.R;
import com.asc.yazy.adapter.BookingAdapter;
import com.asc.yazy.adapter.TripsAdapter;
import com.asc.yazy.api.ApiClient;
import com.asc.yazy.api.ApiInterface;
import com.asc.yazy.api.model.ModelBooking;
import com.asc.yazy.api.model.ModelBookingAPI;
import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.core.GlobalInfoDialog;
import com.asc.yazy.databinding.FragmentMyTripsBinding;
import com.asc.yazy.interfaces.IUpdatableFragment;
import com.asc.yazy.interfaces.OnTripListener;
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


public class CanceledTripsFragment extends Fragment implements View.OnClickListener, IUpdatableFragment, OnTripListener, SwipeRefreshLayout.OnRefreshListener {


    private FragmentMyTripsBinding binding;

    public CanceledTripsFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_my_trips, null, false);
        AnalyticsUtility.logEventOpen(AnalyticsUtility.Events.CONFIRMED_TRIPS);
        AnalyticsUtility.setScreen(this);
        CheckLanguage checkLanguage = new CheckLanguage(getActivity());
        checkLanguage.UpdateLanguage();
        binding.swipeRefreshLayout.setOnRefreshListener(this);
        binding.layoutNoTrips.btnExplore.setOnClickListener(this);
        binding.layoutNoInternet.btnTryAgain.setOnClickListener(this);
        loadingDate();
        loadDate();
        return binding.getRoot();
    }


    private void loadDate() {

        if (getActivity() == null)
            return;

        String token = UtilsPreference.getInstance(getActivity()).getUser().getAccess_token();
        if (token == null || token.isEmpty()) {
            if (!UnAuthorizedFragment.isAlive)
                Objects.requireNonNull(getActivity()).getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fullContent, new UnAuthorizedFragment())
                        .addToBackStack(null)
                        .commit();

            binding.layoutNoTrips.getRoot().setVisibility(View.VISIBLE);
            return;
        }
        ApiInterface mApiService = ApiClient.getInterfaceService();
        Call<ModelBookingAPI> call = mApiService.getBooking("Bearer " + token, Constants.getLANGUAGE());
        call.enqueue(new Callback<ModelBookingAPI>() {
            @Override
            public void onResponse(@NonNull Call<ModelBookingAPI> call, @NonNull Response<ModelBookingAPI> response) {

                if (getActivity() == null)
                    return;
                if (CanceledTripsFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;

                if (response.code() == 401) {
                    if (!UnAuthorizedFragment.isAlive)
                        Objects.requireNonNull(getActivity()).getSupportFragmentManager()
                                .beginTransaction()
                                .add(R.id.fullContent, new UnAuthorizedFragment())
                                .addToBackStack(null)
                                .commit();
                    binding.layoutNoTrips.getRoot().setVisibility(View.VISIBLE);
                    return;
                }

                if (response.code() != 200) {
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String message = jObjError.getString("message");
                        GlobalInfoDialog.getInstance(CanceledTripsFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(message).show();
                        return;
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                ModelBookingAPI body = response.body();

                if (body == null) {
                    GlobalInfoDialog.getInstance(CanceledTripsFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(getString(R.string.please_check_your_internet_connection)).show();
                    return;
                }
                if (body.getStatus() == 200) {
                    AnalyticsUtility.logEventAPISuccess(AnalyticsUtility.Events.CONFIRMED_TRIPS);
                    if (body.getData().getCanceled().size() == 0) {
                        binding.layoutNoTrips.getRoot().setVisibility(View.VISIBLE);
                    } else {
                        BookingAdapter bookingAdapter = new BookingAdapter(CanceledTripsFragment.this.getContext(), body.getData().getCanceled(), CanceledTripsFragment.this, false);
                        binding.rvTrips.setLayoutManager(new LinearLayoutManager(getContext()));
                        binding.rvTrips.setAdapter(bookingAdapter);
                        binding.layoutNoTrips.getRoot().setVisibility(View.GONE);
                        binding.layoutNoInternet.getRoot().setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ModelBookingAPI> call, @NonNull Throwable t) {

                if (CanceledTripsFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;
                AnalyticsUtility.logEventAPIFail(AnalyticsUtility.Events.CONFIRMED_TRIPS);
              //  GlobalInfoDialog.getInstance(CanceledTripsFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(CanceledTripsFragment.this.getContext().getResources().getString(R.string.cant_connect)).show();
                binding.layoutNoTrips.getRoot().setVisibility(View.GONE);
                binding.layoutNoInternet.getRoot().setVisibility(View.VISIBLE);
            }
        });
    }


    private void loadingDate() {

        List<ModelBooking> offersList = new ArrayList<>();
        ModelBooking offer = new ModelBooking();
        offersList.add(offer);
        offersList.add(offer);
        offersList.add(offer);
        offersList.add(offer);
        offersList.add(offer);
        offersList.add(offer);
        TripsAdapter offersAdapter = new TripsAdapter(offersList, true);
        binding.rvTrips.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvTrips.setItemAnimator(new DefaultItemAnimator());
        binding.rvTrips.setHasFixedSize(true);
        binding.rvTrips.setAdapter(offersAdapter);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (v == null)
            return;
        switch (v.getId()) {
            case R.id.layoutNoTrips:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_HOME);
                Objects.requireNonNull(getActivity()).getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                        .add(R.id.container, new Home3Fragment())
                        .addToBackStack("HomeFragment")
                        .commit();
                break;
            case R.id.btnTryAgain:
                loadingDate();
                loadDate();
                break;
            case R.id.btnExplore:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_HOME);
                if (getActivity() == null)
                    return;
                getActivity().finish();
                break;

        }
    }


    @Override
    public void onTripClick(ModelBooking trip) {

        if (getActivity() == null)
            return;
        AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_BOOK_DETAILS);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                .add(R.id.fullContent, new NewTripDetailsFragment(trip))
                .addToBackStack("OfferDetailsFragment")
                .commit();
    }

    @Override
    public void onRefresh() {
        binding.swipeRefreshLayout.setRefreshing(false);
        loadingDate();
        loadDate();

    }

    @Override
    public void onFragmentUpdate() {
        loadingDate();
        loadDate();
    }
}
