package com.asc.yazy.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.asc.yazy.R;
import com.asc.yazy.adapter.BookingViewPagerAdapter;
import com.asc.yazy.api.model.ModelDetails;
import com.asc.yazy.api.model.ModelTravelAgency;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.core.PaymentModel;
import com.asc.yazy.databinding.FragmentBookingCountrollerBinding;
import com.asc.yazy.interfaces.IFragment;
import com.asc.yazy.utils.AnalyticsUtility;

import java.util.Objects;

public class BookingControllerFragment extends Fragment implements IFragment, View.OnClickListener {

    public static ModelDetails offer;
    static BookingViewPagerAdapter mainContainerViewPagerAdapter;
    static ViewPager bookingContent;
    static PaymentModel paymentModel;
    private InputMethodManager keyboard;
    private FragmentBookingCountrollerBinding binding;
    private int currentPage;
    private final ViewPager.OnPageChangeListener onPagerChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            BookingControllerFragment.this.currentPage = position;
            assert keyboard != null;
            keyboard.hideSoftInputFromWindow(Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView().getWindowToken(), 0);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    public BookingControllerFragment(ModelDetails offer, ModelTravelAgency travelAgency, String selectedDate) {
        BookingControllerFragment.offer = offer;
        paymentModel = new PaymentModel();
        paymentModel.setStartDate(selectedDate);
        if (travelAgency != null)
            paymentModel.setTravelAgencyId(travelAgency.getTravel_agency_id());
        paymentModel.setDestination(offer.getTitle());
        paymentModel.setOfferId(offer.getId());
        paymentModel.setCurrency(offer.getCurrency());
        paymentModel.setTitle(offer.getTitle());
        paymentModel.setOffer(offer);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_booking_countroller, null, false);
        AnalyticsUtility.logEventOpen(AnalyticsUtility.Events.BOOKING_CONTROLLER);
        AnalyticsUtility.setScreen(this);
        CheckLanguage checkLanguage = new CheckLanguage(getActivity());
        checkLanguage.UpdateLanguage();
        binding.bookingContent.beginFakeDrag();
        bookingContent = binding.bookingContent;
        new SetUpViewPagerTask().execute();
        binding.imgBack.setOnClickListener(this);
        Animation adults = AnimationUtils.loadAnimation(getActivity(), R.anim.item_animation_fall);
        adults.setStartOffset(0);
        adults.setDuration(800);
        binding.layoutHeader.setAnimation(adults);
        keyboard = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Activity.INPUT_METHOD_SERVICE);
        return binding.getRoot();
    }

    @Override
    public void onBack() {
        if (currentPage == 0) {
            if (getActivity() != null)
                getActivity().finish();
            return;
        }
        if (currentPage == 4) {
            if (paymentModel.getChild() == 0)
                binding.bookingContent.setCurrentItem(2, true);
            else
                binding.bookingContent.setCurrentItem(3, true);
        } else
            binding.bookingContent.setCurrentItem(currentPage - 1, true);

    }

    @Override
    public void onClick(View v) {

        if (v == null)
            return;

        if (v.getId() == R.id.imgBack) {

            if (currentPage == 0) {
                if (getActivity() != null)
                    getActivity().finish();
                return;
            }
            if (currentPage == 4) {
                if (paymentModel.getChild() == 0)
                    binding.bookingContent.setCurrentItem(2, true);
                else
                    binding.bookingContent.setCurrentItem(3, true);
            } else
                binding.bookingContent.setCurrentItem(currentPage - 1, true);
        }

    }

    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class SetUpViewPagerTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            mainContainerViewPagerAdapter = new BookingViewPagerAdapter(Objects.requireNonNull(BookingControllerFragment.this.getActivity()).getSupportFragmentManager());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            binding.bookingContent.setOffscreenPageLimit(5);
            binding.bookingContent.setAdapter(mainContainerViewPagerAdapter);
            binding.bookingContent.setCurrentItem(0);
            binding.bookingContent.addOnPageChangeListener(onPagerChangeListener);
        }
    }
}
