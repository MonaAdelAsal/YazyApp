package com.asc.yazy.fragment;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.asc.yazy.R;
import com.asc.yazy.activity.AuthenticationActivity;
import com.asc.yazy.activity.HesabeActivity;
import com.asc.yazy.activity.NoNetActivity;
import com.asc.yazy.adapter.AdultsCheckOutAdapter;
import com.asc.yazy.adapter.ChildCheckOutAdapter;
import com.asc.yazy.api.ApiClient;
import com.asc.yazy.api.ApiInterface;
import com.asc.yazy.api.model.ModelPromoCodeAPI;
import com.asc.yazy.api.model.ModelUser;
import com.asc.yazy.api.model.PromoCode;
import com.asc.yazy.api.pagination.offers.NetworkState;
import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.core.GlobalInfoDialog;
import com.asc.yazy.core.PaymentModel;
import com.asc.yazy.core.PlaneDialog;
import com.asc.yazy.core.SelectDateDialog;
import com.asc.yazy.databinding.DialogSelectdateMandatoryBinding;
import com.asc.yazy.databinding.DoneShareBinding;
import com.asc.yazy.databinding.FragmentCheckOutBinding;
import com.asc.yazy.databinding.ShareLinkDialogBinding;
import com.asc.yazy.interfaces.IFragment;
import com.asc.yazy.interfaces.OnBackThreadListener;
import com.asc.yazy.interfaces.OnDateSelectedListener;
import com.asc.yazy.service.BEBookingTask;
import com.asc.yazy.utils.AnalyticsUtility;
import com.asc.yazy.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CheckOutFragment extends Fragment implements View.OnClickListener, IFragment {


    // private long mLastClickTime = System.currentTimeMillis();
    private FragmentCheckOutBinding binding;
    private PaymentModel paymentModel;
    private boolean payForMe = true;
    private String ShareLink = "", bookingId = "";
    private int shareDone = 0;


    private boolean isPaying = false;
    private PlaneDialog booingBar;
    private final OnBackThreadListener threadListener = new OnBackThreadListener() {
        @Override
        public void onSuccess(String shareLink, String booking_id, int gift, String points) {

            if (booingBar != null)
                booingBar.dismiss();

            AnalyticsUtility.logEvent(AnalyticsUtility.Events.PAYMENT_SUCCESS, AnalyticsUtility.Events.PAYMENT_SUCCESS);
            if (gift == 1 && shareLink != null && !shareLink.trim().isEmpty()) {
                ShareLink = shareLink;
                bookingId = booking_id;
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, shareLink);
                intent.setType("text/plain");
                try {
                    startActivity(intent);
                    shareDone = 1;
                } catch (Exception e) {
                    GlobalInfoDialog.getInstance(CheckOutFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(getResources().getString(R.string.not_supported_apps)).show();
                    shareDone = 0;
                }

            } else {
                shareDone = 0;
                GlobalInfoDialog.getInstance(CheckOutFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(getResources().getString(R.string.share_link_not_retrived)).show();
            }
        }

        @Override
        public void onFailure(String error) {
            if (booingBar != null)
                booingBar.dismiss();
            AnalyticsUtility.logEvent(AnalyticsUtility.Events.PAYMENT_FAIL, AnalyticsUtility.Events.PAYMENT_FAIL);
            GlobalInfoDialog.getInstance(getActivity()).setTitle(Objects.requireNonNull(getActivity()).getString(R.string.error)).setMessage(error).show();

        }
    };
    private final OnDateSelectedListener listener = SelectedDate -> {

        if (SelectedDate == null || SelectedDate.trim().isEmpty()) {
            return;
        }
        paymentModel.setStartDate(SelectedDate);
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = df.parse(paymentModel.getStartDate());
            assert date != null;
            binding.tvDate.setText(new SimpleDateFormat("dd LLLL yyyy", Locale.getDefault()).format(date));
        } catch (ParseException e) {
            binding.tvDate.setText(paymentModel.getStartDate());
        }

        if (isPaying && !payForMe)
            shareLink();

    };
    private int basePrice;


    public CheckOutFragment() {
    }

    public CheckOutFragment(PaymentModel paymentModel) {
        this.paymentModel = paymentModel;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_check_out, null, false);
        AnalyticsUtility.setScreen(this);
        AnalyticsUtility.logEventOpen(AnalyticsUtility.Events.CHECK_OUT);
        binding.imgBack.setOnClickListener(this);
        binding.btnPayForMe.setOnClickListener(this);
        binding.BtnPayForOthers.setOnClickListener(this);
        binding.tvSelect.setOnClickListener(this);
        binding.btnProcess.setOnClickListener(this);
        binding.tvRemove.setOnClickListener(null);
        binding.tvRemove.setVisibility(View.GONE);
        binding.etDisCount.setEnabled(true);
        binding.setModel(paymentModel);
        basePrice = paymentModel.getAmountInt();
        binding.tvPrice.setText((paymentModel.getAmountInt() + " " + paymentModel.getCurrency()));
        binding.etDisCount.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    binding.tvApply.setVisibility(View.VISIBLE);
                    binding.tvApply.setTextColor(CheckOutFragment.this.requireContext().getResources().getColor(R.color.colorAccent));
                    binding.imgChecked.setVisibility(View.GONE);
                    //    binding.tvRemove.setVisibility(View.GONE);
                    binding.tvApply.setOnClickListener(CheckOutFragment.this);
                } else {
                    binding.tvApply.setVisibility(View.VISIBLE);
                    binding.tvApply.setTextColor(CheckOutFragment.this.requireContext().getResources().getColor(R.color.gray_bold));
                    binding.imgChecked.setVisibility(View.GONE);
                    //    binding.tvRemove.setVisibility(View.GONE);
                    binding.tvApply.setOnClickListener(null);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                binding.tvDisCountMessage.setText(null);
                paymentModel.setAmount(basePrice);
                paymentModel.setPromoCodeID(null);
                binding.tvPrice.setText((paymentModel.getAmountInt() + " " + paymentModel.getCurrency()));
                binding.tvPriceDiscount.setText(null);
                binding.tvPriceDiscount.setVisibility(View.GONE);

                binding.line.setVisibility(View.GONE);
                binding.tvApply.setVisibility(View.VISIBLE);
                binding.imgChecked.setVisibility(View.GONE);

            }
        });
        bindData();
        CheckLanguage checkLanguage = new CheckLanguage(getActivity());
        checkLanguage.UpdateLanguage();
        return binding.getRoot();
    }

    private void bindData() {


        binding.tvName.setText(paymentModel.getTitle());
        binding.tvMail.setText(paymentModel.getEmail());
        binding.tvMobile.setText(paymentModel.getPhoneNumber());

        if (paymentModel.getOffer().getOpen_package() == 1) {
            if (paymentModel.getStartDate() == null || paymentModel.getStartDate().isEmpty()) {
                binding.tvDate.setText(getResources().getString(R.string.not_selected_yet));
                binding.tvSelect.setVisibility(View.VISIBLE);

            } else {
                try {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    Date date = df.parse(paymentModel.getStartDate());
                    assert date != null;
                    binding.tvDate.setText(new SimpleDateFormat("dd LLLL yyyy", Locale.getDefault()).format(date));
                } catch (ParseException e) {
                    binding.tvDate.setText(paymentModel.getStartDate());
                }

                binding.tvSelect.setVisibility(View.GONE);
            }
        } else {
            binding.tvDate.setText(Constants.getFormattedDate(paymentModel.getOffer()));
            binding.tvSelect.setVisibility(View.GONE);
        }


        AdultsCheckOutAdapter adults = new AdultsCheckOutAdapter(paymentModel.getOriginalAdults());
        binding.rvTravellers.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvTravellers.setHasFixedSize(true);
        binding.rvTravellers.setAdapter(adults);

        ChildCheckOutAdapter adapter = new ChildCheckOutAdapter(paymentModel.getOriginalChild());
        binding.rvTravellersChild.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvTravellersChild.setHasFixedSize(true);
        binding.rvTravellersChild.setAdapter(adapter);


    }

    @SuppressLint({"UseCompatLoadingForDrawables", "NonConstantResourceId"})
    @Override
    public void onClick(View v) {
        if (v == null)
            return;
        isPaying = false;
        switch (v.getId()) {
            case R.id.BtnPayForOthers:
                payForMe = false;
                binding.imgPayForMe.setImageDrawable(CheckOutFragment.this.requireContext().getResources().getDrawable(R.drawable.ic_select_off));
                binding.imgPayForOthers.setImageDrawable(CheckOutFragment.this.requireContext().getResources().getDrawable(R.drawable.ic_select_on));
                break;
            case R.id.btnPayForMe:
                payForMe = true;
                binding.imgPayForMe.setImageDrawable(CheckOutFragment.this.requireContext().getResources().getDrawable(R.drawable.ic_select_on));
                binding.imgPayForOthers.setImageDrawable(CheckOutFragment.this.requireContext().getResources().getDrawable(R.drawable.ic_select_off));
                break;
            case R.id.tvSelect:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_SELECT_DATE);
                new SelectDateDialog(Objects.requireNonNull(CheckOutFragment.this.getContext()), listener, paymentModel.getOffer().getDate_from(), paymentModel.getOffer().getDate_to());
                break;
            case R.id.tvApply:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.CHECK_PROMO_CODE);
                checkPromoCode();
                break;
            case R.id.imgBack:
                paymentModel.setAmount(basePrice);
                paymentModel.setPromoCodeID(null);
                if (getActivity() != null)
                    getActivity().getSupportFragmentManager().popBackStack();
                break;
                /*
            case R.id.tvRemove:

                binding.tvDisCountMessage.setText(null);
                paymentModel.setAmount(basePrice);
                binding.tvPrice.setText((paymentModel.getAmountInt() + " " + paymentModel.getCurrency()));
                binding.tvPriceDiscount.setText(null);
                binding.tvPriceDiscount.setVisibility(View.GONE);

                binding.line.setVisibility(View.GONE);

                binding.tvApply.setVisibility(View.VISIBLE);
                binding.imgChecked.setVisibility(View.GONE);
                binding.tvRemove.setVisibility(View.GONE);

                binding.etDisCount.setEnabled(true);

                break;
                */
            case R.id.btnProcess:
                isPaying = true;
                if (payForMe) {

                    if (getActivity() == null)
                        return;

                    ModelUser user = UtilsPreference.getInstance(getActivity()).getUser();
                    if (user == null || user.getAccess_token() == null || user.getAccess_token().isEmpty()) {
                        AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_REGISTER);
                        Intent intent = new Intent(getActivity(), AuthenticationActivity.class);
                        intent.putExtra(Constants.TRANSACTION, NetworkState.UN_AUTHORISE);
                        Objects.requireNonNull(getActivity()).startActivity(intent);
                        return;
                    }

                    AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_PAYMENT);
                    Intent intent = new Intent(CheckOutFragment.this.getActivity(), HesabeActivity.class);
                    paymentModel.setOriginalAdults(null);
                    paymentModel.setOriginalChild(null);
                    intent.putExtra(Constants.TRANSACTION, paymentModel);
                    if (bookingId != null && !bookingId.isEmpty()) {
                        intent.putExtra("BookingID", bookingId);
                    }
                    Objects.requireNonNull(CheckOutFragment.this.getActivity()).startActivity(intent);


                } else {

                    AnalyticsUtility.logAction(AnalyticsUtility.Actions.PAY_FOR_ME);
                    if (getActivity() == null)
                        return;

                    final Dialog ShareDialog;
                    ShareDialog = new Dialog(getActivity(), R.style.DialogSlideAnim);
                    ShareLinkDialogBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.share_link_dialog, null, false);
                    ShareDialog.setContentView(binding.getRoot());
                    ShareDialog.setCancelable(true);
                    Objects.requireNonNull(ShareDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    binding.btnCancel.setOnClickListener(v1 -> ShareDialog.dismiss());
                    binding.btnSelectDate.setOnClickListener(v12 -> {
                        ShareDialog.dismiss();
                        shareLink();
                    });
                    ShareDialog.show();


                }
                break;
        }

    }

    @Override
    public void onResume() {

        if (shareDone == 1) {
            final Dialog share_done;
            share_done = new Dialog(Objects.requireNonNull(getActivity()), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            DoneShareBinding shareBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.done_share, null, false);
            share_done.setContentView(shareBinding.getRoot());
            share_done.setCancelable(false);
            Objects.requireNonNull(share_done.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            shareBinding.btnHome.setOnClickListener(v12 -> {
                share_done.dismiss();
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_HOME);
                startActivity(new Intent(getActivity(), com.asc.yazy.activity.MainActivity.class));
                getActivity().finish();
            });
            share_done.show();
        }

        super.onResume();
    }

    private void shareLink() {


        if (paymentModel.getOffer().getOpen_package() == 1 && paymentModel.getStartDate().isEmpty()) {
            final Dialog SelectDate_Mandatory;
            SelectDate_Mandatory = new Dialog(Objects.requireNonNull(getActivity()), R.style.DialogSlideAnim);
            DialogSelectdateMandatoryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.dialog_selectdate_mandatory, null, false);
            SelectDate_Mandatory.setContentView(binding.getRoot());
            SelectDate_Mandatory.setCancelable(true);
            Objects.requireNonNull(SelectDate_Mandatory.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            binding.btnCancel.setOnClickListener(v1 -> SelectDate_Mandatory.dismiss());
            binding.btnSelectDate.setOnClickListener(v12 -> {
                SelectDate_Mandatory.dismiss();
                new SelectDateDialog(Objects.requireNonNull(getActivity()), listener, paymentModel.getOffer().getDate_from(), paymentModel.getOffer().getDate_to());
            });
            SelectDate_Mandatory.show();
        } else {
            if (ShareLink.length() == 0) {
                booingBar = new PlaneDialog(CheckOutFragment.this.requireActivity());
                booingBar.show();
                BEBookingTask.book(getActivity(), paymentModel, threadListener, 1);
            } else {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, ShareLink);
                intent.setType("text/plain");
                try {
                    startActivity(intent);
                    shareDone = 1;
                } catch (Exception e) {
                    GlobalInfoDialog.getInstance(CheckOutFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(getResources().getString(R.string.not_supported_apps)).show();
                    shareDone = 0;
                }
            }
        }
    }

    @Override
    public void onBack() {
        if (getActivity() != null)
            getActivity().getSupportFragmentManager().popBackStack();
    }

    private void checkPromoCode() {

        ModelUser user = UtilsPreference.getInstance(CheckOutFragment.this.getContext()).getUser();
        if (user == null || user.getAccess_token() == null || user.getAccess_token().isEmpty())
            return;


        if (binding.etDisCount.getText() == null || binding.etDisCount.getText().toString().trim().isEmpty()) {
            binding.etDisCount.setError(Objects.requireNonNull(CheckOutFragment.this.getContext()).getResources().getString(R.string.required));
            return;
        }

        binding.tvDisCountMessage.setText(null);

        PlaneDialog planeDialog = new PlaneDialog(Objects.requireNonNull(getActivity()));

        ApiInterface mApiService = ApiClient.getInterfaceService();
        Call<ModelPromoCodeAPI> call = mApiService.checkPromoCode("Bearer " + user.getAccess_token(), Constants.getLANGUAGE(), binding.etDisCount.getText().toString());

        call.enqueue(new Callback<ModelPromoCodeAPI>() {
            @Override
            public void onResponse(@NonNull Call<ModelPromoCodeAPI> call, @NonNull Response<ModelPromoCodeAPI> response) {

                if (getActivity() == null)
                    return;
                if (CheckOutFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;
                planeDialog.Dismiss();

                if (response.code() != 200) {
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String message = jObjError.getString("message");
                        //   GlobalInfoDialog.getInstance(CheckOutFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(message).show();
                        binding.tvDisCountMessage.setText(message);
                        return;
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                ModelPromoCodeAPI body = response.body();

                if (body == null) {
                    //   GlobalInfoDialog.getInstance(CheckOutFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(getString(R.string.please_check_your_internet_connection)).show();
                    binding.tvDisCountMessage.setText(getString(R.string.please_check_your_internet_connection));
                    return;
                }
                PromoCode promoCode = body.getData();
                if (promoCode != null) {


                    if (promoCode.getPromocodeType().equals(Constants.FIXED)) {
                        double newTotal = paymentModel.getAmount() - Double.parseDouble(promoCode.getPromocodeValue());
                        if (newTotal >= 0) {
                            binding.tvDisCountMessage.setText(promoCode.getPromocodeDescription());
                            paymentModel.setAmount(newTotal);
                            paymentModel.setPromoCodeID(promoCode.getPromocodeId());
                            binding.tvPrice.setText((paymentModel.getAmountInt() + " " + paymentModel.getCurrency()));
                            binding.tvPriceDiscount.setText((basePrice + " " + paymentModel.getCurrency()));
                            binding.tvPriceDiscount.setVisibility(View.VISIBLE);

                            binding.line.setVisibility(View.VISIBLE);
                            binding.tvApply.setVisibility(View.GONE);
                            binding.imgChecked.setVisibility(View.VISIBLE);
                            //   binding.tvRemove.setVisibility(View.VISIBLE);
                            //   binding.etDisCount.setEnabled(false);
                        } else {
                            binding.tvDisCountMessage.setText(R.string.discount_greater);
                        }
                    } else if (promoCode.getPromocodeType().equals(Constants.PERCENTAGE)) {

                        binding.tvDisCountMessage.setText(promoCode.getPromocodeDescription());
                        double newTotal = paymentModel.getAmount() - (((Double.parseDouble(promoCode.getPromocodeValue())) * paymentModel.getAmount()) / 100);
                        paymentModel.setAmount(newTotal);
                        paymentModel.setPromoCodeID(promoCode.getPromocodeId());
                        binding.tvPrice.setText((paymentModel.getAmountInt() + " " + paymentModel.getCurrency()));
                        binding.tvPriceDiscount.setText((basePrice + " " + paymentModel.getCurrency()));
                        binding.tvPriceDiscount.setVisibility(View.VISIBLE);

                        binding.line.setVisibility(View.VISIBLE);

                        binding.tvApply.setVisibility(View.GONE);
                        binding.imgChecked.setVisibility(View.VISIBLE);
                        //  binding.tvRemove.setVisibility(View.VISIBLE);

                        //   binding.etDisCount.setEnabled(false);

                    }
                } else {
                    binding.tvDisCountMessage.setText((body.getMessage() + ""));
                }


            }

            @Override
            public void onFailure(@NonNull Call<ModelPromoCodeAPI> call, @NonNull Throwable t) {

                if (CheckOutFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;
                planeDialog.Dismiss();
                AnalyticsUtility.logEventAPIFail(AnalyticsUtility.Events.ADD_FREQUENT_TRAVELLER);
                //   GlobalInfoDialog.getInstance(CheckOutFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(CheckOutFragment.this.getContext().getResources().getString(R.string.cant_connect)).show();
                binding.tvDisCountMessage.setText(getString(R.string.cant_connect));
                requireActivity().startActivity(new Intent(getActivity(), NoNetActivity.class));


            }
        });
    }


}
