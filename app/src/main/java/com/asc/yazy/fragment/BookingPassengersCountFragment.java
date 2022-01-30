package com.asc.yazy.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.asc.yazy.R;
import com.asc.yazy.adapter.AdultsCountAdapter;
import com.asc.yazy.adapter.AdultsObserverAdapter;
import com.asc.yazy.adapter.BookingViewPagerAdapter;
import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.databinding.FragmentBookingCountBinding;
import com.asc.yazy.interfaces.OnCountListener;
import com.asc.yazy.utils.AnalyticsUtility;
import com.asc.yazy.utils.Constants;

import java.util.ArrayList;
import java.util.Objects;

import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity;

public class BookingPassengersCountFragment extends Fragment implements View.OnClickListener {


    private FragmentBookingCountBinding binding;
    private int adultCount = 1;
    private int childrenCount = 0;
    private final OnCountListener onAdultsCount = (count, isChecked) -> {
        if (isChecked) {
            BookingPassengersCountFragment.this.adultCount = count;
            BookingControllerFragment.paymentModel.setAdults(adultCount);
            BookingControllerFragment.paymentModel.setOriginalAdults(new ArrayList<>());
            BookingViewPagerAdapter.type = AdultsObserverAdapter.ADULT_OBSERVER_TYPE;
            BookingControllerFragment.mainContainerViewPagerAdapter.notifyDataSetChanged();
            updatePrice();
        } else {

            BookingPassengersCountFragment.this.adultCount = 1;
            BookingControllerFragment.paymentModel.setAdults(adultCount);
            BookingControllerFragment.paymentModel.setOriginalAdults(new ArrayList<>());
            BookingViewPagerAdapter.type = AdultsObserverAdapter.ADULT_OBSERVER_TYPE;
            BookingControllerFragment.mainContainerViewPagerAdapter.notifyDataSetChanged();
            updatePrice();
        }

    };
    private final OnCountListener onChildrenCount = (count, isChecked) -> {
        if (isChecked) {
            BookingPassengersCountFragment.this.childrenCount = count;
            BookingControllerFragment.paymentModel.setChild(childrenCount);
            BookingControllerFragment.paymentModel.setOriginalChild(new ArrayList<>());
            BookingViewPagerAdapter.type = AdultsObserverAdapter.CHILD_OBSERVER_TYPE;
            BookingControllerFragment.mainContainerViewPagerAdapter.notifyDataSetChanged();
            updatePrice();
        } else {
            BookingPassengersCountFragment.this.childrenCount = 0;
            BookingControllerFragment.paymentModel.setChild(childrenCount);
            BookingControllerFragment.paymentModel.setOriginalChild(new ArrayList<>());
            BookingViewPagerAdapter.type = AdultsObserverAdapter.CHILD_OBSERVER_TYPE;
            BookingControllerFragment.mainContainerViewPagerAdapter.notifyDataSetChanged();
            updatePrice();
        }
    };


    public BookingPassengersCountFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_booking_count, null, false);
        AnalyticsUtility.logEventOpen(AnalyticsUtility.Events.BOOKING_COUNT);
        AnalyticsUtility.setScreen(this);
        CheckLanguage checkLanguage = new CheckLanguage(getActivity());
        checkLanguage.UpdateLanguage();
        updatePrice();
        binding.btnPay.setOnClickListener(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Animation slideFade2 = AnimationUtils.loadAnimation(getActivity(), R.anim.item_animation_fall);
        slideFade2.setDuration(800);
        binding.tvTitle.setAnimation(slideFade2);

        Animation adults = AnimationUtils.loadAnimation(getActivity(), R.anim.item_animation_fall);
        adults.setStartOffset(200);
        adults.setDuration(400);
        binding.tvAdults.setAnimation(adults);
        binding.tvAbove12.setAnimation(adults);


        AdultsCountAdapter adultsCountAdapter = new AdultsCountAdapter(BookingPassengersCountFragment.this.getContext(), onAdultsCount, 1, 400);
        binding.rvAdultsCount.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        binding.rvAdultsCount.setHasFixedSize(true);
        binding.rvAdultsCount.setAdapter(adultsCountAdapter);

        Animation slideFade = AnimationUtils.loadAnimation(getActivity(), R.anim.item_animation_fall);
        slideFade.setStartOffset(500);
        slideFade.setDuration(500);
        binding.layoutPrice.setAnimation(slideFade);


        AdultsCountAdapter childrenCountAdapter = new AdultsCountAdapter(BookingPassengersCountFragment.this.getContext(), onChildrenCount, 280);
        binding.rvChildrenCount.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        binding.rvChildrenCount.setHasFixedSize(true);
        binding.rvChildrenCount.setAdapter(childrenCountAdapter);

        Animation child = AnimationUtils.loadAnimation(getActivity(), R.anim.item_animation_fall);
        child.setStartOffset(350);
        child.setDuration(400);
        binding.tvChild.setAnimation(child);
        binding.tvChildBelow12.setAnimation(child);


        //  CustomAnimation.scaleWidth(binding.btnPay , 800 , 1000);

        introViewBehaviour();
    }


    private void introViewBehaviour() {

        if (BookingPassengersCountFragment.this.getContext() == null)
            return;

        boolean firstRun = UtilsPreference.getInstance(BookingPassengersCountFragment.this.getContext()).getPreference(Constants.IS_BOOK_COUNT_FIRST_RUN, true);


        if (!firstRun)
            return;


        GuideView caseView = new GuideView.Builder(BookingPassengersCountFragment.this.getContext())
                .setTitle(Objects.requireNonNull(BookingPassengersCountFragment.this.getContext()).getResources().getString(R.string.intro_book))
                .setContentText(Objects.requireNonNull(BookingPassengersCountFragment.this.getContext()).getResources().getString(R.string.book_count))
                .setTargetView(binding.layoutShowCase)
                .setGravity(Gravity.center)//optional
                .setDismissType(DismissType.anywhere)
                .build();

        caseView.dismiss();
        caseView.show();

        UtilsPreference.getInstance(BookingPassengersCountFragment.this.getContext()).savePreference(Constants.IS_BOOK_COUNT_FIRST_RUN, false);


    }

    private void updatePrice() {

        if (BookingControllerFragment.offer != null && BookingControllerFragment.offer.getPrice() != null) {

            float basAdultPrice = Float.parseFloat(BookingControllerFragment.offer.getPrice());
            float basChildPrice = Float.parseFloat(BookingControllerFragment.offer.getPrice_child());
            float totalPrice = (adultCount * basAdultPrice) + (childrenCount * basChildPrice);
            BookingControllerFragment.paymentModel.setAmount(totalPrice);
            BookingControllerFragment.paymentModel.setAdults(adultCount);
            BookingControllerFragment.paymentModel.setChild(childrenCount);
            AnalyticsUtility.logEvent(AnalyticsUtility.Events.UPDATE_PRICE, AnalyticsUtility.Events.UPDATE_PRICE);
            binding.tvPrice.setText((totalPrice + " " + BookingControllerFragment.offer.getCurrency()));

        }
    }

    @Override
    public void onClick(View v) {
        if (v == null)
            return;
        if (v.getId() == R.id.btnPay) {
            updatePrice();
            if (BookingControllerFragment.paymentModel.getAdults() <= 0) {
                Toast.makeText(BookingPassengersCountFragment.this.getContext(), R.string.can_travel, Toast.LENGTH_SHORT).show();
                return;
            }
            BookingControllerFragment.bookingContent.setCurrentItem(1, true);
        }
    }
}
