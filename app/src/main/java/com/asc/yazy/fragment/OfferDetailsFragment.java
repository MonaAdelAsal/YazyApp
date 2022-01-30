package com.asc.yazy.fragment;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.asc.yazy.R;
import com.asc.yazy.activity.AuthenticationActivity;
import com.asc.yazy.activity.BookingActivity;
import com.asc.yazy.activity.NoNetActivity;
import com.asc.yazy.adapter.AccommodationAdapter;
import com.asc.yazy.adapter.PromotedOffersAdapter;
import com.asc.yazy.adapter.ViewPagerAdapter;
import com.asc.yazy.api.ApiClient;
import com.asc.yazy.api.ApiInterface;
import com.asc.yazy.api.model.CityModel;
import com.asc.yazy.api.model.ModelDetails;
import com.asc.yazy.api.model.ModelDetailsAPi;
import com.asc.yazy.api.model.ModelOffer;
import com.asc.yazy.api.model.ModelStatic;
import com.asc.yazy.api.model.ModelTravelAgency;
import com.asc.yazy.api.model.ModelUser;
import com.asc.yazy.api.pagination.offers.NetworkState;
import com.asc.yazy.api.pagination.offers.OffersPagedAdapter;
import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.cash.room.model.OfferRoomModel;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.core.GlobalInfoDialog;
import com.asc.yazy.core.PlaneDialog;
import com.asc.yazy.core.SelectDateDialog;
import com.asc.yazy.databinding.CustomerServiceDialogBinding;
import com.asc.yazy.databinding.FragmentOfferDetailsBinding;
import com.asc.yazy.databinding.LoginDialogBinding;
import com.asc.yazy.databinding.LoginToBookDialogBinding;
import com.asc.yazy.databinding.QuestionSendDialogBinding;
import com.asc.yazy.interfaces.IFragment;
import com.asc.yazy.interfaces.OnDateSelectedListener;
import com.asc.yazy.service.FavoriteTask;
import com.asc.yazy.utils.AnalyticsUtility;
import com.asc.yazy.utils.Constants;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity;


public class OfferDetailsFragment extends Fragment implements View.OnClickListener, IFragment {


    private FragmentOfferDetailsBinding binding;
    private ModelOffer offer;
    private ModelDetails offerDetail;
    private OfferRoomModel offerRoomModel;
    private ModelTravelAgency travelAgency;
    private final OnDateSelectedListener Listener = new OnDateSelectedListener() {
        @Override
        public void onDateSelectedListener(String selectedDate) {
            Intent intent = new Intent(getActivity(), BookingActivity.class);
            intent.putExtra(Constants.OFFER, offerDetail);
            intent.putExtra(Constants.TRAVEL_AGENCY, travelAgency);
            intent.putExtra(Constants.SELECTED_DATE, selectedDate);
            Objects.requireNonNull(getActivity()).startActivity(intent);
        }
    };
    private int currentPage = 0;
    private ModelUser user;
    private int liked;
    private GuideView caseView;

    public OfferDetailsFragment() {
    }

    public OfferDetailsFragment(ModelOffer offer) {
        this.offer = offer;
    }

    public OfferDetailsFragment(OfferRoomModel offer) {
        this.offerRoomModel = offer;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_offer_details, container, false);
        binding.likeAnimation.setVisibility(View.VISIBLE);
        binding.imgBack.setOnClickListener(this);
        binding.linearTerms.setOnClickListener(this);
        binding.btnAskQuestion.setOnClickListener(this);
        CheckLanguage checkLanguage = new CheckLanguage(getActivity());
        checkLanguage.UpdateLanguage();
        AnalyticsUtility.logEventOpen(AnalyticsUtility.Events.OFFER_DETAILS);
        AnalyticsUtility.setScreen(this);
        user = UtilsPreference.getInstance(OfferDetailsFragment.this.getContext()).getUser();
        return binding.getRoot();

    }

    private void showIntroInstructions() {

        if (OfferDetailsFragment.this.getContext() == null)
            return;

        boolean firstRun = UtilsPreference.getInstance(OfferDetailsFragment.this.getContext()).getPreference(Constants.IS_TRIP_FIRST_RUN, true);

        if (!firstRun)
            return;


        caseView = new GuideView.Builder(OfferDetailsFragment.this.getContext())
                .setTitle(Objects.requireNonNull(OfferDetailsFragment.this.getContext()).getResources().getString(R.string.intro_book))
                .setContentText(Objects.requireNonNull(OfferDetailsFragment.this.getContext()).getResources().getString(R.string.book_offer_details))
                .setTargetView(binding.bookLayout)
                .setGravity(Gravity.center)//optional
                .setDismissType(DismissType.anywhere) //optional - default dismissible by TargetView
                .build();


        caseView.show();

        UtilsPreference.getInstance(OfferDetailsFragment.this.getContext()).savePreference(Constants.IS_TRIP_FIRST_RUN, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (Constants.isNetworkAvailable(OfferDetailsFragment.this.getContext())) {
            binding.layoutNoInternet.getRoot().setVisibility(View.GONE);
            bindDate();
            loadDate();

        } else {
            binding.layoutNoInternet.getRoot().setVisibility(View.VISIBLE);
            binding.layoutNoInternet.btnTryAgain.setOnClickListener(this);
        }
    }

    private void loadDate() {
        if (offer == null || offer.getId() == null)
            return;

        String token = null;
        if (user != null && user.getAccess_token() != null && !user.getAccess_token().isEmpty())
            token = "Bearer " + user.getAccess_token();

        ApiInterface mApiService = ApiClient.getInterfaceService();

        Call<ModelDetailsAPi> call = mApiService.getDetails(token, Constants.getLANGUAGE(), offer.getId());
        call.enqueue(new Callback<ModelDetailsAPi>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<ModelDetailsAPi> call, @NonNull Response<ModelDetailsAPi> response) {
                if (getActivity() == null)
                    return;
                if (!isAdded())
                    return;

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
                        GlobalInfoDialog.getInstance(getActivity()).setTitle(getString(R.string.error)).setMessage(message).show();
                        getActivity().getSupportFragmentManager().popBackStack();
                        return;
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                ModelDetailsAPi body = response.body();

                if (body == null) {
                    GlobalInfoDialog.getInstance(getActivity()).setTitle(getString(R.string.error)).setMessage(getString(R.string.please_check_your_internet_connection)).show();
                    return;
                }
                if (body.getStatus() == 200 && body.getData() != null) {
                    AnalyticsUtility.logEventAPISuccess(AnalyticsUtility.Events.OFFER_DETAILS);
                    binding.tvDescription.stopShimmerAnimation();
                    binding.tvDescriptionDate.setBackground(null);
                    binding.tvFlightName.stopShimmerAnimation();
                    binding.tvFlightData.setBackground(null);
                    binding.tvClass.stopShimmerAnimation();
                    binding.tvClassData.setBackground(null);
                    binding.loaderHotel.stopShimmerAnimation();
                    binding.tvHotelData.setBackground(null);
                    binding.tvRoomType.stopShimmerAnimation();
                    binding.tvRoomTypeData.setBackground(null);
                    binding.tvHotelData.setBackground(null);
                    binding.loaderTravelAgency.stopShimmerAnimation();
                    binding.tvTravelAgencyName.setBackground(null);
                    binding.loaderDate.stopShimmerAnimation();
                    binding.tvDate.setBackground(null);


                    binding.tvChild.setText(body.getData().getPrice_child() + " " + body.getData().getCurrency() + " / " + getActivity().getResources().getString(R.string.child));
                    binding.tvAdult.setText(body.getData().getPrice() + " " + body.getData().getCurrency() + " / " + getActivity().getResources().getString(R.string.adult));
                    offer.setPrice(body.getData().getPrice());
                    offer.setPrice_child(body.getData().getPrice_child());


                    try {
                        binding.tvDestination.setText(body.getData().getCities().get(0).getCity_name());
                        binding.tvFromCity.setText(body.getData().getCities().get(0).getCountry_name());
                    } catch (Exception ignored) {
                    }

                    binding.tvChild.setBackground(null);
                    binding.tvAdult.setBackground(null);
                    binding.LoaderAdultPrice.stopShimmerAnimation();
                    binding.LoaderChildPrice.stopShimmerAnimation();
                    ModelDetails details = body.getData();
                    CityModel city = details.getCities().get(0);
                    details.setAccommodation_services(city.getAccommodation_services());
                    details.setHotel(city.getHotel());
                    details.setHotel_room_type(city.getHotel_room_type());
                    details.setAirlines(city.getAirlines());
                    details.setFight_class(city.getFlight_class());


                    binding.setDetailsData(details);
                    if (body.getData().getOpen_package() == 1 && !body.getData().getDuration().equals("( null )")) {
                        binding.tvDate.setText(body.getData().getDuration());
                        binding.tvValidationDate.setText(getString(R.string.offer_valid_from) + " " + body.getData().getDate_from() + " " + getString(R.string.to) + " " + body.getData().getExpiration_date());
                    } else {
                        binding.tvDate.setText(Constants.getFormattedDate(body.getData()));
                        binding.tvValidationDate.setVisibility(View.INVISIBLE);
                    }
                    List<String> sliderImg;
                    sliderImg = body.getData().getImages();
                    sliderImg.add(body.getData().getImage());
                    binding.vpImages.setAdapter(new ViewPagerAdapter(sliderImg, getActivity()));

                    if (body.getData().getAccommodation_services() != null && body.getData().getAccommodation_services().size() > 0) {
                        AccommodationAdapter offersAdapter = new AccommodationAdapter(body.getData().getAccommodation_services());
                        binding.rvAccommodationServices.setLayoutManager(new GridLayoutManager(getContext(), 1));
                        binding.rvAccommodationServices.setHasFixedSize(true);
                        binding.rvAccommodationServices.setAdapter(offersAdapter);
                        binding.tvAccommodationServices.setVisibility(View.VISIBLE);
                        binding.rvAccommodationServices.setVisibility(View.VISIBLE);
                    } else {
                        binding.tvAccommodationServices.setVisibility(View.GONE);
                        binding.rvAccommodationServices.setVisibility(View.GONE);
                    }


                    binding.likeAnimation.setOnClickListener(OfferDetailsFragment.this);
                    offer.setIs_favorited(body.getData().getIs_favorited());
                    offer.setId(body.getData().getId());
                    liked = body.getData().getIs_favorited();
                    travelAgency = body.getData().getTravel_agency_data();
                    StartAnimateSlider(body.getData().getImages());
                    binding.btnBookNow.setOnClickListener(OfferDetailsFragment.this);
                    binding.tvTravelAgencyName.setOnClickListener(OfferDetailsFragment.this);
                    binding.ratingCount.setOnClickListener(OfferDetailsFragment.this);
                    binding.ivShare.setOnClickListener(OfferDetailsFragment.this);

                    offer.getLiveFav().observe(OfferDetailsFragment.this, integer -> {
                        Log.d("OfferDetailsFragment", "fragment onChanged: " + integer);
                        if (integer == 1)
                            binding.likeAnimation.setImageResource(R.drawable.ic_favorite_selected);
                        else
                            binding.likeAnimation.setImageResource(R.drawable.ic_favorite_unselected);

                    });

                    offerDetail = body.getData();
                    if (offerDetail.getPolicy() == null) {
                        binding.linearTerms.setVisibility(View.GONE);
                    }
                    showIntroInstructions();

                } else {
                    GlobalInfoDialog.getInstance(getActivity()).setTitle(getString(R.string.error)).setMessage(body.getMessage()).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ModelDetailsAPi> call, @NonNull Throwable t) {

                if (getActivity() == null)
                    return;
                if (OfferDetailsFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;
                AnalyticsUtility.logEventAPIFail(AnalyticsUtility.Events.OFFER_DETAILS);
                GlobalInfoDialog.getInstance(OfferDetailsFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(OfferDetailsFragment.this.getContext().getResources().getString(R.string.cant_connect)).show();
                getActivity().getSupportFragmentManager().popBackStack();

            }
        });
    }

    private void StartAnimateSlider(List<String> list) {
        final Handler handler = new Handler();
        final Runnable Update = () -> {
            if (currentPage == list.size() - 1) {
                currentPage = 0;
            }
            binding.vpImages.setCurrentItem(currentPage++, true);
        };

        Timer timer = new Timer(); // This will create a new Thread
        long DELAY_MS = 3000;
        long PERIOD_MS = 3000;
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
    }

    private void bindDate() {

        if (offerRoomModel != null && offer == null) {
            offer = new ModelOffer();
            offer.setId(offerRoomModel.getLocalID());
            offer.setImage(offerRoomModel.getImage());
            offer.setDate_from(offerRoomModel.getDate_from());
            offer.setDate_to(offerRoomModel.getDate_to());

        }
        binding.LoaderAdultPrice.startShimmerAnimation();
        binding.LoaderChildPrice.startShimmerAnimation();
        binding.tvDescription.startShimmerAnimation();
        binding.tvFlightName.startShimmerAnimation();
        binding.tvClass.startShimmerAnimation();
        binding.loaderHotel.startShimmerAnimation();
        binding.tvRoomType.startShimmerAnimation();
        binding.loaderTravelAgency.startShimmerAnimation();
        binding.loaderDate.startShimmerAnimation();

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (v == null || getActivity() == null)
            return;
        switch (v.getId()) {
            case R.id.btnTryAgain:
                if (Constants.isNetworkAvailable(OfferDetailsFragment.this.getContext())) {
                    binding.layoutNoInternet.getRoot().setVisibility(View.GONE);
                    bindDate();
                    loadDate();

                } else {
                    binding.layoutNoInternet.getRoot().setVisibility(View.VISIBLE);
                    binding.layoutNoInternet.btnTryAgain.setOnClickListener(this);
                }
                break;
            case R.id.imgBack:
                if (getActivity() != null)
                    getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.btnBookNow:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_BOOK);
                if (user == null || user.getAccess_token() == null || user.getAccess_token().isEmpty()) {
                    Dialog LoginToBook = new Dialog(Objects.requireNonNull(getActivity()), android.R.style.Theme_DeviceDefault_Dialog);
                    LoginToBook.setCancelable(true);
                    Objects.requireNonNull(LoginToBook.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    LoginToBookDialogBinding DialogBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.login_to_book_dialog, null, false);
                    LoginToBook.setContentView(DialogBinding.getRoot());
                    DialogBinding.llRegister.setOnClickListener(v13 -> {
                        AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_REGISTER);
                        Intent i = new Intent(getActivity(), AuthenticationActivity.class);
                        i.putExtra("Target", "Register");
                        startActivity(i);
                        LoginToBook.dismiss();
                    });
                    DialogBinding.btnLogin.setOnClickListener(v12 -> startActivity(new Intent(getActivity(), AuthenticationActivity.class)));
                    LoginToBook.show();
                    //  getActivity().startActivity(new Intent(getActivity(), AuthenticationActivity.class));
                    return;
                }
                if (offerDetail.getOpen_package() == 1) {
                    AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_SELECT_DATE);
                    new SelectDateDialog(Objects.requireNonNull(OfferDetailsFragment.this.getContext()), Listener, offerDetail.getDate_from(), offerDetail.getExpiration_date());
                } else {
                    AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_BOOK);
                    Intent intent = new Intent(getActivity(), BookingActivity.class);
                    intent.putExtra(Constants.OFFER, offerDetail);
                    intent.putExtra(Constants.TRAVEL_AGENCY, travelAgency);
                    Objects.requireNonNull(getActivity()).startActivity(intent);
                }


                break;
            case R.id.linearTerms:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_OFFER_POLICY);
                if (getActivity() != null)
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down, R.anim.slide_out_down, R.anim.slide_out_down)
                            .add(R.id.fullContent, new OfferPolicyFragment(offerDetail.getPolicy()))
                            .addToBackStack("OfferPolicyFragment")
                            .commit();
                break;
            case R.id.ratingCount:
            case R.id.tvTravelAgencyName:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_TRAVEL_AGENCY);
                if (getActivity() != null)
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down, R.anim.slide_out_down, R.anim.slide_out_down)
                            .add(R.id.fullContent, new TravelAgencyFragment(travelAgency))
                            .addToBackStack("TravelAgencyFragment")
                            .commit();
                break;
            case R.id.likeAnimation:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.LIKE);
                if (user == null || user.getAccess_token() == null || user.getAccess_token().isEmpty()) {
                    Dialog LoginToAddFav = new Dialog(Objects.requireNonNull(getActivity()), android.R.style.Theme_DeviceDefault_Dialog);
                    LoginToAddFav.setCancelable(true);
                    Objects.requireNonNull(LoginToAddFav.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    LoginDialogBinding DialogBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.login_dialog, null, false);
                    LoginToAddFav.setContentView(DialogBinding.getRoot());
                    DialogBinding.btnCancel.setOnClickListener(v1 -> LoginToAddFav.dismiss());
                    DialogBinding.btnLogin.setOnClickListener(v12 -> startActivity(new Intent(getActivity(), AuthenticationActivity.class)));
                    LoginToAddFav.show();
                    break;
                } else {
                    int prev = offer.getIs_favorited();
                    new FavoriteTask(OfferDetailsFragment.this.getContext(), offer, liked, count -> {
                        if (count == NetworkState.NO_NET) {
                            OffersPagedAdapter.faveAction = true;
                            PromotedOffersAdapter.faveAction = true;
                            offer.setIs_favorited(prev);
                        }
                    }).execute();
                    OffersPagedAdapter.faveAction = true;
                    PromotedOffersAdapter.faveAction = true;
                    if (offer.getIs_favorited() == 1) {
                        offer.setIs_favorited(0);

                    } else {
                        offer.setIs_favorited(1);
                    }
                    break;
                }

            case R.id.ivShare:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.SHARE);
                createLink();
                break;
            case R.id.btnAskQuestion:
                if (user == null || user.getAccess_token() == null || user.getAccess_token().isEmpty()) {
                    Dialog LoginToAddFav = new Dialog(Objects.requireNonNull(getActivity()), android.R.style.Theme_DeviceDefault_Dialog);
                    LoginToAddFav.setCancelable(true);
                    Objects.requireNonNull(LoginToAddFav.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    LoginDialogBinding DialogBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.login_dialog, null, false);
                    LoginToAddFav.setContentView(DialogBinding.getRoot());
                    DialogBinding.tvMessage.setText(R.string.cant_ask);
                    DialogBinding.tvhint.setText(R.string.login_to_ask);
                    DialogBinding.btnCancel.setOnClickListener(v1 -> LoginToAddFav.dismiss());
                    DialogBinding.btnLogin.setOnClickListener(v12 -> startActivity(new Intent(getActivity(), AuthenticationActivity.class)));
                    LoginToAddFav.show();
                    break;
                } else {
                    Dialog AskQuestion = new Dialog(Objects.requireNonNull(getActivity()), android.R.style.Theme_DeviceDefault_Dialog);
                    AskQuestion.setCancelable(false);
                    Objects.requireNonNull(AskQuestion.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    CustomerServiceDialogBinding DialogBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.customer_service_dialog, null, false);
                    AskQuestion.setContentView(DialogBinding.getRoot());
                    DialogBinding.etEmail.setText(user.getEmail());
                    DialogBinding.btnCancel.setOnClickListener(v13 -> AskQuestion.dismiss());
                    DialogBinding.btnSend.setOnClickListener(v13 -> {
                        if (DialogBinding.etEmail.getText().toString().length() == 0) {
                            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.email_required), Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (DialogBinding.etQuestion.getText().toString().length() == 0) {
                            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.question_required), Toast.LENGTH_LONG).show();
                            return;
                        }
                        SendQuestion(DialogBinding.etEmail.getText().toString(), DialogBinding.etQuestion.getText().toString());
                        AskQuestion.dismiss();
                    });
                    AskQuestion.show();
                    break;
                }
        }

    }

    private void SendQuestion(String email, String question) {

        String token = UtilsPreference.getInstance(getActivity()).getUser().getAccess_token();
        if (token == null || token.isEmpty()) {
            Objects.requireNonNull(getActivity()).startActivity(new Intent(getActivity(), AuthenticationActivity.class));
            return;
        }
        PlaneDialog planeDialog = new PlaneDialog(Objects.requireNonNull(getActivity()));
        ApiInterface mApiService = ApiClient.getInterfaceService();
        Call<ModelStatic> call = mApiService.SendQuestion("Bearer " + token, Constants.getLANGUAGE(), offer.getId(), email, question);
        call.enqueue(new Callback<ModelStatic>() {
            @Override
            public void onResponse(@NonNull Call<ModelStatic> call, @NonNull Response<ModelStatic> response) {

                if (getActivity() == null)
                    return;
                if (OfferDetailsFragment.this.getContext() == null)
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
                        GlobalInfoDialog.getInstance(OfferDetailsFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(message).show();
                        return;
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                ModelStatic body = response.body();

                if (body == null) {
                    GlobalInfoDialog.getInstance(OfferDetailsFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(getString(R.string.please_check_your_internet_connection)).show();
                    return;
                }
                if (body.getStatus() == 200) {
                    Dialog QuestionSend = new Dialog(Objects.requireNonNull(getActivity()), android.R.style.Theme_DeviceDefault_Dialog);
                    QuestionSend.setCancelable(false);
                    Objects.requireNonNull(QuestionSend.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    QuestionSendDialogBinding DialogBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.question_send_dialog, null, false);
                    QuestionSend.setContentView(DialogBinding.getRoot());
                    DialogBinding.tvOK.setOnClickListener(v13 -> QuestionSend.dismiss());
                    QuestionSend.show();
                } else {
                    GlobalInfoDialog.getInstance(OfferDetailsFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(body.getMessage()).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ModelStatic> call, @NonNull Throwable t) {

                if (OfferDetailsFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;

                planeDialog.Dismiss();
                AnalyticsUtility.logEventAPIFail(AnalyticsUtility.Events.SEND_QUESTION);
                // GlobalInfoDialog.getInstance(OfferDetailsFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(OfferDetailsFragment.this.getContext().getResources().getString(R.string.cant_connect)).show();
                requireActivity().startActivity(new Intent(getActivity(), NoNetActivity.class));

            }
        });

    }

    private void createLink() {

        if (offer == null)
            return;
/*
        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("http://testing.com/details"))
                .setDomainUriPrefix("https://ascios.page.link")
                .setAndroidParameters(
                        new DynamicLink.AndroidParameters.Builder("com.asc.yazy")
                                .setMinimumVersion(Integer.parseInt(offer.getId()))
                                .build())
                .buildDynamicLink();

*/
        //   https://referearnpro.page.link?apn=blueappsoftware.referearnpro&link=https%3A%2F%2Fwww.blueappsoftware.com%2F
        // apn  ibi link
        // manuall link


        String sharelinktext = "https://ascios.page.link/?" +
                "link=https://testing.com/details/?payload=" + offer.getId() + "-" + offer.getMulti_cities() +
                "&apn=" + Objects.requireNonNull(getActivity()).getPackageName();

        //       Log.d("MainActivity", "  Long uri " + dynamicLink.getUri().toString());
        Log.d("MainActivity", "  Long sta " + sharelinktext);
        // shorten the link

        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(Uri.parse(sharelinktext))
                .buildShortDynamicLink()
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Short link created

                        Uri shortLink = Objects.requireNonNull(task.getResult()).getShortLink();
                        Uri flowchartLink = task.getResult().getPreviewLink();
                        Log.d("MainActivity", "short link " + Objects.requireNonNull(shortLink).toString());
                        Log.d("MainActivity", offer.getId());
                        Log.d("MainActivity", "short link " + Objects.requireNonNull(flowchartLink).toString());

                        // share app dialog
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_TEXT, shortLink.toString());
                        intent.setType("text/plain");
                        startActivity(intent);
                    } else {
                        // Error
                        // ...
                        Log.d("MainActivity", " error " + task.getException());
                    }
                });
    }

    @Override
    public void onBack() {

        if (caseView != null && caseView.isShowing()) {
            caseView.dismiss();
            return;
        }

        if (getActivity() != null)
            getActivity().getSupportFragmentManager().popBackStack();
    }


}
