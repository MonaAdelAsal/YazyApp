package com.asc.yazy.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.asc.yazy.R;
import com.asc.yazy.activity.AuthenticationActivity;
import com.asc.yazy.activity.HesabeActivity;
import com.asc.yazy.activity.NoNetActivity;
import com.asc.yazy.adapter.TripCityDetailsAdapter;
import com.asc.yazy.api.ApiClient;
import com.asc.yazy.api.ApiInterface;
import com.asc.yazy.api.model.City;
import com.asc.yazy.api.model.ModelBooking;
import com.asc.yazy.api.model.ModelTripDetailAPI;
import com.asc.yazy.api.model.ModelUpdateBookingAPI;
import com.asc.yazy.api.model.TDetailsModel;
import com.asc.yazy.api.pagination.offers.NetworkState;
import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.core.GlobalInfoDialog;
import com.asc.yazy.core.ModelDetails;
import com.asc.yazy.core.SelectDateDialog;
import com.asc.yazy.databinding.FragmentNewTripBinding;
import com.asc.yazy.databinding.FullScreenImgBinding;
import com.asc.yazy.interfaces.IFragment;
import com.asc.yazy.interfaces.OnCancelCompleted;
import com.asc.yazy.interfaces.OnDateSelectedListener;
import com.asc.yazy.interfaces.OnReschedule;
import com.asc.yazy.utils.AnalyticsUtility;
import com.asc.yazy.utils.Constants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewTripDetailsFragment extends Fragment implements View.OnClickListener, IFragment {

    private FragmentNewTripBinding binding;
    private ModelBooking trip;
    private TDetailsModel offer;
    private int reschedule = 0;
    private String id, ticket;
    private OnReschedule listenerReschedule;
    private final OnDateSelectedListener listener = SelectedDate -> sendDate(SelectedDate, reschedule);

    private final OnCancelCompleted onCancelCompleted = () -> {
        try {
            Home3Fragment.goneFlightStatus(id);
            //HomeFragment.goneFlightStatus(id);
            listenerReschedule.OnDateReschedule(1);

        } catch (Exception ignored) {
        }

        if (getActivity() != null)
            getActivity().getSupportFragmentManager().popBackStack();
    };

    public NewTripDetailsFragment() {

    }

    public NewTripDetailsFragment(ModelBooking trip) {
        this.trip = trip;
        this.id = trip.getBooking_id();
    }

    public NewTripDetailsFragment(ModelBooking trip, OnReschedule listener) {
        this.listenerReschedule = listener;
        this.trip = trip;
        this.id = trip.getBooking_id();
    }


    public NewTripDetailsFragment(String id) {
        this.id = id;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_new__trip, null, false);
        AnalyticsUtility.logEventOpen(AnalyticsUtility.Events.BOOK_DETAILS);
        AnalyticsUtility.setScreen(this);
        CheckLanguage checkLanguage = new CheckLanguage(getActivity());
        checkLanguage.UpdateLanguage();
        binding.imgBack.setOnClickListener(this);
        binding.btnShowTicket.setOnClickListener(this);
        //   binding.loaderAirLine.startShimmerAnimation();
        //   binding.loaderAirLineClass.startShimmerAnimation();
        //   binding.loaderHotel.startShimmerAnimation();
        //   binding.loaderRoom.startShimmerAnimation();
        loadingDate();
        return binding.getRoot();
    }

    private void loadingDate() {
        List<ModelDetails> flights = new ArrayList<>();
        flights.add(new ModelDetails("", ""));
        TripCityDetailsAdapter adapter = new TripCityDetailsAdapter(flights, true);
        binding.rvFlights.setLayoutManager(new LinearLayoutManager(NewTripDetailsFragment.this.getContext()));
        binding.rvFlights.setHasFixedSize(true);
        binding.rvFlights.setAdapter(adapter);
        binding.rvHotel.setLayoutManager(new LinearLayoutManager(NewTripDetailsFragment.this.getContext()));
        binding.rvHotel.setHasFixedSize(true);
        binding.rvHotel.setAdapter(adapter);


        binding.loaderTitle.startShimmerAnimation();
        binding.loaderTripDate.startShimmerAnimation();
        binding.loaderTripDateValue.startShimmerAnimation();


        binding.loaderTravelAgency.startShimmerAnimation();
        binding.loaderUserName.startShimmerAnimation();
        binding.loaderUserPhone.startShimmerAnimation();
        binding.loaderTickets.startShimmerAnimation();
        binding.loaderTicketsNumber.startShimmerAnimation();
        binding.loaderTransID.startShimmerAnimation();
        binding.loaderTransIDValue.startShimmerAnimation();
        binding.loaderTotalValue.startShimmerAnimation();
        binding.loaderDate.startShimmerAnimation();
        binding.loaderCities.startShimmerAnimation();
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

        ProgressDialog dialog = new ProgressDialog(requireContext());
        dialog.show();

        ApiInterface mApiService = ApiClient.getInterfaceService();
        Call<ModelUpdateBookingAPI> call = mApiService.SendDateFrom("Bearer " + token, Constants.getLANGUAGE(), id, date, reschedule);
        call.enqueue(new Callback<ModelUpdateBookingAPI>() {
            @Override
            public void onResponse(@NonNull Call<ModelUpdateBookingAPI> call, @NonNull Response<ModelUpdateBookingAPI> response) {

                if (getActivity() == null)
                    return;

                dialog.dismiss();

                if (response.code() == 401) {
                    Intent intent = new Intent(getActivity(), AuthenticationActivity.class);
                    intent.putExtra(Constants.TRANSACTION, NetworkState.UN_AUTHORISE);
                    getActivity().startActivity(intent);
                    getActivity().getSupportFragmentManager().popBackStack();
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
                    //        MyServiceSyncAdapter.initializeSyncAdapter(NewTripDetailsFragment.this.requireContext());
                    //        MyServiceSyncAdapter.syncImmediately(NewTripDetailsFragment.this.requireContext());
                    Toast.makeText(getContext(), body.getMessage(), Toast.LENGTH_SHORT).show();
                    if (Integer.parseInt(body.getData().getPoints()) != 0) {
                        GlobalInfoDialog.getInstance(getContext()).setTitle(getString(R.string.Congratulations)).setMessage(getString(R.string.you_got) + " " + body.getData().getPoints() + " " + getString(R.string.won_points)).show();
                    }
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
                dialog.dismiss();
                // GlobalInfoDialog.getInstance(getActivity()).setTitle(getActivity().getString(R.string.error)).setMessage(getActivity().getResources().getString(R.string.cant_connect)).show();
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
                if (NewTripDetailsFragment.this.getContext() == null)
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
                        GlobalInfoDialog.getInstance(NewTripDetailsFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(message).show();
                        getActivity().getSupportFragmentManager().popBackStack();
                        return;
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                ModelTripDetailAPI body = response.body();

                if (body == null) {
                    GlobalInfoDialog.getInstance(NewTripDetailsFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(getString(R.string.please_check_your_internet_connection)).show();
                    return;
                }

                if (body.getStatus() == 200 || body.getData() != null) {
                    offer = body.getData();
                    binding.btnSelectDate.setOnClickListener(NewTripDetailsFragment.this);
                    binding.changeDate.setOnClickListener(NewTripDetailsFragment.this);
                    binding.btnPay.setOnClickListener(NewTripDetailsFragment.this);
                    binding.btnReShare.setOnClickListener(NewTripDetailsFragment.this);
                    binding.btnCancel.setOnClickListener(NewTripDetailsFragment.this);
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
                    binding.loaderTitle.stopShimmerAnimation();
                    binding.tvTitleName.setBackground(null);
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
                    binding.loaderDate.stopShimmerAnimation();
                    binding.tvDate.setBackground(null);
                    binding.loaderCities.stopShimmerAnimation();
                    binding.tvCities.setBackground(null);
                    binding.loaderTravelAgency.stopShimmerAnimation();
                    binding.tvTravelAgencyName.setBackground(null);
                    TDetailsModel model = body.getData();
                    binding.setTrip(model);
                    ticket = model.getTicket();
                    if (model.getOffer().getImage() != null && !model.getOffer().getImage().trim().isEmpty())
                        Glide.with(NewTripDetailsFragment.this.requireContext()).asBitmap().load(model.getOffer().getImage()).diskCacheStrategy(DiskCacheStrategy.DATA).placeholder(R.color.gray).into(binding.img);
                    else
                        binding.img.setImageResource(R.color.gray);


                    if (model.getOffer().getOpen_package() == 1) {
                        if (model.getStart_date() == null || model.getStart_date().isEmpty()) {
                            binding.tvDate.setText(getResources().getString(R.string.not_selected_yet));
                            binding.btnSelectDate.setVisibility(View.VISIBLE);
                        } else {
                            binding.tvDate.setText(Constants.getFormattedDate(model.getStart_date(), model.getEnd_date()));
                            binding.btnSelectDate.setVisibility(View.GONE);
                        }
                    } else {
                        binding.tvDate.setText(Constants.getFormattedDate(body.getData().getOffer()));
                        binding.btnSelectDate.setVisibility(View.GONE);
                    }

                    binding.tvTravelAgencyName.setText((model.getOffer().getTravel_agency_name() + ""));
                    binding.tvRate.setText((model.getOffer().getTravel_agency_rate() + ""));

                    /*
                    if (model.getOffer().getOpen_package() == 1 && model.getStart_date() != null) {
                        binding.tvDate.setText(model.getStart_date());
                        binding.tvDateEnd.setText(model.getEnd_date());
                    } else {
                        binding.tvDate.setText(model.getOffer().getDate_from());
                        binding.tvDateEnd.setText(model.getOffer().getDate_to());
                    }
                     */

                    try {
                        if (model.getOffer().getCities().size() == 1) {
                            binding.tvFlight.setText(NewTripDetailsFragment.this.getString(R.string.flight));
                            binding.tvHotel.setText(NewTripDetailsFragment.this.getString(R.string.hotel));
                        }
                    } catch (Exception ignored) {
                    }
                    try {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (City city : model.getOffer().getCities()) {
                            stringBuilder.append(city.getCity_name());
                            stringBuilder.append(" - ");
                        }
                        stringBuilder.delete(stringBuilder.lastIndexOf(" -"), stringBuilder.length());
                        binding.tvCities.setText(stringBuilder.toString());

                    } catch (Exception e) {
                        binding.tvCities.setText(model.getOffer().getTravel_agency_name());
                    }

                    try {
                        List<ModelDetails> flights = new ArrayList<>();
                        for (City city : model.getOffer().getCities()) {
                            ModelDetails details = new ModelDetails(city.getAirlines(), city.getFlight_class());
                            flights.add(details);
                        }
                        TripCityDetailsAdapter adapter = new TripCityDetailsAdapter(flights, false);
                        binding.rvFlights.setLayoutManager(new LinearLayoutManager(NewTripDetailsFragment.this.getContext()));
                        binding.rvFlights.setHasFixedSize(true);
                        binding.rvFlights.setAdapter(adapter);
                    } catch (Exception ignored) {
                    }


                    try {
                        List<ModelDetails> hotels = new ArrayList<>();
                        for (City city : model.getOffer().getCities()) {
                            ModelDetails details = new ModelDetails(city.getHotel(), city.getHotel_room_type());
                            hotels.add(details);
                        }
                        TripCityDetailsAdapter adapter = new TripCityDetailsAdapter(hotels, false);
                        binding.rvHotel.setLayoutManager(new LinearLayoutManager(NewTripDetailsFragment.this.getContext()));
                        binding.rvHotel.setHasFixedSize(true);
                        binding.rvHotel.setAdapter(adapter);
                    } catch (Exception ignored) {
                    }


                    if (model.getAdults() == 0 || model.getAdults() == 1) {
                        if (model.getChildren() == 0 || model.getChildren() == 1) {
                            binding.tvTicketsNumber.setText(model.getAdults() + " " + Objects.requireNonNull(NewTripDetailsFragment.this.getContext()).getResources().getString(R.string.adult) + " - " + model.getChildren() + " " + Objects.requireNonNull(NewTripDetailsFragment.this.getContext()).getResources().getString(R.string.child));
                        } else {
                            binding.tvTicketsNumber.setText(model.getAdults() + " " + Objects.requireNonNull(NewTripDetailsFragment.this.getContext()).getResources().getString(R.string.adult) + " - " + model.getChildren() + " " + Objects.requireNonNull(NewTripDetailsFragment.this.getContext()).getResources().getString(R.string.children));
                        }
                    } else {
                        if (model.getChildren() == 0 || model.getChildren() == 1) {
                            binding.tvTicketsNumber.setText(model.getAdults() + " " + Objects.requireNonNull(NewTripDetailsFragment.this.getContext()).getResources().getString(R.string.adults) + " - " + model.getChildren() + " " + Objects.requireNonNull(NewTripDetailsFragment.this.getContext()).getResources().getString(R.string.child));
                        } else {
                            binding.tvTicketsNumber.setText(model.getAdults() + " " + Objects.requireNonNull(NewTripDetailsFragment.this.getContext()).getResources().getString(R.string.adults) + " - " + model.getChildren() + " " + Objects.requireNonNull(NewTripDetailsFragment.this.getContext()).getResources().getString(R.string.children));
                        }

                    }
                    binding.tvTickets.setText(model.getTickets_number() + " " + Objects.requireNonNull(NewTripDetailsFragment.this.getContext()).getResources().getString(R.string.tickets));
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
                            binding.bookLayout.setVisibility(View.GONE);
                            binding.btnCancel.setText(R.string.reservation_progress);
                            if (model.getOffer().getOpen_package() == 1) {
                                binding.changeDate.setVisibility(View.VISIBLE);
                                binding.btnSelectDate.setVisibility(View.GONE);
                                reschedule = 1;
                            } else {
                                binding.btnSelectDate.setVisibility(View.GONE);
                            }
                            break;
                        case 4:
                        case 5:
                            binding.btnSelectDate.setVisibility(View.GONE);
                            binding.changeDate.setVisibility(View.INVISIBLE);
                            binding.bookLayout.setVisibility(View.GONE);
                            binding.btnCancel.setText(R.string.canceled);
                            binding.btnCancel.setOnClickListener(null);
                            break;
                        case 6:
                            binding.bookLayout.setVisibility(View.GONE);
                            if (model.getOffer().getOpen_package() == 1) {
                                binding.changeDate.setVisibility(View.VISIBLE);
                                binding.btnSelectDate.setVisibility(View.GONE);
                                reschedule = 1;
                            } else {
                                binding.btnSelectDate.setVisibility(View.GONE);
                            }
                            break;
                        case 7:
                            binding.btnShowTicket.setVisibility(View.VISIBLE);
                            binding.bookLayout.setVisibility(View.GONE);
                            binding.btnCancel.setVisibility(View.GONE);
                            binding.changeDate.setVisibility(View.GONE);
                            binding.btnSelectDate.setVisibility(View.GONE);
                            break;
                    }

                } else {
                    GlobalInfoDialog.getInstance(NewTripDetailsFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(body.getMessage()).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ModelTripDetailAPI> call, @NonNull Throwable t) {

                if (NewTripDetailsFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;
                AnalyticsUtility.logEventAPIFail(AnalyticsUtility.Events.BOOK_DETAILS);
                // GlobalInfoDialog.getInstance(NewTripDetailsFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(NewTripDetailsFragment.this.getContext().getResources().getString(R.string.cant_connect)).show();
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
                        GlobalInfoDialog.getInstance(NewTripDetailsFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(getResources().getString(R.string.not_supported_apps)).show();
                    }

                } else {
                    GlobalInfoDialog.getInstance(NewTripDetailsFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(getResources().getString(R.string.share_link_not_retrived)).show();
                }
                break;
            case R.id.btnPay:
                if (offer == null)
                    return;
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_PAYMENT);
                Intent intent = new Intent(NewTripDetailsFragment.this.getActivity(), HesabeActivity.class);
                intent.putExtra(Constants.TRANSACTION_OFFER, offer);
                Objects.requireNonNull(NewTripDetailsFragment.this.getActivity()).startActivity(intent);
                break;
            case R.id.btnSelectDate:
            case R.id.changeDate:
                if (offer == null)
                    return;
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_SELECT_DATE);
                new SelectDateDialog(Objects.requireNonNull(NewTripDetailsFragment.this.getContext()), listener, offer.getOffer().getDate_from(), offer.getOffer().getExpiration_date());
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
            case R.id.btnShowTicket:
                Dialog nagDialog = new Dialog(Objects.requireNonNull(getActivity()), android.R.style.Theme_Dialog);
                nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                FullScreenImgBinding DialogBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.full_screen_img, null, false);
                nagDialog.setContentView(DialogBinding.getRoot());
                if (nagDialog.getWindow() != null)
                    nagDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                if (ticket != null && !ticket.trim().isEmpty()) {
                    DialogBinding.loader.startShimmerAnimation();
                    Glide.with(getActivity()).asBitmap().load(ticket).diskCacheStrategy(DiskCacheStrategy.DATA).placeholder(R.color.gray)
                            .listener(new RequestListener<Bitmap>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                    DialogBinding.loader.stopShimmerAnimation();
                                    nagDialog.dismiss();
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                    DialogBinding.loader.stopShimmerAnimation();
                                    ViewGroup.LayoutParams params = DialogBinding.loader.getLayoutParams();
                                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                                    params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                                    DialogBinding.loader.setLayoutParams(params);
                                    return false;
                                }
                            }).into(DialogBinding.photoView);
                    nagDialog.show();
                } else
                    GlobalInfoDialog.getInstance(getActivity()).setTitle(getActivity().getString(R.string.app_name)).setMessage(getString(R.string.ticket_not_ready)).show();

                break;

        }
    }


    @Override
    public void onBack() {
        if (getActivity() != null)
            getActivity().getSupportFragmentManager().popBackStack();
    }


}