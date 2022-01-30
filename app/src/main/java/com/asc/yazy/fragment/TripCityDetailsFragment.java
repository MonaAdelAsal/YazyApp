package com.asc.yazy.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.asc.yazy.R;
import com.asc.yazy.api.model.City;
import com.asc.yazy.api.model.TDetailsModel;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.utils.AnalyticsUtility;


public class TripCityDetailsFragment extends Fragment {


    private City city;
    private TDetailsModel model;


    public TripCityDetailsFragment() {
    }

    public TripCityDetailsFragment(City city, TDetailsModel model) {
        this.city = city;
        this.model = model;

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        com.asc.yazy.databinding.FragmentCityTripBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_city_trip, null, false);
        AnalyticsUtility.setScreen(this);
        AnalyticsUtility.logEventOpen(AnalyticsUtility.Events.ABOUT);
        CheckLanguage checkLanguage = new CheckLanguage(getActivity());
        checkLanguage.UpdateLanguage();
        binding.setCity(city);

        binding.loaderAirLine.stopShimmerAnimation();
        binding.tvAirLine.setBackground(null);
        binding.loaderAirLineClass.stopShimmerAnimation();
        binding.tvClass.setBackground(null);
        binding.loaderHotel.stopShimmerAnimation();
        binding.tvHotel.setBackground(null);
        binding.loaderRoom.stopShimmerAnimation();
        binding.tvRoomType.setBackground(null);


        if (model.getOffer().getOpen_package() == 1 && model.getStart_date() != null) {
            binding.tvDate.setText(model.getStart_date());
            binding.tvDateEnd.setText(model.getEnd_date());
        } else {
            binding.tvDate.setText(model.getOffer().getDate_from());
            binding.tvDateEnd.setText(model.getOffer().getDate_to());
        }


        return binding.getRoot();
    }

}
