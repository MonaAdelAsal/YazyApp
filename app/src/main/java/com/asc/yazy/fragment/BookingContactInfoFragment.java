package com.asc.yazy.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.asc.yazy.R;
import com.asc.yazy.activity.AuthenticationActivity;
import com.asc.yazy.api.model.ModelUser;
import com.asc.yazy.api.pagination.offers.NetworkState;
import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.databinding.FragmentContactInfoBinding;
import com.asc.yazy.utils.AnalyticsUtility;
import com.asc.yazy.utils.Constants;

import java.util.Objects;

import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity;

public class BookingContactInfoFragment extends Fragment implements View.OnClickListener {


    private FragmentContactInfoBinding binding;
    private GuideView caseView;


    public BookingContactInfoFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_contact_info, null, false);
        AnalyticsUtility.logEventOpen(AnalyticsUtility.Events.BOOKING_CONTACT_INFO);
        AnalyticsUtility.setScreen(this);
        CheckLanguage checkLanguage = new CheckLanguage(getActivity());
        checkLanguage.UpdateLanguage();
        binding.btnProcess.setOnClickListener(this);
        binding.countryCode.registerCarrierNumberEditText(binding.etMobile);
        binding.etMobile.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    if (binding.countryCode.isValidFullNumber()) {
                        boolean isEnglish = getResources().getBoolean(R.bool.english_lan);
                        if (isEnglish)
                            binding.etMobile.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_correct, 0);
                        else
                            binding.etMobile.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_correct, 0, 0, 0);
                    } else {
                        boolean isEnglish = getResources().getBoolean(R.bool.english_lan);
                        if (isEnglish)
                            binding.etMobile.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error, 0);
                        else
                            binding.etMobile.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_error, 0, 0, 0);
                    }
                }
            }
        });
        bindDate();
        return binding.getRoot();
    }


    private void bindDate() {

        if (getActivity() == null)
            return;

        ModelUser user = UtilsPreference.getInstance(getActivity()).getUser();
        if (user == null || user.getAccess_token() == null || user.getAccess_token().isEmpty())
            return;

        binding.etEmail.setText(user.getEmail());
        binding.etMobile.setText(user.getMobile());
        binding.countryCode.setCountryForPhoneCode(Integer.parseInt(user.getCountry_code()));
        AnalyticsUtility.logEventLoadDate(AnalyticsUtility.Events.BOOKING_CONTACT_INFO);
    }


    private boolean isValidateDate() {


        if (!binding.countryCode.isValidFullNumber()) {
            binding.etMobile.setError(Objects.requireNonNull(BookingContactInfoFragment.this.getContext()).getResources().getString(R.string.mobileNumberNotValid));
            return false;
        } else
            BookingControllerFragment.paymentModel.setCountryCode(binding.countryCode.getSelectedCountryCodeWithPlus());

        if (binding.etMobile.getText() == null || binding.etMobile.getText().toString().trim().isEmpty()) {
            binding.etMobile.setError(Objects.requireNonNull(BookingContactInfoFragment.this.getContext()).getResources().getString(R.string.mobileNumberNotValid));
            return false;
        } else
            BookingControllerFragment.paymentModel.setPhoneNumber(Constants.getNormalizedPhoneNumber(binding.countryCode.getFullNumber(), binding.countryCode.getSelectedCountryCode()));

        if (binding.etEmail.getText() == null || binding.etEmail.getText().toString().trim().isEmpty() || Constants.isEmailValid(binding.etEmail.getText().toString().trim())) {
            binding.etEmail.setError(Objects.requireNonNull(BookingContactInfoFragment.this.getContext()).getResources().getString(R.string.valid_mail_is_required));
            return false;
        } else
            BookingControllerFragment.paymentModel.setEmail(binding.etEmail.getText().toString());

        return true;
    }

    @Override
    public void onClick(View v) {
        if (v == null)
            return;

        if (v.getId() == R.id.btnProcess) {


            if (getActivity() == null)
                return;

            if (getActivity() != null) {
                InputMethodManager keyboard = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                assert keyboard != null;
                keyboard.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getRootView().getWindowToken(), 0);
            }

            ModelUser user = UtilsPreference.getInstance(getActivity()).getUser();
            if (user == null || user.getAccess_token() == null || user.getAccess_token().isEmpty()) {
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_REGISTER);
                Intent intent = new Intent(getActivity(), AuthenticationActivity.class);
                intent.putExtra(Constants.TRANSACTION, NetworkState.UN_AUTHORISE);
                Objects.requireNonNull(getActivity()).startActivity(intent);
                return;
            }

            if (isValidateDate()) {

                AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_CHECK_OUT);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                        .add(R.id.mainContent, new CheckOutFragment(BookingControllerFragment.paymentModel))
                        .addToBackStack(null)
                        .commit();

            }

        }


        /*
        if (v.getId() == R.id.btnPay) {
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

            if (isValidateDate()) {

                AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_CHECK_OUT);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                        .add(R.id.mainContent, new CheckOutFragment(BookingControllerFragment.paymentModel))
                        .addToBackStack("OfferDetailsFragment")
                        .commit();

                AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_PAYMENT);
                Intent intent = new Intent(BookingContactInfoFragment.this.getActivity(), HesabeActivity.class);
                intent.putExtra(Constants.TRANSACTION, BookingControllerFragment.paymentModel);
                if (bookingId.length() > 0) {
                    intent.putExtra("BookingID", bookingId);
                }
                Objects.requireNonNull(BookingContactInfoFragment.this.getActivity()).startActivity(intent);

            }
        } else if (v.getId() == R.id.btnPayForMe) {
            AnalyticsUtility.logAction(AnalyticsUtility.Actions.PAY_FOR_ME);
            if (getActivity() == null)
                return;
            if (isValidateDate()) {
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
        }
        */

    }

    /*
    private void shareLink() {
        if (OpenPackage == 1 && SelectedDate.length() == 0) {
            final Dialog SelectDate_Mandatory;
            SelectDate_Mandatory = new Dialog(Objects.requireNonNull(getActivity()), R.style.DialogSlideAnim);
            DialogSelectdateMandatoryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.dialog_selectdate_mandatory, null, false);
            SelectDate_Mandatory.setContentView(binding.getRoot());
            SelectDate_Mandatory.setCancelable(true);
            Objects.requireNonNull(SelectDate_Mandatory.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            binding.btnCancel.setOnClickListener(v1 -> SelectDate_Mandatory.dismiss());
            binding.btnSelectDate.setOnClickListener(v12 -> {
                SelectDate_Mandatory.dismiss();
                new SelectDateDialog(Objects.requireNonNull(getActivity()), Listener, DateFrom, DateTo, duration);
            });
            SelectDate_Mandatory.show();
        } else {
            if (ShareLink.length() == 0) {
                BEBookingTask.book(getActivity(), BookingControllerFragment.paymentModel, threadListener, 1);
            } else {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, ShareLink);
                intent.setType("text/plain");
                try {
                    startActivity(intent);
                    shareDone = 1;
                } catch (Exception e) {
                    GlobalInfoDialog.getInstance(BookingContactInfoFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(getResources().getString(R.string.not_supported_apps)).show();
                    shareDone = 0;
                }
            }
        }
    }
    */

    @SuppressWarnings("deprecation")
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            final Handler handler = new Handler();
            handler.postDelayed(this::introViewBehaviour, 500);
        } else if (caseView != null) {
            caseView.dismiss();
        }

    }

    private void introViewBehaviour() {

        if (BookingContactInfoFragment.this.getContext() == null)
            return;

        boolean firstRun = UtilsPreference.getInstance(BookingContactInfoFragment.this.getContext()).getPreference(Constants.IS_BOOK_INFO_FIRST_RUN, true);


        if (!firstRun)
            return;


        caseView = new GuideView.Builder(BookingContactInfoFragment.this.getContext())
                .setTitle(Objects.requireNonNull(BookingContactInfoFragment.this.getContext()).getResources().getString(R.string.intro_book))
                .setContentText(Objects.requireNonNull(BookingContactInfoFragment.this.getContext()).getResources().getString(R.string.book_contact_info))
                .setTargetView(binding.RLContactData)
                .setGravity(Gravity.center)//optional
                .setDismissType(DismissType.anywhere)
                .build();

        caseView.dismiss();
        caseView.show();

        UtilsPreference.getInstance(BookingContactInfoFragment.this.getContext()).savePreference(Constants.IS_BOOK_INFO_FIRST_RUN, false);


    }

    /*
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
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_HOME);
                startActivity(new Intent(getActivity(), com.asc.yazy.activity.MainActivity.class));
                getActivity().finish();
                share_done.dismiss();
            });
            share_done.show();
        }
        super.onResume();
    }
    */
}
