package com.asc.yazy.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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
import com.asc.yazy.activity.HesabeActivity;
import com.asc.yazy.activity.NoNetActivity;
import com.asc.yazy.adapter.TripDetailsViewPagerAdapter;
import com.asc.yazy.api.ApiClient;
import com.asc.yazy.api.ApiInterface;
import com.asc.yazy.api.model.ModelBooking;
import com.asc.yazy.api.model.ModelTripDetailAPI;
import com.asc.yazy.api.model.ModelUpdateBookingAPI;
import com.asc.yazy.api.model.TDetailsModel;
import com.asc.yazy.api.pagination.offers.NetworkState;
import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.core.GlobalInfoDialog;
import com.asc.yazy.core.SelectDateDialog;
import com.asc.yazy.databinding.FragmentTripDetailsBinding;
import com.asc.yazy.interfaces.IFragment;
import com.asc.yazy.interfaces.OnCancelCompleted;
import com.asc.yazy.interfaces.OnDateSelectedListener;
import com.asc.yazy.interfaces.OnReschedule;
import com.asc.yazy.utils.AnalyticsUtility;
import com.asc.yazy.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import me.kungfucat.viewpagertransformers.DepthPageTransformer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripDetailsFragment extends Fragment implements View.OnClickListener, IFragment {

    private FragmentTripDetailsBinding binding;
    private ModelBooking trip;
    private TDetailsModel offer;
    private int reschedule = 0;
    private String id;
    private final OnCancelCompleted onCancelCompleted = () -> {
        try {
            //  HomeFragment.goneFlightStatus(id);
            Home3Fragment.goneFlightStatus(id);

        } catch (Exception ignored) {
        }

        if (getActivity() != null)
            getActivity().getSupportFragmentManager().popBackStack();
    };
    private OnReschedule listenerReschedule;
    private final OnDateSelectedListener listener = SelectedDate -> sendDate(SelectedDate, reschedule);

    public TripDetailsFragment() {

    }

    public TripDetailsFragment(ModelBooking trip) {
        this.trip = trip;
        this.id = trip.getBooking_id();
    }

    public TripDetailsFragment(ModelBooking trip, OnReschedule listener) {
        this.listenerReschedule = listener;
        this.trip = trip;
        this.id = trip.getBooking_id();
    }


    public TripDetailsFragment(String id) {
        this.id = id;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_trip_details, null, false);
        AnalyticsUtility.logEventOpen(AnalyticsUtility.Events.BOOK_DETAILS);
        AnalyticsUtility.setScreen(this);
        CheckLanguage checkLanguage = new CheckLanguage(getActivity());
        checkLanguage.UpdateLanguage();
        binding.imgBack.setOnClickListener(this);

        //   binding.loaderAirLine.startShimmerAnimation();
        //   binding.loaderAirLineClass.startShimmerAnimation();
        //   binding.loaderHotel.startShimmerAnimation();
        //   binding.loaderRoom.startShimmerAnimation();
        binding.loaderTravelAgencyName.startShimmerAnimation();
        binding.loaderTripDate.startShimmerAnimation();
        binding.loaderTripDateValue.startShimmerAnimation();


        binding.loaderUserName.startShimmerAnimation();
        binding.loaderUserPhone.startShimmerAnimation();
        binding.loaderTickets.startShimmerAnimation();
        binding.loaderTicketsNumber.startShimmerAnimation();
        binding.loaderTransID.startShimmerAnimation();
        binding.loaderTransIDValue.startShimmerAnimation();
        binding.loaderTotalValue.startShimmerAnimation();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadDate();
    }

    private void sendDate(String date, int reschedule) {
        if (date == null || date.trim().isEmpty())
            return;

        String token = UtilsPreference.getInstance(getActivity()).getUser().getAccess_token();
        if (token == null || token.isEmpty()) {
            Objects.requireNonNull(getActivity()).startActivity(new Intent(getActivity(), AuthenticationActivity.class));
            return;
        }
        ApiInterface mApiService = ApiClient.getInterfaceService();
        Call<ModelUpdateBookingAPI> call = mApiService.SendDateFrom("Bearer " + token, Constants.getLANGUAGE(), id, date, reschedule);
        call.enqueue(new Callback<ModelUpdateBookingAPI>() {
            @Override
            public void onResponse(@NonNull Call<ModelUpdateBookingAPI> call, @NonNull Response<ModelUpdateBookingAPI> response) {

                if (getActivity() == null)
                    return;

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
                        GlobalInfoDialog.getInstance(getActivity()).setTitle(getActivity().getString(R.string.error)).setMessage(message).show();
                        return;
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                ModelUpdateBookingAPI body = response.body();
                if (body == null) {
                    GlobalInfoDialog.getInstance(getActivity()).setTitle(getActivity().getString(R.string.error)).setMessage(getActivity().getString(R.string.please_check_your_internet_connection)).show();
                    return;
                }
                if (body.getStatus() == 200) {
                    Toast.makeText(getContext(), body.getMessage(), Toast.LENGTH_SHORT).show();
                    if (reschedule == 1) {
                        // MainActivity.updateBottomNavigation(0);
                        if (listenerReschedule != null) {
                            listenerReschedule.OnDateReschedule(1);
                        }
                        getActivity().getSupportFragmentManager().popBackStack();
                    } else {
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                    if (MyTripsPagerFragment.basketViewPagerAdapter != null)
                        MyTripsPagerFragment.basketViewPagerAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(@NonNull Call<ModelUpdateBookingAPI> call, @NonNull Throwable t) {

                if (getActivity() == null)
                    return;
                //  GlobalInfoDialog.getInstance(getActivity()).setTitle(getActivity().getString(R.string.error)).setMessage(getActivity().getResources().getString(R.string.cant_connect)).show();
                requireActivity().startActivity(new Intent(getActivity(), NoNetActivity.class));

            }
        });
    }

    private void loadDate() {

        if (getActivity() == null)
            return;

        String token = UtilsPreference.getInstance(getActivity()).getUser().getAccess_token();
        if (token == null || token.isEmpty() || id == null || id.isEmpty()) {
            // getActivity().startActivity(new Intent(getActivity(), AuthenticationActivity.class));
            return;
        }

        ApiInterface mApiService = ApiClient.getInterfaceService();
        Call<ModelTripDetailAPI> call = mApiService.tripDetails("Bearer " + token, Constants.getLANGUAGE(), id);
        call.enqueue(new Callback<ModelTripDetailAPI>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<ModelTripDetailAPI> call, @NonNull Response<ModelTripDetailAPI> response) {

                if (getActivity() == null)
                    return;
                if (TripDetailsFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;

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
                        //GlobalInfoDialog.getInstance(TripDetailsFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(message).show();
                        return;
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                ModelTripDetailAPI body = response.body();

                if (body == null) {
                    GlobalInfoDialog.getInstance(TripDetailsFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(getString(R.string.please_check_your_internet_connection)).show();
                    return;
                }

                if (body.getStatus() == 200 || body.getData() != null) {
                    offer = body.getData();
                    binding.btnSelectDate.setOnClickListener(TripDetailsFragment.this);
                    binding.changeDate.setOnClickListener(TripDetailsFragment.this);
                    binding.btnPay.setOnClickListener(TripDetailsFragment.this);
                    binding.btnReShare.setOnClickListener(TripDetailsFragment.this);
                    binding.btnCancel.setOnClickListener(TripDetailsFragment.this);
                    try {
                        if (offer.getOffer().getMulti_cities() == 1)
                            binding.tvMulti.setVisibility(View.VISIBLE);
                        else
                            binding.tvMulti.setVisibility(View.GONE);
                    } catch (Exception ignored) {
                    }

                    AnalyticsUtility.logEventAPISuccess(AnalyticsUtility.Events.BOOK_DETAILS);
                    //    binding.loaderAirLine.stopShimmerAnimation();
                    //    binding.tvAirLine.setBackground(null);
                    //    binding.loaderAirLineClass.stopShimmerAnimation();
                    //    binding.tvClass.setBackground(null);
                    //    binding.loaderHotel.stopShimmerAnimation();
                    //  binding.tvHotel.setBackground(null);
                    //  binding.loaderRoom.stopShimmerAnimation();
                    //  binding.tvRoomType.setBackground(null);
                    binding.loaderTravelAgencyName.stopShimmerAnimation();
                    binding.tvTravelAgencyName.setBackground(null);
                    binding.tvTripDate.setBackground(null);
                    binding.TripDateValue.setBackground(null);
                    binding.TripDateValue.setText(Constants.getFormattedTripDate(body.getData().getCreated_at()));
                    binding.loaderTripDate.stopShimmerAnimation();
                    binding.loaderTripDateValue.stopShimmerAnimation();
                    binding.loaderUserName.stopShimmerAnimation();
                    binding.tvUsername.setBackground(null);
                    binding.loaderUserPhone.stopShimmerAnimation();
                    binding.tvUserPhone.setBackground(null);
                    binding.loaderTickets.stopShimmerAnimation();
                    binding.tvTickets.setBackground(null);
                    binding.loaderTicketsNumber.stopShimmerAnimation();
                    binding.tvTicketsNumber.setBackground(null);
                    binding.loaderTransID.stopShimmerAnimation();
                    binding.tvTransID.setBackground(null);
                    binding.loaderTransIDValue.stopShimmerAnimation();
                    binding.tvLoaderIDValue.setBackground(null);
                    binding.loaderTotalValue.stopShimmerAnimation();
                    binding.tvTotalValue.setBackground(null);
                    TDetailsModel model = body.getData();
                    binding.setTrip(model);

                    /*
                    if (model.getOffer().getOpen_package() == 1 && model.getStart_date() != null) {
                        binding.tvDate.setText(model.getStart_date());
                        binding.tvDateEnd.setText(model.getEnd_date());
                    } else {
                        binding.tvDate.setText(model.getOffer().getDate_from());
                        binding.tvDateEnd.setText(model.getOffer().getDate_to());
                    }
                     */

                    binding.viewPager.setAdapter(new TripDetailsViewPagerAdapter(model, model.getOffer().getCities(), getChildFragmentManager()));
                    binding.indicatorView.setViewPager(binding.viewPager);
                    binding.viewPager.setPageTransformer(true, new DepthPageTransformer());

                    if (model.getAdults() == 0 || model.getAdults() == 1) {
                        if (model.getChildren() == 0 || model.getChildren() == 1) {
                            binding.tvTicketsNumber.setText(model.getAdults() + " " + Objects.requireNonNull(TripDetailsFragment.this.getContext()).getResources().getString(R.string.adult) + " - " + model.getChildren() + " " + Objects.requireNonNull(TripDetailsFragment.this.getContext()).getResources().getString(R.string.child));
                        } else {
                            binding.tvTicketsNumber.setText(model.getAdults() + " " + Objects.requireNonNull(TripDetailsFragment.this.getContext()).getResources().getString(R.string.adult) + " - " + model.getChildren() + " " + Objects.requireNonNull(TripDetailsFragment.this.getContext()).getResources().getString(R.string.children));
                        }
                    } else {
                        if (model.getChildren() == 0 || model.getChildren() == 1) {
                            binding.tvTicketsNumber.setText(model.getAdults() + " " + Objects.requireNonNull(TripDetailsFragment.this.getContext()).getResources().getString(R.string.adults) + " - " + model.getChildren() + " " + Objects.requireNonNull(TripDetailsFragment.this.getContext()).getResources().getString(R.string.child));
                        } else {
                            binding.tvTicketsNumber.setText(model.getAdults() + " " + Objects.requireNonNull(TripDetailsFragment.this.getContext()).getResources().getString(R.string.adults) + " - " + model.getChildren() + " " + Objects.requireNonNull(TripDetailsFragment.this.getContext()).getResources().getString(R.string.children));
                        }

                    }
                    binding.tvTickets.setText(model.getTickets_number() + " " + Objects.requireNonNull(TripDetailsFragment.this.getContext()).getResources().getString(R.string.tickets));
                    if (UtilsPreference.getInstance(getActivity()).getPreference(Constants.LANGUAGE, "ar").equals("en")) {
                        binding.tvUserPhone.setText((("(" + model.getCountry_code() + ") ") + model.getMobile()));
                    } else {
                        binding.tvUserPhone.setText(((model.getMobile() + "(" + model.getCountry_code() + ") ")));
                    }

                    switch (model.getStatus_mobile()) {

                        case 1:
                            binding.btnSelectDate.setVisibility(View.GONE);
                            if (model.getOffer().getOpen_package() == 1) {
                                binding.changeDate.setVisibility(View.VISIBLE);
                                binding.btnSelectDate.setVisibility(View.GONE);
                                reschedule = 1;
                            } else {
                                binding.btnSelectDate.setVisibility(View.GONE);
                            }
                            binding.bookLayout.setVisibility(View.VISIBLE);
                            break;
                        case 2:
                            binding.btnSelectDate.setVisibility(View.VISIBLE);
                            binding.bookLayout.setVisibility(View.GONE);
                            break;
                        case 3:
                            binding.btnSelectDate.setVisibility(View.GONE);
                            if (model.getOffer().getOpen_package() == 1) {
                                binding.changeDate.setVisibility(View.VISIBLE);
                                binding.btnSelectDate.setVisibility(View.GONE);
                                reschedule = 1;
                            } else {
                                binding.btnSelectDate.setVisibility(View.GONE);
                            }
                            binding.bookLayout.setVisibility(View.GONE);
                            break;
                        case 4:
                            binding.btnSelectDate.setVisibility(View.GONE);
                            binding.changeDate.setVisibility(View.GONE);
                            binding.bookLayout.setVisibility(View.GONE);
                            binding.btnCancel.setText(TripDetailsFragment.this.getContext().getResources().getString(R.string.canceled));
                            binding.btnCancel.setOnClickListener(null);

                            break;
                    }

                } else {
                    GlobalInfoDialog.getInstance(TripDetailsFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(body.getMessage()).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ModelTripDetailAPI> call, @NonNull Throwable t) {

                if (TripDetailsFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;
                AnalyticsUtility.logEventAPIFail(AnalyticsUtility.Events.BOOK_DETAILS);
                // GlobalInfoDialog.getInstance(TripDetailsFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(TripDetailsFragment.this.getContext().getResources().getString(R.string.cant_connect)).show();
                requireActivity().startActivity(new Intent(getActivity(), NoNetActivity.class));


            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (v == null)
            return;

        switch (v.getId()) {
            case R.id.btnReShare:
                if (offer == null)
                    return;
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.RE_SHARE_PAYMENT_LINK);
                if (offer.getGift() == 1 && offer.getPayment_link() != null && !offer.getPayment_link().trim().isEmpty()) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT, offer.getPayment_link());
                    intent.setType("text/plain");
                    try {
                        startActivity(intent);
                    } catch (Exception e) {
                        GlobalInfoDialog.getInstance(TripDetailsFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(getResources().getString(R.string.not_supported_apps)).show();
                    }

                } else {
                    GlobalInfoDialog.getInstance(TripDetailsFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(getResources().getString(R.string.share_link_not_retrived)).show();
                }
                break;
            case R.id.btnPay:
                if (offer == null)
                    return;
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_PAYMENT);
                Intent intent = new Intent(TripDetailsFragment.this.getActivity(), HesabeActivity.class);
                intent.putExtra(Constants.TRANSACTION_OFFER, offer);
                Objects.requireNonNull(TripDetailsFragment.this.getActivity()).startActivity(intent);
                break;
            case R.id.btnSelectDate:
            case R.id.changeDate:
                if (offer == null)
                    return;
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_SELECT_DATE);
                new SelectDateDialog(Objects.requireNonNull(TripDetailsFragment.this.getContext()), listener, offer.getOffer().getDate_from(), offer.getOffer().getExpiration_date());
                break;
            case R.id.imgBack:
                if (getActivity() != null)
                    getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.btnCancel:
                if (trip == null)
                    return;
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.CANCELLATION_POLICY);
                Objects.requireNonNull(getActivity()).getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                        .add(R.id.fullContent, new CancellationPolicyFragment(trip, onCancelCompleted))
                        .addToBackStack("CancellationPolicyFragment")
                        .commit();
                break;
        }
    }


    @Override
    public void onBack() {
        if (getActivity() != null)
            getActivity().getSupportFragmentManager().popBackStack();
    }


}