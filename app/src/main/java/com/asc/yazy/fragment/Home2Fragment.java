package com.asc.yazy.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.asc.yazy.R;
import com.asc.yazy.activity.NoNetActivity;
import com.asc.yazy.adapter.AllOffersAdapter;
import com.asc.yazy.adapter.OffersAdapter;
import com.asc.yazy.adapter.PromotedOffersAdapter;
import com.asc.yazy.api.ApiClient;
import com.asc.yazy.api.ApiInterface;
import com.asc.yazy.api.model.ModelBooking;
import com.asc.yazy.api.model.ModelFlightStatus;
import com.asc.yazy.api.model.ModelOffer;
import com.asc.yazy.api.model.ModelOfferAPI;
import com.asc.yazy.api.model.ModelPromotedOfferAPI;
import com.asc.yazy.api.model.ModelUser;
import com.asc.yazy.api.pagination.PaginationProvider;
import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.cash.room.model.OfferRoomModel;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.core.CongratulationDialog;
import com.asc.yazy.core.GlobalInfoDialog;
import com.asc.yazy.databinding.FragmentHomeBinding;
import com.asc.yazy.interfaces.IUpdatableFragment;
import com.asc.yazy.interfaces.OnOfferItemListener;
import com.asc.yazy.interfaces.OnReschedule;
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


public class Home2Fragment extends Fragment implements IUpdatableFragment, OnOfferItemListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {


    @SuppressLint("StaticFieldLeak")
    public static ViewGroup counterView;
    private static String booking_id = null;
    private FragmentHomeBinding binding;
    private final OnReschedule listenerReschedule = res -> {
        if (res == 1) {
            showFlightStatus();
        }
    };
    private long mLastClickTime = System.currentTimeMillis();


    public Home2Fragment() {
    }

    public static void goneFlightStatus(String id) {
        try {
            if (booking_id.equals(id))
                counterView.setVisibility(View.GONE);
        } catch (Exception ignored) {
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_home, null, false);
        counterView = binding.FlightStatusLin;
        AnalyticsUtility.logEventOpen(AnalyticsUtility.Events.HOME);
        AnalyticsUtility.setScreen(this);
        CheckLanguage checkLanguage = new CheckLanguage(getActivity());
        checkLanguage.UpdateLanguage();
        PaginationProvider.USER_TOKEN = UtilsPreference.getInstance(Home2Fragment.this.getContext()).getUser().getAccess_token();
        Log.d("HomeFragment", "onCreateView: " + PaginationProvider.USER_TOKEN);
        binding.swipeRefreshLayout.setOnRefreshListener(this);
        binding.layoutNoInternet.btnTryAgain.setOnClickListener(this);
        binding.imgGridView.setOnClickListener(this);
        binding.FlightStatusLin.setOnClickListener(this);
        //CheckIfHasDynamicLink();
        CheckIfHasNotificationId();
        return binding.getRoot();
    }

    private void showFlightStatus() {


        if (!isAdded())
            return;

        String token = UtilsPreference.getInstance(Home2Fragment.this.requireContext()).getUser().getAccess_token();
        if (token == null || token.isEmpty()) {
            binding.FlightStatusLin.setVisibility(View.GONE);
            return;
        }
        ApiInterface mApiService = ApiClient.getInterfaceService();
        Call<ModelFlightStatus> call = mApiService.GetFlightStatus("Bearer " + token, Constants.getLANGUAGE());
        call.enqueue(new Callback<ModelFlightStatus>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<ModelFlightStatus> call, @NonNull Response<ModelFlightStatus> response) {
                binding.FlightStatusLin.setVisibility(View.GONE);
                if (getActivity() == null)
                    return;
                if (!isAdded())
                    return;

                if (response.code() != 200) {
                    return;
                }

                ModelFlightStatus body = response.body();

                if (body == null) {
                    return;
                }
                if (body.getStatus() == 200 && body.getData() != null) {


                    int progress = Integer.parseInt(body.getData().getDays());
                    booking_id = body.getData().getBooking_id();
                    if (progress == 0) {
                        binding.FlightStatusLin.setVisibility(View.VISIBLE);
                        binding.FlightStatusLin.setBackgroundResource(R.drawable.light_blue);
                        binding.layoutCounter.setVisibility(View.GONE);
                        binding.progress.setVisibility(View.GONE);
                        binding.imgPlane.setVisibility(View.VISIBLE);
                        binding.title.setText(Home2Fragment.this.requireContext().getResources().getString(R.string.get_ready));
                        binding.topTitleTv.setText(Home2Fragment.this.requireContext().getResources().getString(R.string.its_today));
                    } else if (progress <= 3) {
                        binding.imgPlane.setVisibility(View.GONE);
                        binding.layoutCounter.setVisibility(View.VISIBLE);
                        binding.progress.setVisibility(View.VISIBLE);
                        binding.FlightStatusLin.setBackgroundResource(R.drawable.light_green);
                        binding.tvDays.setTextColor(Home2Fragment.this.requireContext().getResources().getColor(R.color.lightgreen));
                        binding.tvLabel.setTextColor(Home2Fragment.this.requireContext().getResources().getColor(R.color.lightgreen));

                        binding.FlightStatusLin.setVisibility(View.VISIBLE);
                        binding.title.setText(body.getData().getBooking_title());
                        binding.tvDays.setText(getCelleDate(String.valueOf(progress)));
                        int daysRemind = 8 - progress;
                        binding.progress.setProgress(daysRemind);
                        binding.progress.setFinishedStrokeColor(Color.parseColor(body.getData().getColor()));
                        if (progress == 1)
                            binding.tvLabel.setText(Home2Fragment.this.getResources().getString(R.string.day));
                        else
                            binding.tvLabel.setText(Home2Fragment.this.getResources().getString(R.string.days));

                    } else if (progress <= 5) {

                        binding.imgPlane.setVisibility(View.GONE);
                        binding.layoutCounter.setVisibility(View.VISIBLE);
                        binding.progress.setVisibility(View.VISIBLE);
                        binding.FlightStatusLin.setBackgroundResource(R.drawable.light_yellow);
                        binding.tvDays.setTextColor(Home2Fragment.this.requireContext().getResources().getColor(R.color.lightyellow));
                        binding.tvLabel.setTextColor(Home2Fragment.this.requireContext().getResources().getColor(R.color.lightyellow));
                        binding.tvLabel.setText(Home2Fragment.this.getResources().getString(R.string.days));
                        binding.FlightStatusLin.setVisibility(View.VISIBLE);
                        binding.title.setText(body.getData().getBooking_title());
                        binding.tvDays.setText(getCelleDate(String.valueOf(progress)));
                        int daysRemind = 8 - progress;
                        binding.progress.setProgress(daysRemind);
                        binding.progress.setFinishedStrokeColor(Color.parseColor(body.getData().getColor()));

                    } else if (progress <= 7) {

                        binding.imgPlane.setVisibility(View.GONE);
                        binding.layoutCounter.setVisibility(View.VISIBLE);
                        binding.progress.setVisibility(View.VISIBLE);
                        binding.FlightStatusLin.setBackgroundResource(R.drawable.light_red);
                        binding.tvDays.setTextColor(Home2Fragment.this.requireContext().getResources().getColor(R.color.colorAccent));
                        binding.tvLabel.setTextColor(Home2Fragment.this.requireContext().getResources().getColor(R.color.colorAccent));
                        binding.tvLabel.setText(Home2Fragment.this.getResources().getString(R.string.days));

                        binding.FlightStatusLin.setVisibility(View.VISIBLE);
                        binding.title.setText(body.getData().getBooking_title());
                        binding.tvDays.setText(getCelleDate(String.valueOf(progress)));
                        int daysRemind = 8 - progress;
                        binding.progress.setProgress(daysRemind);
                        binding.progress.setFinishedStrokeColor(Color.parseColor(body.getData().getColor()));
                    } else {
                        binding.FlightStatusLin.setVisibility(View.GONE);
                    }


                }
            }

            @Override
            public void onFailure(@NonNull Call<ModelFlightStatus> call, @NonNull Throwable t) {

                if (!isAdded())
                    return;
                binding.FlightStatusLin.setVisibility(View.GONE);
                AnalyticsUtility.logEventAPIFail(AnalyticsUtility.Events.HOME);

            }
        });
    }

    private String getCelleDate(String days) {
        switch (days) {
            case "0":
                return "00";
            case "1":
                return "01";
            case "2":
                return "02";
            case "3":
                return "03";
            case "4":
                return "04";
            case "5":
                return "05";
            case "6":
                return "06";
            case "7":
                return "07";
            case "8":
                return "08";
            case "9":
                return "09";
            default:
                return days;
        }

    }

    private void CheckIfHasNotificationId() {
        Intent intent = new Intent();
        String id = intent.getStringExtra("orderID");
        Log.d("NotificationOrderID", id + "");
        if (id != null) {
            ModelOffer offer = new ModelOffer();
            AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_OFFER_DETAILS);
            offer.setId(id);
            Objects.requireNonNull(getActivity()).getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                    .add(R.id.fullContent, new OfferDetailsFragment(offer))
                    .addToBackStack("OfferDetailsFragment")
                    .commit();
        }
    }

    private void loadingDate() {

        List<ModelOffer> offersList = new ArrayList<>();
        ModelOffer offer = new ModelOffer();
        offersList.add(offer);
        offersList.add(offer);
        offersList.add(offer);
        PromotedOffersAdapter promotedOffersAdapter = new PromotedOffersAdapter(Home2Fragment.this.getContext(), offersList, null, true);
        binding.rvPromotedOffers.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        binding.rvPromotedOffers.setAdapter(promotedOffersAdapter);
        binding.rvPromotedOffers.setVisibility(View.VISIBLE);
        binding.tvPromoted.setVisibility(View.VISIBLE);

        OffersAdapter offersAdapter = new OffersAdapter(offersList, null, true);
        binding.rvOffers.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvOffers.setAdapter(offersAdapter);

        binding.layoutNoOffers.getRoot().setVisibility(View.GONE);
        binding.layoutNoInternet.getRoot().setVisibility(View.GONE);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String points = UtilsPreference.getInstance(requireContext()).getPreference(Constants.REGISTRATION_POINTS, null);
        if (points != null) {
            CongratulationDialog.getInstance(Home2Fragment.this.requireContext()).setTitle(getString(R.string.Congratulations)).setMessage(points).show();
            UtilsPreference.getInstance(requireContext()).savePreference(Constants.REGISTRATION_POINTS, null);
        }
        if (Constants.isNetworkAvailable(Home2Fragment.this.getContext())) {
            loadingDate();
            loadPagedDate();
            showFlightStatus();
            //loadPromotedOffers();
        } else {
            binding.layoutNoOffers.getRoot().setVisibility(View.GONE);
            binding.layoutNoInternet.getRoot().setVisibility(View.VISIBLE);
        }


    }

    private void loadPagedDate() {


        ModelUser user = UtilsPreference.getInstance(Home2Fragment.this.getContext()).getUser();

        if (user == null || user.getAccess_token() == null || user.getAccess_token().isEmpty()) {
            binding.rvPromotedOffers.setVisibility(View.GONE);
            binding.tvPromoted.setVisibility(View.GONE);
            //  return;
        }


        ApiInterface mApiService = ApiClient.getInterfaceService();
        Call<ModelOfferAPI> call = mApiService.getOffersNoPagination("Bearer " + (user != null ? user.getAccess_token() : null), Constants.getLANGUAGE());
        call.enqueue(new Callback<ModelOfferAPI>() {
            @Override
            public void onResponse(@NonNull Call<ModelOfferAPI> call, @NonNull Response<ModelOfferAPI> response) {
                if (getActivity() == null)
                    return;
                if (Home2Fragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;
                if (response.code() != 200) {
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String message = jObjError.getString("message");
                        GlobalInfoDialog.getInstance(Home2Fragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(message).show();
                        return;
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                ModelOfferAPI body = response.body();

                if (body == null) {
                    GlobalInfoDialog.getInstance(Home2Fragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(getString(R.string.please_check_your_internet_connection)).show();
                    return;
                }
                if (body.getStatus() == 200 && body.getData() != null && body.getData() != null) {
                    AllOffersAdapter allStoresPagedAdapter = new AllOffersAdapter(Home2Fragment.this.requireContext(), body.getData(), Home2Fragment.this, false);
                    binding.rvOffers.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.layoutNoOffers.getRoot().setVisibility(View.GONE);
                    binding.layoutNoInternet.getRoot().setVisibility(View.GONE);
                    binding.rvOffers.setAdapter(allStoresPagedAdapter);
                } else {
                    GlobalInfoDialog.getInstance(Home2Fragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(body.getMessage()).show();
                }

                if (body.getStatus() == 200 && body.getPromotedOffers() != null && body.getData() != null) {
                    AnalyticsUtility.logEventAPISuccess(AnalyticsUtility.Events.FREQUENT_TRAVELLERS);
                    PromotedOffersAdapter promotedOffersAdapter = new PromotedOffersAdapter(Home2Fragment.this.getContext(), body.getPromotedOffers(), Home2Fragment.this, false);
                    binding.rvPromotedOffers.setLayoutManager(new LinearLayoutManager(Home2Fragment.this.getContext(), RecyclerView.HORIZONTAL, false));

                    binding.rvPromotedOffers.setAdapter(promotedOffersAdapter);
                    binding.rvPromotedOffers.setVisibility(View.VISIBLE);
                    binding.tvPromoted.setVisibility(View.VISIBLE);

                } else {
                    binding.rvPromotedOffers.setVisibility(View.GONE);
                    binding.tvPromoted.setVisibility(View.GONE);
                    GlobalInfoDialog.getInstance(Home2Fragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(body.getMessage()).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ModelOfferAPI> call, @NonNull Throwable t) {

                if (Home2Fragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;
                AnalyticsUtility.logEventAPIFail(AnalyticsUtility.Events.FREQUENT_TRAVELLERS);
                //GlobalInfoDialog.getInstance(Home2Fragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(Home2Fragment.this.getContext().getResources().getString(R.string.cant_connect)).show();
                requireActivity().startActivity(new Intent(getActivity(), NoNetActivity.class));

            }
        });

    }


    private void loadPromotedOffers() {

        ModelUser user = UtilsPreference.getInstance(Home2Fragment.this.getContext()).getUser();
        if (user == null || user.getAccess_token() == null || user.getAccess_token().isEmpty()) {
            binding.rvPromotedOffers.setVisibility(View.GONE);
            binding.tvPromoted.setVisibility(View.GONE);
            //  return;
        }

        ApiInterface mApiService = ApiClient.getInterfaceService();
        Call<ModelPromotedOfferAPI> call = mApiService.getPromotedOffers("Bearer " + (user != null ? user.getAccess_token() : null), Constants.getLANGUAGE());
        call.enqueue(new Callback<ModelPromotedOfferAPI>() {
            @Override
            public void onResponse(@NonNull Call<ModelPromotedOfferAPI> call, @NonNull Response<ModelPromotedOfferAPI> response) {
                if (getActivity() == null)
                    return;
                if (Home2Fragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;
                if (response.code() != 200) {
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String message = jObjError.getString("message");
                        GlobalInfoDialog.getInstance(Home2Fragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(message).show();
                        return;
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                ModelPromotedOfferAPI body = response.body();

                if (body == null) {
                    GlobalInfoDialog.getInstance(Home2Fragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(getString(R.string.please_check_your_internet_connection)).show();
                    return;
                }
                if (body.getStatus() == 200 && body.getData() != null && body.getData() != null) {
                    AnalyticsUtility.logEventAPISuccess(AnalyticsUtility.Events.FREQUENT_TRAVELLERS);
                    PromotedOffersAdapter promotedOffersAdapter = new PromotedOffersAdapter(Home2Fragment.this.getContext(), body.getData(), Home2Fragment.this, false);
                    binding.rvPromotedOffers.setLayoutManager(new LinearLayoutManager(Home2Fragment.this.getContext(), RecyclerView.HORIZONTAL, false));

                    binding.rvPromotedOffers.setAdapter(promotedOffersAdapter);
                    binding.rvPromotedOffers.setVisibility(View.VISIBLE);
                    binding.tvPromoted.setVisibility(View.VISIBLE);

                } else {
                    binding.rvPromotedOffers.setVisibility(View.GONE);
                    binding.tvPromoted.setVisibility(View.GONE);
                    GlobalInfoDialog.getInstance(Home2Fragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(body.getMessage()).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ModelPromotedOfferAPI> call, @NonNull Throwable t) {

                if (Home2Fragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;
                AnalyticsUtility.logEventAPIFail(AnalyticsUtility.Events.FREQUENT_TRAVELLERS);
                //GlobalInfoDialog.getInstance(Home2Fragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(Home2Fragment.this.getContext().getResources().getString(R.string.cant_connect)).show();
                requireActivity().startActivity(new Intent(getActivity(), NoNetActivity.class));


            }
        });

    }


    @Override
    public void OnOfferClicked(ModelOffer offer) {

        if (getActivity() == null)
            return;

        if (offer == null)
            return;
        long now = System.currentTimeMillis();
        if (now - mLastClickTime < Constants.CLICK_TIME_INTERVAL) {
            return;
        }
        mLastClickTime = now;

        if (offer.getMulti_cities() == 1) {
            AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_MULTI_CITIES_OFFER_DETAILS);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                    .add(R.id.fullContent, new MultiOfferDetailsFragment(offer))
                    .addToBackStack("OfferDetailsFragment")
                    .commit();
        } else {
            AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_OFFER_DETAILS);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                    .add(R.id.fullContent, new OfferDetailsFragment(offer))
                    .addToBackStack("OfferDetailsFragment")
                    .commit();
        }
    }

    @Override
    public void OnCashedOfferClicked(OfferRoomModel offer) {

        if (getActivity() == null)
            return;

        long now = System.currentTimeMillis();
        if (now - mLastClickTime < Constants.CLICK_TIME_INTERVAL) {
            return;
        }
        mLastClickTime = now;

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                .add(R.id.fullContent, new OfferDetailsFragment(offer))
                .addToBackStack("OfferDetailsFragment")
                .commit();
    }


    @Override
    public void onRefresh() {
        if (Constants.isNetworkAvailable(Home2Fragment.this.getContext())) {
            loadingDate();
            loadPagedDate();
            showFlightStatus();
            //  loadPromotedOffers();
        } else {
            binding.layoutNoOffers.getRoot().setVisibility(View.GONE);
            binding.layoutNoInternet.getRoot().setVisibility(View.VISIBLE);
        }
        binding.swipeRefreshLayout.setRefreshing(false);

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onClick(View v) {
        if (v == null)
            return;
        long now = System.currentTimeMillis();
        if (now - mLastClickTime < Constants.CLICK_TIME_INTERVAL) {
            return;
        }
        mLastClickTime = now;
        if (v == binding.layoutNoInternet.btnTryAgain) {
            if (Constants.isNetworkAvailable(Home2Fragment.this.getContext())) {
                loadingDate();
                loadPagedDate();
                showFlightStatus();
                //    loadPromotedOffers();
            } else {
                binding.layoutNoOffers.getRoot().setVisibility(View.GONE);
                binding.layoutNoInternet.getRoot().setVisibility(View.VISIBLE);
            }
        } else if (v == binding.FlightStatusLin) {
            if (getActivity() == null)
                return;
            ModelBooking trip = new ModelBooking();
            trip.setBooking_id(booking_id);
            AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_BOOK_DETAILS);

            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                    .add(R.id.fullContent, new NewTripDetailsFragment(trip, listenerReschedule))
                    .addToBackStack("OfferDetailsFragment")
                    .commit();
        }
    }

    @Override
    public void onFragmentUpdate() {
        showFlightStatus();
        loadPagedDate();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            showFlightStatus();
        }

    }

}